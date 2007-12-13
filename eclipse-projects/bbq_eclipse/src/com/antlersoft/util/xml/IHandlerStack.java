/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

/**
 * A stack of ContentHandler objects used to parse elements in an XML stream with a SAX reader
 * @author Michael A. MacDonald
 *
 */
public interface IHandlerStack {

    public void pushHandlerStack( DefaultHandler handler);
    
    /**
     * Used to implement an override of startElement in DefaultHandler, this forwards to arguments to a new
     * implementation of DefaultHandler (representing a contained element) pushed on the stack.
     * @param handler
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    public void startWithHandler( DefaultHandler handler, String uri, String localName, String qName, Attributes attributes)
    	throws SAXException;

    public void popHandlerStack( );

}
