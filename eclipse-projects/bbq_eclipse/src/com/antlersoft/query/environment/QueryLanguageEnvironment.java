/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.Token;

import com.antlersoft.query.BasicBase;
import com.antlersoft.query.ParserEnvironment;
import com.antlersoft.query.SetExpression;

/**
 * @author Michael A. MacDonald
 *
 */
public class QueryLanguageEnvironment extends ParserEnvironment {
	public QueryLanguageEnvironment( BasicBase parser)
	{
		m_parser=parser;
		parser.setParserEnvironment( this);
		storedValues=new HashMap();
		storedValuesSupport=new PropertyChangeSupport(this);
	}
	
    public SetExpression getExpression()
    throws ParseException
	{
	    currentIndex=0;
	    boolean errorOut=false;
	    for (; ! errorOut && currentIndex<tokens.length; currentIndex++)
	    {
	    	Token token=tokens[currentIndex];
	    	m_parser.massageToken( token);
	        errorOut=m_parser.parse( token.symbol, token.value);
	    }
	    if ( errorOut)
	    {
	        ParseException pe=new ParseException( this);
	        m_parser.reset();
	        throw pe;
	    }
	    return getLastParsedExpression();
	}
	
	public void setLine( String toParse)
	{
	    tokens=tokenize( toParse);
	}
	
	// Bean type interface for accessing stored value names
	public String[] getStoredValues()
	{
	    return (String[])storedValues.keySet().toArray( new String[0]);
	}
	
	public void addStoredValuesListener( PropertyChangeListener l)
	{
	    storedValuesSupport.addPropertyChangeListener( "storedValues", l);
	}
	
	public void removeStoredValuesListener( PropertyChangeListener l)
	{
	    storedValuesSupport.removePropertyChangeListener( l);
	}
	
	public void setStoredValue( String name, Object o)
	{
		storedValues.put( name, o);
		storedValuesSupport.firePropertyChange( "storedValues", null, this);
	}
	
	public Object getStoredValue( String name)
	{
		return storedValues.get( name);
	}
	

	// Package interface
	Token[] tokens;
	int currentIndex;
	HashMap storedValues; // String, SetExpression
	PropertyChangeSupport storedValuesSupport;
	
	Parser getParser()
	{
		return m_parser;
	}
	private BasicBase m_parser;
	
	private static void addCurrentString( StringBuffer currentString, Vector tokens)
	{
	    if ( currentString.length()>0)
	    {
	        String cs=currentString.toString();
	        currentString.setLength(0);
	        ReservedWord rw=(ReservedWord)ReservedWord.wordList.get( cs);
	        if ( rw==null)
	        {
	            tokens.addElement( new Token( BasicBase.nameSymbol, cs));
	        }
	        else
	        {
	            tokens.addElement( new Token( rw, cs));
	        }
	    }  	
	}
	
	static Token[] tokenize( String toTokenize)
	{
	    char[] chars=toTokenize.toCharArray();
	    Vector tokens=new Vector();
	    StringBuffer currentString=new StringBuffer();
	    int i=0;
	    boolean inQuoted=false;
	    for ( ; i<=chars.length; i++)
	    {
	        char c;
	        if ( i==chars.length)
	        {
	            if ( inQuoted)
	                c='"';
	            else
	                c='\n';
	        }
	        else
	        {
	            c=chars[i];
	        }
	        if ( inQuoted)
	        {
	            if ( c=='"')
	            {
	                tokens.addElement( new LiteralToken( currentString.toString()));
	                currentString.setLength(0);
	                inQuoted=false;
	            }
	            else
	                currentString.append( c);
	        }
	        else
	        {
	            switch ( c)
	            {
	            // Whitespace cases
	            case ' ' :
	            case '\r' :
	            case '\n' :
	            case '\t' :
	            	addCurrentString( currentString, tokens);
	            	break;
	            	
	            // Initial quote
	            case '"' :
	            	addCurrentString( currentString, tokens);
	                if ( c=='"')
	                {
	                    inQuoted=true;
	                }
	                break;
	                
	            // Non-quote Punctuation-- only supports single char punctuation tokens
	            case ',' :
	            case '(' :
	            case ')' :
	            	addCurrentString( currentString, tokens);
	            	currentString.append( c);
	            	addCurrentString( currentString, tokens);
	            	break;
	
	            default :
	                currentString.append( c);
	            }
	        }
	    }
	    tokens.addElement( new Token( Parser._end_, ""));
	
	    Token[] retval=new Token[tokens.size()];
	    tokens.copyInto( retval);
	    return retval;
	}

}
