package com.crestech.opkey.plugin.communication.transport;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.crestech.opkey.plugin.communication.MalformedXmlException;

public abstract class TransportLayer extends Observable implements Runnable, UncaughtExceptionHandler, Closeable {

	private Thread _tThread;
	private State _state = State.NEW;

	public abstract void run();

	public abstract void SendMessage(Object msg, Marshaller messageSerializer) throws Exception;

	protected abstract void setup() throws Exception;

	protected abstract void cleanup();

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public State getState() {
		return _state;
	}

	public void open() throws Exception {
		_state = State.OPEN;
		
		setup(); // allow the derived class to initialize itself

		_tThread = new Thread(this);
		_tThread.setDaemon(true);
		_tThread.setName(this.getClass().getSimpleName());
		_tThread.setPriority(Thread.MIN_PRIORITY);
		_tThread.setUncaughtExceptionHandler(this);
		_tThread.start();
		
	}

	public void close() {
		_state = State.CLOSED;
		
		if (_tThread != Thread.currentThread()) {
			_tThread.interrupt();

			logger.finer(this.getClass().getSimpleName() + " is waiting for transport thread to shut down");
			try {
				_tThread.join();
			} catch (InterruptedException e) {
			}
		}

		_tThread = null;		
		cleanup();		
	}

	protected void messageReceived(Object message) {
		setChanged();

		try {
			RawMessage rawMsg = new RawMessage(message);
			notifyObservers(rawMsg);

		} catch (MalformedXmlException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Class<?> exceptionType = e.getClass();

		if (InterruptedException.class.isAssignableFrom(exceptionType)) {
			// thread has died because it was deliberately killed
			// no need to do anything at all...
			return;

		} else { // some real uncaught exception caused the death of the thread
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	protected String serialize(Object msg, Marshaller messageSerializer) throws JAXBException, UnsupportedEncodingException {
		String xmlRepresentation;

		if (msg.getClass() != String.class) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			messageSerializer.marshal(msg, buffer);
			xmlRepresentation = buffer.toString("UTF-8");

		} else { // message already in string form
			xmlRepresentation = (String) msg;
		}

		return xmlRepresentation;
	}

	public enum State {
		NEW, OPEN, CLOSED
	}
}
