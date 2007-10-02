package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.analyzecxx.LexException;
import com.antlersoft.analyzecxx.LexState;
import com.antlersoft.parser.RuleActionException;

/**
 * Default state (not within any known tokens) while lexing il file.
 * @author Michael A. MacDonald
 *
 */
class InitialState implements LexState {
	private IldasmReader m_reader;
	InitialState( IldasmReader reader)
	{
		m_reader=reader;
	}

	public LexState endOfFile() throws IOException, RuleActionException,
			LexException {
		return null;
	}

	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		LexState result=null;
		switch ( c)
		{
		case '\'' :
		case '"' :
			result=new QuoteString( this, m_reader, c);
			break;
		case '/' :
			result=new InitialSlash( this, m_reader);
			break;
		default :
			if ( CharClass.isWhiteSpace(c))
				result=this;
		}
		return result;
	}

}
