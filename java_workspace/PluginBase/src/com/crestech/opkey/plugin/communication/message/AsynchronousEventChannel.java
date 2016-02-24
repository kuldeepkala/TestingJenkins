package com.crestech.opkey.plugin.communication.message;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.crestech.opkey.plugin.communication.MalformedXmlException;
import com.crestech.opkey.plugin.communication.contracts.eventnotification.EventNotificationMessage;
import com.crestech.opkey.plugin.communication.contracts.eventsubscription.EventSubscriptionRequest;
import com.crestech.opkey.plugin.communication.contracts.eventsubscription.EventSubscriptionRequest.Event;
import com.crestech.opkey.plugin.communication.contracts.eventsubscription.EventSubscriptionRequest.Parameters;
import com.crestech.opkey.plugin.communication.contracts.eventsubscription.EventSubscriptionRequest.Parameters.Parameter;
import com.crestech.opkey.plugin.communication.contracts.eventsubscription.EventSubscriptionRequest.Publisher;
import com.crestech.opkey.plugin.communication.transport.ChannelClosedException;
import com.crestech.opkey.plugin.communication.transport.RawMessage;
import com.crestech.opkey.plugin.communication.transport.TransportLayer;
import com.crestech.opkey.plugin.eventhandling.EventHandler;

public class AsynchronousEventChannel implements Observer, Closeable {

	private TransportLayer transportLayer;

	private Unmarshaller notificationDeserializer;
	private Unmarshaller subscriptionDeserializer;

	private Marshaller notificationSerializer;
	private Marshaller subscriptionSerializer;

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private List<EventHandler> registeredEventHandlers = new ArrayList<>();

	
	
	

	public AsynchronousEventChannel(TransportLayer transport) throws JAXBException {
		this.transportLayer = transport;
		
		JAXBContext notificationContext = JAXBContext.newInstance(EventNotificationMessage.class);
		notificationDeserializer = notificationContext.createUnmarshaller();
		notificationSerializer = notificationContext.createMarshaller();
		notificationSerializer.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		JAXBContext subscriptionContext = JAXBContext.newInstance(EventSubscriptionRequest.class);
		subscriptionDeserializer = subscriptionContext.createUnmarshaller();
		subscriptionSerializer = subscriptionContext.createMarshaller();
		subscriptionSerializer.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	}



	


	public void subscribe(EventHandler eh) throws Exception {
		EventSubscriptionRequest req = new EventSubscriptionRequest();

		req.setEvent(new Event());
		req.setPublisher(new Publisher());
		req.setParameters(new Parameters());

		req.getEvent().setId(eh.getEventID().toString());
		req.getEvent().setName(eh.getEventName());

		req.getPublisher().setId(eh.getPublisherID().toString());

		if(eh.getFilterCriteria() != null) {
			List<Parameter> params = req.getParameters().getParameter();

			for (Entry<UUID, String> entry : eh.getFilterCriteria().entrySet()) {
				Parameter param = new Parameter();

				param.setParamid(entry.getKey().toString());
				param.setValue(entry.getValue());

				params.add(param);
			}
		}

		transportLayer.addObserver(this);
		
		registeredEventHandlers.add(eh);
		logger.finer("Requesting notifications for '" + req.getEvent().getName() + "'");
		transportLayer.SendMessage(req, subscriptionSerializer);
	}

	
	
	


	@Override
	public void update(Observable o, Object arg) {
		RawMessage rawMsg = (RawMessage)arg;
		Object msg = rawMsg.getMessage();
		Class<?> msgClass = rawMsg.getMessage().getClass();

		/*the Transport Layer has got a message from OpKey.
		 *  this class can handle only two types of messages:
		 *    1. event-notification-message
		 *    2. event-subscription-message
		 */

		if(msgClass == String.class) {
			String evString = (String)msg;

			try {
				if(rawMsg.getMessageHeader().equalsIgnoreCase("EventNotificationMessage")) {
					onEventOccur(evString);

				} else if(rawMsg.getMessageHeader().equalsIgnoreCase("EventSubscriptionMessage")) {
					onEventRequest(evString);

				} else {
					//  the message is a string but nither a notification, nor a subscription
					//  hence not our concern
				}
			} catch (MalformedXmlException e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				logger.warning(exceptionAsString);
			}

		} else if(ChannelClosedException.class.isAssignableFrom(msgClass)) {
			//TODO: after channel closed

		} else if(EventNotificationMessage.class.isAssignableFrom(msgClass)) {
			onEventOccur((EventNotificationMessage)msg);

		} else if(EventSubscriptionRequest.class.isAssignableFrom(msgClass)) {
			onEventRequest((EventSubscriptionRequest)msg);

		} else { 
			/* the message we just received was neither a 
			function-call, nor an exception. whatever
			it was, we are not bothered about it*/ 
		}
	}
	


	
	


	private void onEventOccur(String notification) throws MalformedXmlException {
		try {
			EventNotificationMessage event = (EventNotificationMessage) this.
					notificationDeserializer.unmarshal(new StringReader(notification));

			onEventOccur(event);

		} catch (JAXBException e) {
			throw new MalformedXmlException(e, notification);
		}
	}
	
	
	
	
	

	private void onEventOccur(EventNotificationMessage notification) {
		//find a suitable event handler and invoke it
		for(EventHandler eh : registeredEventHandlers.toArray(new EventHandler[registeredEventHandlers.size()])) {
			
			//TODO: try to match the event filter conditions too!
			if(eh.getEventID().toString().equalsIgnoreCase(notification.getEvent().getId())) {
				
				//TODO: pass proper information here
				logger.finer("Invoking " + eh.getClass().getSimpleName() + " for event: " + notification.getEvent().getId());
				eh.onEventOccur(null);
			}
		}
	}
	
	
	
	
	
	

	private void onEventRequest(String subscription) throws MalformedXmlException {
		try {
			EventSubscriptionRequest event = (EventSubscriptionRequest) this.
					subscriptionDeserializer.unmarshal(new StringReader(subscription));

			onEventRequest(event);

		} catch (JAXBException e) {
			throw new MalformedXmlException(e, subscription);
		}
	}

	
	
	
	
	
	private void onEventRequest(EventSubscriptionRequest subscription) {
		throw new UnsupportedOperationException();
		//TODO: onEventRequest
	}



	


	@Override
	public void close() throws IOException {
		transportLayer.deleteObserver(this);
		registeredEventHandlers.clear();
		
		transportLayer = null;
		notificationDeserializer = null;
		subscriptionDeserializer = null;
		notificationSerializer = null;
		subscriptionSerializer = null;
		registeredEventHandlers = null;
	}

}
