/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Michael A. MacDonald
 *
 */
public interface IHandlerStack {

    public void pushHandlerStack( DefaultHandler handler);
    
    public void startWithHandler( DefaultHandler handler, String uri, String localName, String qName, Attributes attributes)
    	throws SAXException;

    public void popHandlerStack( );

}
