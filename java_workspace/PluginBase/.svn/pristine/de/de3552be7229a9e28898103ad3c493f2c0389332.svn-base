package com.crestech.opkey.plugin.communication.transport;

import java.net.Socket;
import java.util.logging.Logger;

public class TCPv1Channel extends AbstractStreamChannel {

	private String serverAddress = null;
	private int serverPort = -1;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public TCPv1Channel(String endpoint) {
		String[] endpointParts = endpoint.split(":");
		
		if(endpointParts.length != 2)
			throw new IllegalArgumentException(endpoint);		
			
		this.serverAddress = endpointParts[0];
		this.serverPort = Integer.parseInt(endpointParts[1]);
	}

	@Override
	protected void setup() throws Exception {
		logger.fine("Connecting to " + serverAddress + ":" + serverPort);
		
		Socket sock = new Socket(serverAddress, serverPort);
		
		_inStream = sock.getInputStream();
		_outStream = sock.getOutputStream();
	}
}
