/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.Token;

import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;
import com.antlersoft.util.xml.ISimpleElement;
import com.antlersoft.util.xml.SimpleHandler;

/**
 * @author Michael A. MacDonald
 *
 */
class TokenElement implements IElement, ISimpleElement {
	private Token m_token;
	private String m_tag;
	
	/**
	 * 
	 */
	public TokenElement(String tag, Token token) {
		m_tag = tag;
		m_token=token;
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
		AttributesImpl impl=new AttributesImpl();
		
		impl.addAttribute( "", "", "symbol", "CDATA", m_token.symbol.toString());
		impl.addAttribute( "", "", "value", "CDATA", m_token.value);
		
		xml_writer.startElement( "", "", getElementTag(), impl);
		xml_writer.endElement( "", "", getElementTag());
	}

	public String getElementTag() { return m_tag; }

	/* (non-Javadoc)
	 * @see com.antlersoft.util.xml.ISimpleElement#gotElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void gotElement(String name, String contents, Attributes attributes)
			throws SAXException {
		m_token.symbol=Symbol.get( attributes.getValue( "symbol"));
		m_token.value=attributes.getValue( "value");
	}

}
