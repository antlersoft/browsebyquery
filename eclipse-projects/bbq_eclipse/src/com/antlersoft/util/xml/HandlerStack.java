/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.util.xml;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Michael A. MacDonald
 *
 */
public class HandlerStack implements IHandlerStack {

	private Stack m_handler_stack;
	private XMLReader m_reader;
	
	/**
	 * 
	 */
	public HandlerStack( XMLReader reader) {
		m_handler_stack=new Stack();
		m_reader=reader;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.util.xml.IHandlerStack#pushHandlerStack(org.xml.sax.helpers.DefaultHandler)
	 */
	public void pushHandlerStack(DefaultHandler handler) {
        m_handler_stack.push( handler);
        if ( m_reader!=null)
        {
            m_reader.setContentHandler( handler);
            m_reader.setErrorHandler( handler);
        }
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.util.xml.IHandlerStack#popHandlerStack()
	 */
	public void popHandlerStack() {
        m_handler_stack.pop();
        if ( m_reader!=null && m_handler_stack.size()>0)
        {
            DefaultHandler handler=(DefaultHandler)m_handler_stack.peek();
            m_reader.setContentHandler( handler);
            m_reader.setErrorHandler( handler);
        }
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.util.xml.IHandlerStack#pushHandlerStack(org.xml.sax.helpers.DefaultHandler, java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startWithHandler(DefaultHandler handler, String uri, String localName, String qName, Attributes attributes)
		throws SAXException {
		pushHandlerStack( handler);
		handler.startElement( uri, localName, qName, attributes);
	}

}
