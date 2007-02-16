/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.util.Map;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;
import com.antlersoft.util.xml.SimpleHandler;

/**
 * @author Michael A. MacDonald
 *
 */
class EnvironmentElement extends DefaultHandler implements IElement {

	private QueryLanguageEnvironment m_environment;
	private IHandlerStack m_stack;
	private final static String ELEMENT_TAG="query_language_environment";
	
	/**
	 * 
	 */
	public EnvironmentElement( QueryLanguageEnvironment qle) {
		m_environment=qle;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.util.xml.IElement#readFromXML(com.antlersoft.util.xml.IHandlerStack)
	 */
	public DefaultHandler readFromXML(IHandlerStack handler_stack) {
		m_stack=handler_stack;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.util.xml.IElement#writeToXML(org.xml.sax.ContentHandler)
	 */
	public void writeToXML(ContentHandler xml_writer) throws SAXException {
		xml_writer.startElement( "", "", ELEMENT_TAG, SimpleHandler.m_empty);
		AttributesImpl impl=new AttributesImpl();
		
		for ( Iterator i=m_environment.storedValues.entrySet().iterator();
			i.hasNext();)
		{
			Map.Entry entry=(Map.Entry)i.next();

			impl.clear();
			impl.addAttribute( "", "", "name", "CDATA", (String)entry.getKey());
			
			xml_writer.startElement( "", "", "stored_value", impl);
			
			new TokenSequenceElement((TokenSequence)entry.getValue()).writeToXML( xml_writer);
			
			xml_writer.endElement( "", "", "stored_value");
		}
		if ( m_environment.m_previous_sequence!=null)
		{
			xml_writer.startElement( "", "", "previous_sequence", SimpleHandler.m_empty);
			new TokenSequenceElement( m_environment.m_previous_sequence).writeToXML( xml_writer);
			xml_writer.endElement( "", "", "previous_sequence");
		}
		
		xml_writer.endElement( "", "", ELEMENT_TAG);
	}

	public String getElementTag() { return ELEMENT_TAG; }

	public void startElement( String uri, String localname, String qname, Attributes attr)
		throws SAXException
	{
		if ( qname=="stored_value")
		{
			String name=attr.getValue( "name");
			TokenSequence seq=new TokenSequence();
			m_environment.storedValues.put( name, seq);
			m_stack.startWithHandler( new TokenSequenceElement( seq).readFromXML( m_stack),
					uri, localname, qname, attr);
		}
		if ( qname=="previous_sequence")
		{
			m_environment.m_previous_sequence=new TokenSequence();
			m_stack.startWithHandler( new TokenSequenceElement( m_environment.m_previous_sequence)
					.readFromXML( m_stack),
					uri, localname, qname, attr);
		}
	}

	public void endElement( String uri, String localname, String qname)
	{
		if ( qname==ELEMENT_TAG)
		{
			m_stack.popHandlerStack();
		}
	}
}
