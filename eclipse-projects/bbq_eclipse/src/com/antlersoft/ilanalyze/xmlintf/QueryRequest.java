/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.xmlintf;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;
import com.antlersoft.util.xml.ISimpleElement;
import com.antlersoft.util.xml.SimpleHandler;

/**
 * @author Michael A. MacDonald
 *
 */
public class QueryRequest {
	public static class Element implements IElement, ISimpleElement
	{
		public Element( QueryRequest req)
		{
			request=req;
		}
		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.ISimpleElement#gotElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		public void gotElement(String name, String contents, Attributes attributes) throws SAXException {
			request.setText( contents);
		}
	
		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#getElementTag()
		 */
		public String getElementTag() {
			return "QueryRequest";
		}
	
		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#readFromXML(com.antlersoft.util.xml.IHandlerStack)
		 */
		public DefaultHandler readFromXML(IHandlerStack handler_stack) {
			return new SimpleHandler(handler_stack, this);
		}
	
		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#writeToXML(org.xml.sax.ContentHandler)
		 */
		public void writeToXML(ContentHandler xml_writer) throws SAXException {
			SimpleHandler.writeElement(xml_writer, getElementTag(), request.getText());
		}
		
		QueryRequest request;
	}

	private String _text;
	
	public String getText() { return _text; }
	
	public void setText( String text) { _text = text; }

}
