/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;
import java.io.Reader;

import java.util.HashMap;

import com.antlersoft.analyzecxx.LexException;
import com.antlersoft.analyzecxx.LexState;

import com.antlersoft.ilanalyze.DBDriver;

import com.antlersoft.parser.ReservedScope;
import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;

/**
 * @author Michael A. MacDonald
 *
 */
public class IldasmReader {
	
	private IldasmParser m_parser;
	private DBDriver m_driver;
	private HashMap m_expected;
	
	/**
	 * Read a sequence of characters that represents an ildasm file, and add the contents to
	 * the database
	 * @param reader
	 * @throws IOException
	 * @throws LexException
	 * @throws RuleActionException
	 */
	public void Read( DBDriver driver, Reader reader) throws IOException, LexException, RuleActionException
	{
		LexState state=new InitialState( this);
		m_driver=driver;
		m_expected=null;
		m_parser=new IldasmParser( m_driver);
		for ( int i=reader.read(); i>=0; i=reader.read())
		{
			state=state.nextCharacter((char)i);
		}
		state.endOfFile();
	}
	
	/**
	 * Process a token from the lexing, sending it to the parser
	 * @param symbol Symbol representing token from the IL file
	 * @param value Value of the symbol
	 */
	void processToken( Symbol symbol, Object value)
	{
	
	}

	/**
	 * Find if a string matches one of the expected terminals of the parser
	 * @param found String that has been found by the lexer
	 * @result Symbol expected by the parser, or null if no matching symbol
	 */
	Symbol expectedReserved( )
	{
	
	}
}
