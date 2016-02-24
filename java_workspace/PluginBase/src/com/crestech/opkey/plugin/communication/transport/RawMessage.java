package com.crestech.opkey.plugin.communication.transport;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.crestech.opkey.plugin.communication.MalformedXmlException;

public class RawMessage {

	private Object _message;
	private String _header;

	public RawMessage(Object message) throws MalformedXmlException {
		if (message.getClass() == String.class) {
			String strMessage = (String) message;
			strMessage = strMessage.trim();

			try {// to parse the string using SAX parser

				SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setNamespaceAware(true);

				SAXParser saxParser = factory.newSAXParser();

				InputSource is = new InputSource(new StringReader(strMessage));

				DefaultHandler handler = new RootNodeFinder();

				saxParser.parse(is, handler);

				/*
				 * Ideally we would have got our root-node during the parse
				 * process itself. the parse process should have broken out
				 * through an exception. but if we have come here that means no
				 * exception was raised. hence no root node was found. we can
				 * safely conclude that the input string was not a valid xml
				 */
				throw new MalformedXmlException("Root node not found", strMessage);

			} catch (RootFoundException e) {
				_header = e.getRootNode();
				_message = strMessage;

			} catch (SAXParseException e) {
				throw new MalformedXmlException(e.getMessage(), strMessage);

			} catch (SAXException | IOException | ParserConfigurationException e) {
				// these will never happen
				e.printStackTrace();
			}

		} else { // the message is already a structured object
			_header = message.getClass().getSimpleName();			
			_message = message;
		}
	}

	public String getMessageHeader() {
		return _header;
	}

	public Object getMessage() {
		return _message;
	}
}

class RootNodeFinder extends DefaultHandler {

	public void startElement(String namespaceUri, String localName, String qualifiedName, Attributes attributes) throws SAXException {

		throw new RootFoundException(localName);
	}
}

class RootFoundException extends SAXException {

	private static final long serialVersionUID = -7420078297597688542L;
	private String _rootNodename;

	public RootFoundException(String rootNode) {
		_rootNodename = rootNode;
	}

	public String getRootNode() {
		return _rootNodename;
	}

}
