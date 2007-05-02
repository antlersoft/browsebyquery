/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.util.Vector;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.Token;

import com.antlersoft.query.BasicBase;

import com.antlersoft.util.CharClass;

/**
 * Breaks a query string into tokens
 * 
 * @author Michael A. MacDonald
 *
 */
class Lexer {

	private static final int INITIAL=0;
	private static final int IN_QUOTE=1;
	private static final int IN_QUOTE_ESCAPE=2;
	private static final int IN_NUMBER=3;
	private static final int IN_WORD=4;
	private static final int IN_PUNC=5;
	
	private Parser m_parser;
	
	Lexer( Parser parser)
	{
		m_parser=parser;
	}
	
	/**
	 * May be called with 0 or one characters in the current string.
	 * If the current string is empty, adds the character c to the current string.
	 * If the current string has a character, checks if that character plus c
	 * makes a reserved word; if it does, submit both as a reserved word.
	 * Otherwise, submit the first character as a reserved word.
	 * @param currentString StringBuffer holding 0 or 1 unmatched punctuation characters
	 * @param c Next punctuation character to add
	 * @param tokens Token list
	 * @return Parser state when finished
	 */
	int addPunctuationCharacter( StringBuffer currentString, char c, Vector tokens)
	{
		int result=IN_PUNC;
		if ( currentString.length()==0)
		{
			currentString.append( c);
		}
		else
		{
			String first=currentString.toString();
			currentString.append( c);
			String both=currentString.toString();
			if ( m_parser.getReservedScope().findReserved( both)!=null)
			{
				addCurrentString( currentString, tokens);
				result=INITIAL;
			}
			else
			{
				currentString.setLength(0);
				currentString.append( first);
				addCurrentString( currentString, tokens);
				currentString.append( c);
			}
		}
		
		return result;
	}
	
	/**
	 * Add the token represented by the content of the current buffer to the list of tokens
	 * @param currentString Current buffer of received characters
	 * @param tokens List of tokens
	 */
	private void addCurrentString( StringBuffer currentString, Vector tokens)
	{
	    if ( currentString.length()>0)
	    {
	        String cs=currentString.toString();
	        currentString.setLength(0);
	        Symbol rw=m_parser.getReservedScope().findReserved( cs);
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
	
	Token[] tokenize( String toTokenize)
	{
	    char[] chars=toTokenize.toCharArray();
	    Vector tokens=new Vector();
	    StringBuffer currentString=new StringBuffer();
	    int i=0;
	    int lex_state=INITIAL;
	    for ( ; i<chars.length; i++)
	    {
	        char c=chars[i];
	        switch ( lex_state)
	        {
	        case INITIAL :
	        	if ( CharClass.isDigit( c))
	        	{
	        		currentString.append(c);
	        		lex_state=IN_NUMBER;
	        	}
	        	else if ( CharClass.isOperator( c))
	        	{
	        		lex_state=addPunctuationCharacter( currentString, c, tokens);
	        	}
	        	else if ( c=='"')
	        	{
	        		lex_state=IN_QUOTE;
	        	}
	        	else if ( ! CharClass.isWhiteSpace( c))
	        	{
	        		currentString.append( c);
	        		lex_state=IN_WORD;
	        	}
	        	break;
	        case IN_QUOTE :
	        	if ( c=='\\')
	        	{
	        		lex_state=IN_QUOTE_ESCAPE;
	        	}
	        	else if ( c=='"')
	        	{
	        		tokens.addElement( new LiteralToken( currentString.toString()));
	        		currentString.setLength(0);
	        		lex_state=INITIAL;
	        	}
	        	else
	        		currentString.append( c);
	        	break;
	        case IN_QUOTE_ESCAPE :
	        	currentString.append( c);
	        	lex_state=IN_QUOTE;
	        	break;	        	
	        case IN_NUMBER :
	        	if ( CharClass.isDigit( c))
	        	{
	        		currentString.append( c);
	        	}
	        	else
	        	{
	        		tokens.addElement( new Token( BasicBase.number, currentString.toString()));
	        		currentString.setLength(0);
	        		--i;
	        		lex_state=INITIAL;
	        	}
	        	break;
	        case IN_WORD :
	        	if ( CharClass.isIdentifierPart(c))
	        	{
	        		currentString.append( c);
	        	}
	        	else
	        	{
	        		addCurrentString( currentString, tokens);
	        		--i;
	        		lex_state=INITIAL;
	        	}
	        	break;
	        case IN_PUNC :
	        	if ( CharClass.isOperator(c))
	        		lex_state=addPunctuationCharacter( currentString, c, tokens);
	        	else
	        	{
	        		addCurrentString( currentString, tokens);
	        		--i;
	        		lex_state=INITIAL;	        		
	        	}
	        	break;
	        }
	    }
	    switch ( lex_state)
	    {
	    case IN_QUOTE :
	    case IN_QUOTE_ESCAPE :
    		tokens.addElement( new LiteralToken( currentString.toString()));
    		break;
	    case IN_NUMBER :
	    	tokens.addElement( new Token( BasicBase.number, currentString.toString()));
	    	break;
	    case IN_WORD :
	    case IN_PUNC :
	    	addCurrentString( currentString, tokens);
	    	break;
	    // INITIAL state, do nothing
	    }
	    tokens.addElement( new Token( Parser._end_, ""));
	
	    Token[] retval=new Token[tokens.size()];
	    tokens.copyInto( retval);
	    return retval;
	}

}
