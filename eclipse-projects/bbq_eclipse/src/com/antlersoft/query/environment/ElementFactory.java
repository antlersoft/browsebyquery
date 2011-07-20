/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.util.ArrayList;

import com.antlersoft.parser.Token;

import com.antlersoft.util.xml.IElement;

/**
 * Aid to serializing query history, stored as TokenSequence objects
 * 
 * @author Michael A. MacDonald
 *
 */
class ElementFactory {
	static ElementFactory m_instance=new ElementFactory();
	
	private static final String LITERAL_TOKEN_TAG = "literal_string_token";
	private static final String TOKEN_TAG = "token";
	
	private ElementFactory() {}
	
	static ElementFactory getInstance() { return m_instance; }
	
	/**
	 * Return an object of the type implied by the XML element name qname
	 * @param qName Name of xml element
	 * @return Token object with element tag qname, or if the element name
	 * does not correspond to a token object, null
	 */
	Token getTokenForTag(String qName)
	{
		if (qName.equals(LITERAL_TOKEN_TAG))
			return new LiteralToken(null);
		else if (qName.equals(SelectionTokenElement.ELEMENT_TAG))
			return new SelectionToken(null);
		else if (qName.equals(TOKEN_TAG))
			return new Token(null, null);
		
		return null;
	}
	
	IElement getElementForToken(Token token)
	{
		if (token instanceof SelectionToken)
		{
			return new SelectionTokenElement((SelectionToken)token);
		}
		return new TokenElement(token instanceof LiteralToken ? LITERAL_TOKEN_TAG : TOKEN_TAG,
				token);
	}
	
	IElement getElementForMember(TokenSequence.Member o)
	{
		if ( o instanceof TokenSequence.TokenHolder)
			return getElementForToken(((TokenSequence.TokenHolder)o).m_token);
		
		return (IElement)o;
	}
	
	TokenSequence.Member getMemberForTag(String qName)
	{
		if (qName.equals(TokenSequence.Replacement.ELEMENT_TAG))
			return new TokenSequence.Replacement(new TokenSequence(),
				new ArrayList<TokenSequence.Member>(), 0);
		Token t = getTokenForTag(qName);
		if (t == null)
			return null;
		return new TokenSequence.TokenHolder(t);
	}
}
