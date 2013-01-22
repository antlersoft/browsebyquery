/**
 * Copyright (c) 2011 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author Michael A. MacDonald
 *
 */
class SelectionTokenElement extends TokenElement {
	static final String ELEMENT_TAG = "selection_token";
	
	private SelectionToken m_token;
	
	SelectionTokenElement(SelectionToken token)
	{
		super(ELEMENT_TAG, token);
		m_token = token;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.TokenElement#writeToXML(org.xml.sax.ContentHandler)
	 */
	@Override
	public void writeToXML(ContentHandler xmlWriter) throws SAXException {
		AttributesImpl impl=new AttributesImpl();
		
		impl.addAttribute( "", "", "symbol", "CDATA", m_token.symbol.toString());
		impl.addAttribute( "", "", "value", "CDATA", m_token.value);
		impl.addAttribute( "", "", "resultClass", "CDATA", m_token.m_className);
		
		xmlWriter.startElement( "", "", getElementTag(), impl);
		xmlWriter.endElement( "", "", getElementTag());
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.environment.TokenElement#gotElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void gotElement(String name, String contents, Attributes attributes)
			throws SAXException {
		super.gotElement(name, contents, attributes);
		m_token.m_className = attributes.getValue("resultClass");
	}
}
