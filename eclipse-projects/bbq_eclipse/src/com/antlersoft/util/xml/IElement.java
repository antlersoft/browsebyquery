/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.util.xml;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

/**
 * An object that can be read from or written to XML with the SAX interface can implement this interface.
 * @author Michael A. MacDonald
 *
 */
public interface IElement {

	/**
	 * Return an object that can be pushed on the stack and used to read this element
	 * (and any elements it contains)
	 * @param handler_stack Stack of handlers used to read enclosing elements
	 * @return Subclass of DefaultHandler suitable for reading this element
	 */
    public DefaultHandler readFromXML( IHandlerStack handler_stack);
    /**
     * Write this element to a ContentHandler so that it might be read back
     * with the handler returned by readFromXML
     * @param xml_writer 
     * @throws SAXException Thrown from ContentHandler methods or other problem
     * writing out object
     */
	public void writeToXML( ContentHandler xml_writer)
		throws SAXException;
	/**
	 * Return the element name of the elements that will be used to store this class
	 * @return Element name
	 */
	public String getElementTag();
}
