package com.crestech.opkey.plugin.communication.message;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.crestech.opkey.plugin.communication.MalformedXmlException;
import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall;
import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;
import com.crestech.opkey.plugin.communication.transport.ChannelClosedException;
import com.crestech.opkey.plugin.communication.transport.RawMessage;
import com.crestech.opkey.plugin.communication.transport.TransportLayer;

public class FunctionCallChannel extends Observable implements Observer, Closeable {

	public static final String CRLF = System.lineSeparator();

	private Unmarshaller fcUnmarshaller = null;
	private Marshaller frMarshaller = null;
	
	protected TransportLayer transport = null;
	
	private Queue<FunctionCallChannelMessage> messageQueue = null;
	private Logger logger = Logger.getLogger(this.getClass().getName());	

	public FunctionCallChannel(TransportLayer transport) throws JAXBException {
		this.transport = transport;

		messageQueue = new LinkedList<FunctionCallChannelMessage>();

		// functionCall De-Serializer, i.e- from xml to object-graph
		JAXBContext fcContext = JAXBContext.newInstance(FunctionCall.class);
		fcUnmarshaller = fcContext.createUnmarshaller();		

		// functionResult Serializer, i.e- from object-grap to xml
		JAXBContext frContext = JAXBContext.newInstance(FunctionResult.class);
		frMarshaller = frContext.createMarshaller();

		frMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		frMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		this.transport.addObserver(this);
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		RawMessage rawMsg = (RawMessage) arg;
		Object msg = rawMsg.getMessage();
		Class<?> msgClass = msg.getClass();

		/*
		 * the Transport Layer has got a message from OpKey. this class can
		 * handle only two types of messages: 1. function-call 2.
		 * function-result
		 * 
		 * now, function results are sent from-plugin-to-opkey hence they cannot
		 * arrive to us. that leaves us only one option : function call. if the
		 * message is a function call then we accept it. otherwise we discard it
		 */

		if (msgClass == String.class) {
			String fcString = (String) msg;

			if (rawMsg.getMessageHeader().equalsIgnoreCase("FunctionCall")) {
				FunctionCallChannelMessage fccm = new FunctionCallChannelMessage(fcString);
				enqueueCall(fccm, FunctionCall.class.getSimpleName());

			} else {
				// the message is a string but not a function call
				// hence not our concern
			}

		} else if (ChannelClosedException.class.isAssignableFrom(msgClass)) {
			FunctionCallChannelMessage fccm = new FunctionCallChannelMessage((ChannelClosedException) msg);
			enqueueCall(fccm, ChannelClosedException.class.getSimpleName());

		} else if (FunctionCall.class.isAssignableFrom(msgClass)) {
			FunctionCallChannelMessage fccm = new FunctionCallChannelMessage((FunctionCall) msg);
			enqueueCall(fccm, FunctionCall.class.getSimpleName());
			
		} else {
			/*
			 * the message we just received was neither a function-call, nor an
			 * exception. whatever it was, we are not bothered about it
			 */
		}
	}
	
	private void enqueueCall(FunctionCallChannelMessage msg, String type) {
		messageQueue.add(msg);
		logger.finer("Enqueued " + type);
		setChanged();
		notifyObservers();
	}

	public synchronized FunctionCall NextCall() throws MalformedXmlException, ChannelClosedException {
		FunctionCall fc = messageQueue.remove().getLastMessage(fcUnmarshaller);
		return fc;
	}

	public void SendResult(FunctionResult rs) throws Throwable {
		transport.SendMessage(rs, frMarshaller);
	}

	public synchronized boolean hasMessage() throws ChannelClosedException {
		return !messageQueue.isEmpty();
	}

	@Override
	public void close() throws IOException {
		transport.deleteObserver(this);
		messageQueue.clear();

		fcUnmarshaller = null;
		transport = null;
		messageQueue = null;
	}
}

class FunctionCallChannelMessage {

	// we accept one of the following types of messages
	private FunctionCall _lastFuncCall = null;
	private String _lastFuncCallStr = null;
	private ChannelClosedException _lastException = null;

	public FunctionCallChannelMessage(String fcString) {
		_lastFuncCallStr = fcString;
	}

	public FunctionCallChannelMessage(ChannelClosedException arg) {
		_lastException = arg;
	}

	public FunctionCallChannelMessage(FunctionCall arg) {
		_lastFuncCall = arg;
	}

	public FunctionCall getLastMessage(Unmarshaller funcCallDeserializer) throws ChannelClosedException, MalformedXmlException {
		ChannelClosedException th = _lastException;
		FunctionCall fc = _lastFuncCall;
		String fcStr = _lastFuncCallStr;

		_lastException = null;
		_lastFuncCall = null;
		_lastFuncCallStr = null;

		if (th != null) {
			throw new ChannelClosedException(th);

		} else if (fc != null) {
			return fc;

		} else if (fcStr != null) {
			try {
				fc = (FunctionCall) funcCallDeserializer.unmarshal(new StringReader(fcStr));
				return fc;

			} catch (JAXBException e) {
				// the message was sure a function-call but seems to have
				// garbled
				throw new MalformedXmlException(e, fcStr);
			}

		} else {
			// all are null!! this must not happen
			return null;
		}
	}
}
