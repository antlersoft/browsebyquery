/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

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
	
	private ElementFactory() {}
	
	static ElementFactory getInstance() { return m_instance; }
	
	IElement getElementForObject( Object o)
	{
		if ( o instanceof Token)
			return new TokenElement( (Token)o);
		else if ( o instanceof TokenSequence)
			return new TokenSequenceElement( (TokenSequence)o);
		
		return (IElement)o;
	}
}
