/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.parser.lex;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;

import com.antlersoft.util.CharClass;

/**
 * Uses a SymbolFinder to tokenize punctuation characters
 * @author Michael A. MacDonald
 *
 */
public abstract class LexWithSymbolTree implements LexState {
	protected LexState m_caller;
	SymbolFinder m_finder;

	public LexWithSymbolTree( SymbolFinderTree tree, LexState caller)
	{
		m_caller=caller;
		m_finder=new SymbolFinder( tree);
	}

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
		if ( ! isOperator( c))
		{
			cleanRemainder();
			return m_caller.nextCharacter( c);
		}
		addCharacter( c);
		return this;
    }

    public LexState endOfFile() throws IOException, RuleActionException, LexException {
		cleanRemainder();
		return m_caller.endOfFile();
    }
    
    /**
     * Determines if a character is in the range of characters tokenized by this state;
     * e.g. it's a punctuation character.  The default implementation uses
     * com.antlersoft.util.CharClass.isOperator; sub-classes can override
     * to change that.
     * @param c Character to be tested
     * @return True if the character is in the range tokenized by this class
     */
    protected boolean isOperator( char c)
    {
    	return CharClass.isOperator(c);
    }
    
    /**
     * Send a token to the parser, with a symbol identified by the SymbolFinder from the SymbolFinderTree,
     * or a default symbol if the characters passed in can't be found.
     * @param s Symbol identified by SymbolFinder, or null if character does not match an available symbol
     * @param value String associated with the symbol
     * @throws RuleActionException Problem parsing symbol
     * @throws LexException If we can't do anything with an unidentified character
     */
    abstract protected void processToken( Symbol s, String value) throws LexException, RuleActionException;

	private void addCharacter( char c)
	throws RuleActionException, LexException
	{
		if ( ! m_finder.accept( c))
		{
			rescan();
		}
		else
		{
			if ( ! m_finder.canGrow())
			{
				Symbol s=m_finder.currentSymbol();
				processToken( s, m_finder.currentText());
				m_finder.reset();
			}
		}
	}

	private void rescan()
	throws RuleActionException, LexException
	{
		Symbol s=m_finder.currentSymbol();
		if ( s!=null)
		{
			processToken( s, m_finder.currentText());
		}
		String r=null;
		if ( m_finder.isRemainder())
		{
			r=m_finder.getRemainder();
		}
		m_finder.reset();
		if ( r!=null)
		{
			int remainder_length=r.length();
			int start_index=0;
			if ( s==null)
			{
				start_index=1;
				processToken( null, r.substring( 0,1));
			}
			for ( ; start_index<remainder_length; ++start_index)
				addCharacter( r.charAt( start_index));
		}
	}

	private void cleanRemainder()
	throws RuleActionException, LexException
	{
		rescan();
		if ( m_finder.isRemainder())
		{
			String r=m_finder.getRemainder();
			m_finder.reset();
			int remainder_length=r.length();
			for ( int i=0; i<remainder_length; ++i)
			{
				processToken(  null, r.substring( i, i+1));
			}
		}
	}
}
