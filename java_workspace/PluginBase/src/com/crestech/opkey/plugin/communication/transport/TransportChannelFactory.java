package com.crestech.opkey.plugin.communication.transport;

import com.crestech.opkey.plugin.contexts.CommunicationProtocol;

public class TransportChannelFactory {

	/**
	 * @deprecated use the other overload instead.
	 */
	@Deprecated
	public static TransportLayer getTransport(String communicationProtocol, String communicationEndpoint) throws ClassNotFoundException {
		if (communicationProtocol.equalsIgnoreCase("SHARED_DB_v02")) {
			return getTransport(CommunicationProtocol.SHARED_DB_v02, communicationEndpoint);

		} else if (communicationProtocol.equalsIgnoreCase("NAMED_PIPE_v01")) {
			return getTransport(CommunicationProtocol.NAMED_PIPE_v01, communicationEndpoint);

		} else {
			return new XmlFileChannel(communicationEndpoint);
		}
	}

	public static TransportLayer getTransport(CommunicationProtocol communicationProtocol, String communicationEndpoint) throws ClassNotFoundException {
		if (communicationProtocol == CommunicationProtocol.SHARED_DB_v02) {
			return new SharedDBv2Channel(communicationEndpoint);

		} else if (communicationProtocol == CommunicationProtocol.NAMED_PIPE_v01) {
			return new NamedPipev1Channel(communicationEndpoint);
			
		} else if (communicationProtocol == CommunicationProtocol.XML_FILE) {
			return new XmlFileChannel(communicationEndpoint);
			
		} else if (communicationProtocol == CommunicationProtocol.BLOCKING_INVOCATION) {
			return new BlockingInvocationChannel();
			
		} else if (communicationProtocol == CommunicationProtocol.TCP_v01) {
			return new TCPv1Channel(communicationEndpoint);

		} else {
			throw new IllegalArgumentException("Unknown protocol " + communicationProtocol.toString());
		}
	}
}
