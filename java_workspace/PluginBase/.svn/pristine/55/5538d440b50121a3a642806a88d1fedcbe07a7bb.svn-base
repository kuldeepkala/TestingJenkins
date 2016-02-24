package com.crestech.opkey.plugin.communication.transport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.xml.bind.Marshaller;

public class XmlFileChannel extends TransportLayer {

	private String xmlFileName = "";
	private File xmlFile = null;
	private String content = "";
	private Scanner scanner = null;

	private int noOfTransmissions = 1;
	private Date startTime;
	
	private PrintStream out = System.out;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public XmlFileChannel(String xmlFileName) {
		this.xmlFileName = xmlFileName;
		this.startTime = new Date();
	}

	@Override
	protected void setup() throws Exception {
		xmlFile = new File(xmlFileName);
		if (!xmlFile.exists())
			throw new FileNotFoundException(xmlFileName);

		scanner = new Scanner(xmlFile);
		scanner.useDelimiter("\\Z|\r\n\r\n"); // end of file or double
												// line-break
	}

	@Override
	protected void cleanup() {
		if (scanner != null)
			scanner.close();

		scanner = null;
		xmlFile = null;

		out.println("Total number of transmissions: " + --noOfTransmissions);

		long diffMiliSec = new Date().getTime() - startTime.getTime();
		long diffSeconds = diffMiliSec / 1000 % 60;
		long diffMinutes = diffMiliSec / (60 * 1000) % 60;
		long diffHours = diffMiliSec / (60 * 60 * 1000);

		out.println("Time in milli seconds: " + diffMiliSec + " milli seconds.");
		out.println("Time in seconds: " + diffSeconds + " seconds.");
		out.println("Time in minutes: " + diffMinutes + " minutes.");
		out.println("Time in hours: " + diffHours + " hours.");
	}

	@Override
	public void run() {
		try {

			while (true) {
				Thread.sleep(10000);

				if (!scanner.hasNext()) {
					logger.warning("!!! EOF !!!");
					messageReceived(new ChannelClosedException());
					break;
				}

				content = scanner.next();
				messageReceived(content);
			}

		} catch (InterruptedException e) {
			return;
		}
	}

	@Override
	public void SendMessage(Object msg, Marshaller messageSerializer) throws Exception {
		String xmlRepresentation = serialize(msg, messageSerializer);

		out.println(xmlRepresentation);
		out.println("Transmission count: " + noOfTransmissions++);
	}
}
