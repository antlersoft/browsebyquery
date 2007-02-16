/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.util.xml;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Michael A. MacDonald
 *
 */
public interface IElement {

    public DefaultHandler readFromXML( IHandlerStack handler_stack);
	public void writeToXML( ContentHandler xml_writer)
		throws SAXException;
	public String getElementTag();
}
