package com.crestech.opkey.plugin.communication.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.Marshaller;

public abstract class AbstractStreamChannel extends TransportLayer {

	protected InputStream _inStream = null;
	protected OutputStream _outStream = null;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private int _nextMessageLength = 0;

	@Override
	public void run() {
		while (true) {
			try {
				takeRest();

				if (!HasMessage())
					continue;

				String msg = GetMessage();
				messageReceived(msg);

			} catch (Exception e) {
				if (getState() == State.CLOSED)
					return;

				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	private boolean HasMessage() throws Exception {
		//
		// read message length
		//
		byte[] msgLenBytes = new byte[4];
		int noOfBytesRead = _inStream.read(msgLenBytes);

		if (noOfBytesRead < 4) // nothing read. channel closed
			throw new ChannelClosedException();

		//
		// extract the advertised message length from the byte array
		//
		ByteBuffer bf1 = ByteBuffer.allocate(4); // 4 bytes = 32 bits
		bf1.order(ByteOrder.LITTLE_ENDIAN);
		bf1.put(msgLenBytes).position(0);
		int len = bf1.getInt();

		_nextMessageLength = len;
		return _nextMessageLength > 0;
	}

	private String GetMessage() throws IOException {
		//
		// read message
		//
		byte[] msgBytes = new byte[_nextMessageLength];
		int noOfBytesRead = 0;
		while (noOfBytesRead < msgBytes.length) {
			int i = _inStream.read(msgBytes, noOfBytesRead, msgBytes.length - noOfBytesRead);

			if (i < 0) {
				throw new ChannelClosedException();
			}

			noOfBytesRead += i;
		}

		String msg = new String(msgBytes, "UTF-8");
		return msg;
	}

	private void takeRest() throws InterruptedException {
		Thread.sleep(10);
	}

	@Override
	public void SendMessage(Object msg, Marshaller messageSerializer) throws Exception {
		//
		// compose reply
		//
		String xmlRepresentation = serialize(msg, messageSerializer);
		byte[] msgBytes = xmlRepresentation.getBytes("utf-8");

		//
		// send reply length
		//
		ByteBuffer bf = ByteBuffer.allocate(4);
		bf.order(ByteOrder.LITTLE_ENDIAN);
		byte[] lengthBytes = bf.putInt(msgBytes.length).array();
		_outStream.write(lengthBytes);

		//
		// send reply
		//
		_outStream.write(msgBytes);
	}

	@Override
	protected void cleanup() {
		if (_inStream != null) {
			try {
				_inStream.close();
			} catch (Exception e) {
			}
		}

		if (_outStream != null) {
			try {
				_outStream.close();
			} catch (Exception e) {
			}
		}
		
		_inStream = null;
		_outStream = null;
	}

}
