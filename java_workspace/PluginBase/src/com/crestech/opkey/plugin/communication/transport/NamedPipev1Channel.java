package com.crestech.opkey.plugin.communication.transport;

import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class NamedPipev1Channel extends AbstractStreamChannel {

	private String _pipeNameRoot = "";

	public NamedPipev1Channel(String pipeNameRoot) {
		_pipeNameRoot = pipeNameRoot;
	}

	@Override
	protected void setup() throws Exception {
		String rPipeName = "\\\\.\\pipe\\OPKEY_TO_PLUGIN__" + _pipeNameRoot;
		String wPipeName = "\\\\.\\pipe\\PLUGIN_TO_OPKEY__" + _pipeNameRoot;

		ReadableByteChannel rChannel = new RandomAccessFile(rPipeName, "r").getChannel();		
		WritableByteChannel wChannel = new RandomAccessFile(wPipeName, "rw").getChannel();

		_inStream = Channels.newInputStream(rChannel);
		_outStream = Channels.newOutputStream(wChannel);
	}
}