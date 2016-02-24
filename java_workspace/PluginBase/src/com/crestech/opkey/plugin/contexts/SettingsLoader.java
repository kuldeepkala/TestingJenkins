package com.crestech.opkey.plugin.contexts;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SettingsLoader {

	private DocumentBuilder builder;

	public SettingsLoader() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		builder = dbf.newDocumentBuilder();
	}

	public Map<String, String> load(File xmlFile) throws SAXException, IOException {
		Document xmlDoc = builder.parse(xmlFile);
		return load(xmlDoc);
	}

	public Map<String, String> load(InputStream in) throws SAXException, IOException {
		Document xmlDoc = builder.parse(in);
		return load(xmlDoc);
	}

	public Map<String, String> load(String xmlStr) throws SAXException,	IOException {
		InputStream is = new ByteArrayInputStream(xmlStr.getBytes());
		Document xmlDoc = builder.parse(is);
		return load(xmlDoc);
	}

	public Map<String, String> load(Document xmlDoc) {
		Element doc = xmlDoc.getDocumentElement();
		return load(doc);
	}

	public Map<String, String> load(Element documentElement) {
		HashMap<String, String> values = new HashMap<String, String>();
		NodeList nodeList = documentElement.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);

			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				
				if ((childNode.getFirstChild()) == null){
					values.put(childNode.getNodeName(), "");
				}else{
					values.put(childNode.getNodeName(), childNode.getFirstChild().getNodeValue());
				}
			}
		}

		return values;
	}
}
