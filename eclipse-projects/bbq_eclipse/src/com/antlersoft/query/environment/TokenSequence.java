/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.ReservedScope;
import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.Token;

import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;
import com.antlersoft.util.xml.SimpleHandler;

/**
 * Represents a sequence of tokens that are interpreted together, and optionally the object
 * that represents the result of interpreting them.
 * <p>
 * A token sequence can be converted to a string form, which can be converted back into
 * an equivalent token sequence by feeding it back to the parserenvironment.
 * </p>
 * 
 * @author Michael A. MacDonald
 *
 */
public class TokenSequence {
	/**
	 * Array of Token and Replacement objects that, when complete, is interpreted
	 * as a unit.
	 */
	private ArrayList m_contents;
	private Object m_result;

	TokenSequence()
	{
		m_contents=new ArrayList();
	}
	
	void addToken( Symbol sym, String val)
	{
		m_contents.add( new Token( sym, val));
	}
	
	/**
	 * Replace the most recent token_count tokens with a given sequence.
	 * @param token_count Number of tokens the sequence replaces
	 * @param to_add Replacing sequence
	 */
	void replaceTokensBySequence( int token_count, TokenSequence to_add)
	{
		Replacement r=new Replacement( to_add, m_contents, token_count);
		int size=m_contents.size();
		for ( int i=size-1; i>size-token_count; --i)
			m_contents.remove( i);
		m_contents.set( size-token_count, r); 
	}
	
	void setResult( Object result)
	{
		m_result=result;
	}
	
	public Object getResult()
	{
		return m_result;
	}
	
	void add( Object o)
	{
		m_contents.add( o);
	}
	
	public List getContents()
	{
		return Collections.unmodifiableList(m_contents);
	}
	
	/**
	 * If the last token is an expression separator or an _end_ token, it is not part of the token sequence
	 * and is removed. 
	 *
	 */
	void throwAwayClosingToken( ReservedScope scope)
	{
		int size=m_contents.size();
		if ( size>0)
		{
			Object o=m_contents.get( size-1);
			if ( o instanceof Token)
			{
				Token t=(Token)o;
				if ( t.symbol==Parser._end_ || t.symbol==scope.findReserved(";"))
					m_contents.remove( size-1);
			}
		}
	}
	
	void collectTokens( ReservedScope scope, Collection tokens)
	{
		for ( Iterator it=m_contents.iterator(); it.hasNext();)
		{
			Object o=it.next();
			if ( o instanceof Replacement)
			{
				tokens.add( new Token( scope.getReserved(  "("), "("));
				((Replacement)o).getReplacer().collectTokens( scope, tokens);
				tokens.add( new Token( scope.getReserved( ")"), ")"));
			}
			else
				tokens.add( o);
		}
	}
	
	/**
	 * Holds a reference to a replacing token sequence and information about the tokens it replaces;
	 * also includes the XML infrastructure for saving and loading itself.
	 * @author Michael A. MacDonald
	 *
	 */
	public static class Replacement extends DefaultHandler implements IElement
	{
		private TokenSequence m_replacer;
		private ArrayList m_replaced;
		
		static final String ELEMENT_TAG="replacement";
		
		/**
		 * State for reading from XML
		 */
		private IHandlerStack m_stack;
		
		Replacement( TokenSequence replacer, ArrayList contents, int token_count)
		{
			int size=contents.size();
			if ( size<token_count)
				throw new IllegalArgumentException( "Trying to replace "+token_count+" tokens when only "+size+" are in sequence.");
			m_replacer=replacer;
			m_replaced=new ArrayList( token_count);
			for ( int i=token_count; i>0; --i)
			{
				assert( contents.get( size-i) instanceof Token);
				m_replaced.add( contents.get( size-i));
			}			
		}
		
		TokenSequence getReplacer()
		{
			return m_replacer;
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
			xml_writer.startElement( "", "", "replaced", SimpleHandler.m_empty);
			for ( Iterator it=m_replaced.iterator(); it.hasNext();)
			{
				ElementFactory.getInstance().getElementForObject( it.next()).writeToXML( xml_writer);
			}
			xml_writer.endElement( "", "", "replaced");
			ElementFactory.getInstance().getElementForObject( m_replacer).writeToXML( xml_writer);
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
				// Don't leave reference to handler lying around
				m_stack=null;
			}
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if ( qName.equals( TokenSequenceElement.ELEMENT_TAG))
			{
				m_replacer=new TokenSequence();
				m_stack.startWithHandler( new TokenSequenceElement( m_replacer).readFromXML( m_stack),
						uri, localName, qName, attributes);
			}
			else if ( qName.equals( TokenElement.ELEMENT_TAG))
			{
				Token replaced_token=new Token( null, null);
				m_replaced.add( replaced_token);
				m_stack.startWithHandler( ElementFactory.getInstance().getElementForObject( replaced_token).readFromXML( m_stack),
						uri, localName, qName, attributes);
			}
		}
	}
}
