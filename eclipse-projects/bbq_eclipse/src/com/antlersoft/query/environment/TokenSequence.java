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
	 * Interface indicating that the object is a constituent of a
	 * token sequence
	 * @author Michael A. MacDonald
	 */
	interface Member
	{
		/**
		 * Add the token(s) represented by this member to the collection
		 * @param scope Scope for reserved word tokens, used to retrieve parentheses used
		 * to delimit replacing parts
		 * @param tokens Collection of tokens in which to expand
		 */
		void collectTokens( ReservedScope scope, Collection<Token> tokens);		
	}
	
	static class TokenHolder implements Member
	{
		Token m_token;
		
		TokenHolder(Token t)
		{
			m_token = t;
		}
		
		/* (non-Javadoc)
		 * @see com.antlersoft.query.environment.TokenSequence.Member#collectTokens(com.antlersoft.parser.ReservedScope, java.util.Collection)
		 */
		public void collectTokens(ReservedScope scope, Collection<Token> tokens) {
			tokens.add(m_token);
		}

		@Override
		public String toString()
		{
			return m_token.toString();
		}
	}
	
	/**
	 * Array of Token and Replacement objects that, when complete, is interpreted
	 * as a unit.
	 */
	private ArrayList<Member> m_contents;
	private Object m_result;

	TokenSequence()
	{
		m_contents=new ArrayList<Member>();
	}
	
	void addToken(Token token)
	{
		try {
			m_contents.add(new TokenHolder((Token)token.clone()));
		} catch (CloneNotSupportedException e) {
		}
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
	
	void add(Member o)
	{
		m_contents.add( o);
	}
	
	public List<Member> getContents()
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
			Member o=m_contents.get( size-1);
			if ( o instanceof TokenHolder)
			{
				Token t=((TokenHolder)o).m_token;
				if ( t.symbol==Parser._end_ || t.symbol==scope.findReserved(";"))
					m_contents.remove( size-1);
			}
		}
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		
		for ( Iterator<Member> i=m_contents.iterator(); i.hasNext();)
		{
			sb.append( i.next().toString());
			if ( i.hasNext())
				sb.append(' ');
		}
		
		return sb.toString();
	}
	
	/**
	 * Recursively add the tokens in this sequence to the collection, expanding any Replacement
	 * objects as you go
	 * @param scope Scope for reserved word tokens, used to retrieve parentheses used
	 * to delimit replacing parts
	 * @param tokens Collection of tokens in which to expand
	 */
	void collectTokens( ReservedScope scope, Collection<Token> tokens)
	{
		for ( Iterator<Member> it=m_contents.iterator(); it.hasNext();)
		{
			Member o=it.next();
			if ( o instanceof Replacement)
			{
				tokens.add( new Token( scope.getReserved(  "("), "("));
				((Replacement)o).getReplacer().collectTokens( scope, tokens);
				tokens.add( new Token( scope.getReserved( ")"), ")"));
			}
			else
				tokens.add(((TokenHolder)o).m_token);
		}
	}
	
	/**
	 * Holds a reference to a replacing token sequence and information about the tokens it replaces;
	 * also includes the XML infrastructure for saving and loading itself.
	 * @author Michael A. MacDonald
	 *
	 */
	public static class Replacement extends DefaultHandler implements IElement, Member
	{
		private TokenSequence m_replacer;
		private ArrayList<Token> m_replaced;
		
		static final String ELEMENT_TAG="replacement";
		
		/**
		 * State for reading from XML
		 */
		private IHandlerStack m_stack;
		
		Replacement( TokenSequence replacer, ArrayList<Member> contents, int token_count)
		{
			int size=contents.size();
			if ( size<token_count)
				throw new IllegalArgumentException( "Trying to replace "+token_count+" tokens when only "+size+" are in sequence.");
			m_replacer=replacer;
			m_replaced=new ArrayList<Token>( token_count);
			for ( int i=token_count; i>0; --i)
			{
				assert( contents.get( size-i) instanceof TokenHolder);
				m_replaced.add( ((TokenHolder)contents.get( size-i)).m_token);
			}			
		}
		
		TokenSequence getReplacer()
		{
			return m_replacer;
		}
		
		public String toString()
		{
			StringBuilder sb=new StringBuilder();
			sb.append( "( ");
			sb.append( m_replacer.toString());
			sb.append( " )");
			return sb.toString();
		}

		/* (non-Javadoc)
		 * @see com.antlersoft.query.environment.TokenSequence.Member#collectTokens(com.antlersoft.parser.ReservedScope, java.util.Collection)
		 */
		public void collectTokens(ReservedScope scope, Collection<Token> tokens) {
			tokens.add( new Token( scope.getReserved(  "("), "("));
			getReplacer().collectTokens( scope, tokens);
			tokens.add( new Token( scope.getReserved( ")"), ")"));
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
			for ( Iterator<Token> it=m_replaced.iterator(); it.hasNext();)
			{
				ElementFactory.getInstance().getElementForToken( it.next()).writeToXML( xml_writer);
			}
			xml_writer.endElement( "", "", "replaced");
			new TokenSequenceElement(m_replacer).writeToXML( xml_writer);
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
			else
			{
				Token replaced_token=ElementFactory.getInstance().getTokenForTag(qName);
				if (replaced_token != null)
				{
					m_replaced.add( replaced_token);
					m_stack.startWithHandler( ElementFactory.getInstance().getElementForToken( replaced_token).readFromXML( m_stack),
							uri, localName, qName, attributes);
				}
			}
		}
	}
}
