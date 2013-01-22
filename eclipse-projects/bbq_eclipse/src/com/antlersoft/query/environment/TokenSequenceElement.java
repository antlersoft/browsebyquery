/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;

/**
 * @author Michael A. MacDonald
 *
 */
class TokenSequenceElement extends DefaultHandler implements IElement {
	
	static final String ELEMENT_TAG="token_sequence";

	private TokenSequence m_sequence;
	private IHandlerStack m_stack;
	
	/**
	 * 
	 */
	public TokenSequenceElement( TokenSequence seq) {
		super();
		m_sequence=seq;
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
		AttributesImpl impl=new AttributesImpl();
		
		impl.addAttribute( "", "", "result_class", "CDATA", m_sequence.getResult()==null ?
				"" : m_sequence.getResult().getClass().getName());
		
		xml_writer.startElement( "", "", ELEMENT_TAG, impl);
		
		for ( Iterator<TokenSequence.Member> it=m_sequence.getContents().iterator(); it.hasNext();)
		{
			ElementFactory.getInstance().getElementForMember( it.next()).writeToXML( xml_writer);
		}
		
		xml_writer.endElement( "", "", ELEMENT_TAG);
	}
	
	public String getElementTag() { return ELEMENT_TAG; }

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ( qName.equals( ELEMENT_TAG))
		{
			m_stack.popHandlerStack();
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if ( qName.equals( ELEMENT_TAG))
		{
			m_sequence.setResult( attributes.getValue( "result_class"));
		}
		else
		{
			TokenSequence.Member m = ElementFactory.getInstance().getMemberForTag(qName);
			if (m != null)
			{
				m_sequence.add(m);
				m_stack.startWithHandler( ElementFactory.getInstance().getElementForMember(m).
						readFromXML(m_stack), uri, localName, qName, attributes);
			}
		}
	}

}
