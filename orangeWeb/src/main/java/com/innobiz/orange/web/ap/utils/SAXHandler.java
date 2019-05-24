package com.innobiz.orange.web.ap.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

	private XMLElement root = null;
	private LinkedList<XMLElement> parsingQueue = null;
	
	@Override
	public void startDocument() throws SAXException {
		parsingQueue = new LinkedList<XMLElement>();
	}
	
	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		XMLElement element = new XMLElement(qName);
		int i=0, size=atts==null ? 0 : atts.getLength();
		for(i=0;i<size;i++){
			element.addAttr(atts.getQName(i), atts.getValue(i));
		}
		if(parsingQueue.isEmpty()) root = element;
		else parsingQueue.getLast().addChild(element);
		parsingQueue.add(element);
	}
	
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		parsingQueue.removeLast();
	}
	
	@Override
	public void characters(char ch[], int start, int length) {
		XMLElement element = parsingQueue.getLast();
		String va = new String(ch, start, length);
		if(!va.trim().isEmpty() || !element.isEmptyValue()){
			element.appendValue(ch, start, length);
		}
	}
	
	public static XMLElement parse(String xmlString) throws SAXException, IOException, ParserConfigurationException{
		if(xmlString==null || xmlString.isEmpty()) return null;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		SAXHandler handler = new SAXHandler();
		saxParser.parse(new InputSource(new StringReader(xmlString)), handler);
		return handler.root;
	}
}
