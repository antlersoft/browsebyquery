/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;
import java.io.Reader;

import com.antlersoft.analyzecxx.LexException;
import com.antlersoft.analyzecxx.LexState;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Token;

/**
 * @author Michael A. MacDonald
 *
 */
public class IldasmReader {
	
	/**
	 * Read a sequence of characters that represents an ildasm file, and add the contents to
	 * the database
	 * @param reader
	 * @throws IOException
	 * @throws LexException
	 * @throws RuleActionException
	 */
	public void Read( Reader reader) throws IOException, LexException, RuleActionException
	{
		LexState state=new InitialState();
		for ( int i=reader.read(); i>=0; i=reader.read())
		{
			state=state.nextCharacter((char)i);
		}
		state.endOfFile();
	}
}
