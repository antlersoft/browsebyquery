/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.xmlintf;

import java.util.ArrayList;

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
	public static class Element extends DefaultHandler implements IElement
	{
		public Element( QueryRequest req)
		{
			request=req;
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
			stack = handler_stack;
			return this;
		}
	
		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#writeToXML(org.xml.sax.ContentHandler)
		 */
		public void writeToXML(ContentHandler xml_writer) throws SAXException {
			throw new IllegalStateException("Implementation is read-only");
		}
		
		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if (qName.equals("ObjectKey"))
			{
				stack.startWithHandler(new ObjectKeyElement(request).readFromXML(stack), uri, localName, qName, attributes);
			}
		}

		public void endElement( String uri, String localname, String qname)
	    throws SAXException
		{
	        if ( stack!=null)
	            stack.popHandlerStack();
	        request._text = sb.toString();
		}
	
	    public void characters( char ch[], int start, int length)
		{
		    sb.append( ch, start, length);
		}
	
	    public void ignorableWhitespace( char ch[], int start, int length)
		{
		    sb.append( ch, start, length);
		}

	    StringBuilder sb = new StringBuilder();
		QueryRequest request;
		IHandlerStack stack;
	}
	
	static class ObjectKeyElement implements IElement, ISimpleElement
	{
		public ObjectKeyElement(QueryRequest r )
		{
			request = r;
		}
		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.ISimpleElement#gotElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		public void gotElement(String name, String contents, Attributes attributes) throws SAXException {
			request.addObjectKey(contents);
		}
	
		/* (non-Javadoc)
		 * @see com.antlersoft.util.xml.IElement#getElementTag()
		 */
		public String getElementTag() {
			return "ObjectKey";
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
			throw new IllegalStateException("Implementation is read-only");
		}
		
		QueryRequest request;
	}


	private String _text;
	private ArrayList<String> _selectedObjectKeys = new ArrayList<String>();
	
	public void addObjectKey(String s)
	{
		_selectedObjectKeys.add(s);
	}
	
	public String getText() { return _text; }
	
	public void setText( String text) { _text = text; }

}
