/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

import java.io.IOException;
import java.util.ArrayList;

import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;
import com.antlersoft.parser.Parser;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.Token;

import com.antlersoft.query.BasicBase;

import com.antlersoft.util.CharClass;

/**
 * Breaks a query string into tokens according to a simple default scheme.
 * Strings are parsed as BasicBase.literalString tokens; names that aren't reserved
 * words in the parser are parsed as BasicBase.nameSymbol tokens.
 * Retrofitted to support the LexState interface, for a more general set of use cases.
 * 
 * @author Michael A. MacDonald
 * @see com.antlersoft.query.BasicBase
 */
public class Lexer implements LexState {

	private static final int INITIAL=0;
	private static final int IN_QUOTE=1;
	private static final int IN_QUOTE_ESCAPE=2;
	private static final int IN_NUMBER=3;
	private static final int IN_WORD=4;
	private static final int IN_PUNC=5;
	private static final int IN_FIRST_SLASH=6;
	private static final int IN_SLASH_QUOTE=7;
	private static final int IN_SLASH_ESCAPE=8;
	
	private Parser m_parser;
	private int lex_state;
	private StringBuilder currentString;
	private TokenAdder adder;
	
	public Lexer( Parser parser)
	{
		m_parser=parser;
		lex_state=INITIAL;
		currentString=new StringBuilder();
		adder = new TokenAdder() {
			public void addToken( Token t) throws RuleActionException
			{
				if ( m_parser.parse(t.symbol, t.value))
				{
					String message=m_parser.getRuleMessage();
					if ( message==null)
					{
						message="Lexer parsing error parsing "+t.symbol.toString()+": '"+t.value.toString()+"'";
					}
					throw new RuleActionException(message);
				}
			}
		};
	}
	
	interface TokenAdder
	{
		void addToken( Token t) throws RuleActionException;
	}
	
	/**
	 * May be called with 0 or one characters in the current string.
	 * If the current string is empty, adds the character c to the current string.
	 * If the current string has a character, checks if that character plus c
	 * makes a reserved word; if it does, submit both as a reserved word.
	 * Otherwise, submit the first character as a reserved word.
	 * @param c Next punctuation character to add
	 */
	private void addPunctuationCharacter( char c) throws RuleActionException
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
				addCurrentString();
				result=INITIAL;
			}
			else
			{
				currentString.setLength(0);
				currentString.append( first);
				addCurrentString();
				currentString.append( c);
			}
		}
		
		lex_state=result;
	}
	
	/**
	 * Add the token represented by the content of the current buffer to the list of tokens
	 */
	private void addCurrentString() throws RuleActionException
	{
	    if ( currentString.length()>0)
	    {
	        String cs=currentString.toString();
	        currentString.setLength(0);
	        Symbol rw=m_parser.getReservedScope().findReserved( cs);
	        if ( rw==null)
	        {
	            adder.addToken( new Token( BasicBase.nameSymbol, cs));
	        }
	        else
	        {
	            adder.addToken( new Token( rw, cs));
	        }
	    }  	
	}
	
	Token[] tokenize( String toTokenize)
	{
	    char[] chars=toTokenize.toCharArray();
	    final ArrayList<Token> tokens=new ArrayList<Token>();
	    currentString.setLength(0);
	    int i=0;
	    lex_state=INITIAL;
	    LexState ls=this;
	    TokenAdder old_adder=adder;
	    adder=new TokenAdder() {
	    	public void addToken( Token t)
	    	{
	    		tokens.add(t);
	    	}
	    };
	    try
	    {
		    for ( ; i<chars.length; i++)
		    {
		        ls=ls.nextCharacter(chars[i]);
		    }
		    ls.endOfFile();
	    }
	    catch ( IOException ioe)
	    {
	    }
	    catch ( RuleActionException rae)
	    {
	    }
	    catch ( LexException le)
	    {
	    }
	    finally
	    {
	    	adder=old_adder;
	    }
	
	    Token[] retval=new Token[tokens.size()];
	    tokens.toArray( retval);
	    return retval;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.parser.lex.LexState#endOfFile()
	 */
	public LexState endOfFile() throws IOException, RuleActionException, LexException {
	    switch ( lex_state)
	    {
	    case IN_QUOTE :
	    case IN_QUOTE_ESCAPE :
	    case IN_SLASH_QUOTE :
    		adder.addToken( new LiteralToken( currentString.toString()));
    		break;
	    case IN_SLASH_ESCAPE :
	    	currentString.append('\\');
	    	adder.addToken( new LiteralToken( currentString.toString()));
	    	break;
	    case IN_FIRST_SLASH:
	    	addPunctuationCharacter( '\\');
	    	break;
	    case IN_NUMBER :
	    	adder.addToken( new Token( BasicBase.number, currentString.toString()));
	    	break;
	    case IN_WORD :
	    case IN_PUNC :
	    	addCurrentString();
	    	break;
	    // INITIAL state, do nothing
	    }
	    adder.addToken( new Token( Parser._end_, ""));
	    return this;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.parser.lex.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
        switch ( lex_state)
        {
        case INITIAL :
        	if ( CharClass.isDigit( c))
        	{
        		currentString.append(c);
        		lex_state=IN_NUMBER;
        	}
        	else if ( c=='/')
        	{
        		lex_state=IN_FIRST_SLASH;
        	}
        	else if ( CharClass.isOperator( c))
        	{
        		addPunctuationCharacter(  c);
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
        		adder.addToken( new LiteralToken( currentString.toString()));
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
        		adder.addToken( new Token( BasicBase.number, currentString.toString()));
        		currentString.setLength(0);
        		lex_state=INITIAL;
        		return nextCharacter(c);
        	}
        	break;
        case IN_FIRST_SLASH :
        	if ( c=='/')
        		lex_state=IN_SLASH_QUOTE;
        	else
        	{
        		addPunctuationCharacter( '/');
        		return nextCharacter(c);
         	}
        	break;
        case IN_SLASH_QUOTE :
        	if ( c=='\\')
        	{
        		lex_state=IN_SLASH_ESCAPE;
        	}
        	else if ( c=='/')
        	{
        		adder.addToken( new LiteralToken( currentString.toString()));
        		currentString.setLength(0);
        		lex_state=INITIAL;
        	}
        	else
        		currentString.append( c);
        	break;
        case IN_SLASH_ESCAPE :
        	if ( c!='/')
        	{
        		currentString.append('\\');
        	}
        	currentString.append( c);
        	lex_state=IN_SLASH_QUOTE;
        	break;
        case IN_WORD :
        	if ( CharClass.isIdentifierPart(c))
        	{
        		currentString.append( c);
        	}
        	else
        	{
        		addCurrentString();
         		lex_state=INITIAL;
         		return nextCharacter(c);
        	}
        	break;
        case IN_PUNC :
        	if ( CharClass.isOperator(c) && c!='/')
        		addPunctuationCharacter( c);
        	else
        	{
        		addCurrentString( );
         		lex_state=INITIAL;
         		return nextCharacter(c);
        	}
        	break;
        }
        return this;
	}

}
