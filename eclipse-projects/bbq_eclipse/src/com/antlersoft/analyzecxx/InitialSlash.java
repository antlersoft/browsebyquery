package com.antlersoft.analyzecxx;

import java.io.IOException;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.RuleActionException;

class InitialSlash implements LexState {
	private LexReader m_reader;
	private LexState m_caller;

	InitialSlash( LexReader reader, LexState caller)
	{
		m_reader=reader;
		m_caller=caller;
	}

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException
	{
		switch ( c)
		{
			case '/' : return new LineComment( m_reader, m_caller);
			case '*' : return new DelimitedComment( m_reader, m_caller);
		}
		LexState result=new LexPunctuation( m_reader, m_caller, '/');
		return result.nextCharacter( c);
    }
    public LexState endOfFile() throws IOException, RuleActionException, LexException {
		LexState result=new LexPunctuation( m_reader, m_caller, '/');
		return result.endOfFile();
    }
}