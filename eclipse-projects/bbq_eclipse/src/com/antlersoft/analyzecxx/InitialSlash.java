package com.antlersoft.analyzecxx;

import java.io.IOException;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.RuleActionException;

public class InitialSlash implements LexState {
	private Parser m_parser;
	private LexState m_caller;

	InitialSlash( Parser parser, LexState caller)
	{
		m_parser=parser;
		m_caller=caller;
	}

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException
	{
		switch ( c)
		{
			case '/' : return new LineComment( m_parser, m_caller);
			case '*' : return new DelimitedComment( m_parser, m_caller);
		}
		LexState result=new LexPunctuation( m_parser, m_caller).nextCharacter( '/');
		return result.nextCharacter( c);
    }
    public LexState endOfFile() throws IOException, RuleActionException, LexException {
		LexState result=new LexPunctuation( m_parser, m_caller).nextCharacter( '/');
		return result.endOfFile();
    }
}