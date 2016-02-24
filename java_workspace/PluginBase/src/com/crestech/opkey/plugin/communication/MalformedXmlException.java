package com.crestech.opkey.plugin.communication;

import javax.xml.bind.JAXBException;

public class MalformedXmlException extends Exception {

	private String fcXml;
	private String message;

	private static final long serialVersionUID = 1287110205279136306L;

	public MalformedXmlException(JAXBException e, String fcXml) {
		super(e);
		this.fcXml = fcXml;
		message = getJAXBMessage();
	}
	
	public MalformedXmlException(String message, String badXml) {
		super(message);
		this.fcXml = badXml;
		this.message = message + "\n" + badXml; 
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	private String getJAXBMessage() {
		String actualExceptionString = super.getMessage();
		
		if(actualExceptionString.contains("org.xml.sax.SAXParseException")) {
			/*
			 * this exception message goes on something like this...
			 * 
			 * 
javax.xml.bind.UnmarshalException
 - with linked exception:
[org.xml.sax.SAXParseException; lineNumber: 2; columnNumber: 2; The markup in the document preceding the root element must be well-formed.]
			 *
			 *
			 * we are about to keep only the SECOND line which
			 *  has the line number where execption occured        */
			String editedString = actualExceptionString.split("\n")[2];
			//and then append the actual xml for users to understand
			String finalString = editedString + "\n" + fcXml;
			return finalString;

		} else {
			//not as simple as thought
			return actualExceptionString;
		}
	}

}
