/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;


/**
 * DefaultHandler specialized to populate XML elements that contain no elements, only attributes
 * and text contents, represented by classes implementing ISimpleElement.
 * Implementing IElement.readFromXML for such classes, then, consists of returning an
 * instance of this.
 * @author Michael A. MacDonald
 *
 */
public class SimpleHandler extends DefaultHandler {

    private String m_current_element_name;
	private StringBuffer m_current_element_contents;
	private Attributes m_current_attributes;
	private ISimpleElement m_element;
    private IHandlerStack m_impl;
    static public AttributesImpl m_empty=new AttributesImpl();

	public SimpleHandler( IHandlerStack impl, ISimpleElement element)
	{
        m_impl=impl;
        m_element=element;
		m_current_element_contents=new StringBuffer();
	}

	// Content handler implementation
	
	public void startElement( String uri, String localname, String qname,
		Attributes attributes) throws SAXException
	{
	    m_current_element_name=qname;
		m_current_element_contents.setLength(0);
		m_current_attributes=attributes;
	}

	public void endElement( String uri, String localname, String qname)
	    throws SAXException
	{
		m_element.gotElement( m_current_element_name,
			m_current_element_contents.toString(), m_current_attributes);
        if ( m_impl!=null)
            m_impl.popHandlerStack();
	}

    public void characters( char ch[], int start, int length)
	{
	    m_current_element_contents.append( ch, start, length);
	}

    public void ignorableWhitespace( char ch[], int start, int length)
	{
	    m_current_element_contents.append( ch, start, length);
	}

    // End Content Handler Implementation
    
    
    // Accessors
    
    String getCurrentElementName() { return m_current_element_name; }
    StringBuffer getContents() { return m_current_element_contents; }
    Attributes getAttributes() { return m_current_attributes; }

    // Static utility methods
    
    /**
     * Write an element with a qname and a value but no attributes
     */
	public static void writeElement( ContentHandler handler, String name, String value)
		throws SAXException
	{
		writeElement(handler, name, value, m_empty);
	}
	
	/**
	 * Write an element with a qname and a value and some attributes
	 */
	public static void writeElement( ContentHandler handler, String name, String value, Attributes attributes)
	throws SAXException
	{
		handler.startElement( "", "", name, attributes);
		char[] buf=value.toCharArray();
		handler.characters( buf, 0, buf.length);
		handler.endElement( "", "", name);		
	}
}
