package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;
import java.io.IOException;

class InitialPeriod implements LexState {

	private CxxReader m_reader;
	private LexState m_caller;

	InitialPeriod( CxxReader reader, LexState caller)
	{
		m_reader=reader;
		m_caller=caller;
	}

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
		if ( c>='0' && c<='9')
		{
			return new LexNumber( m_reader, m_caller, '.').nextCharacter( c);
		}
		return new LexPunctuation( m_reader, m_caller, '.').nextCharacter( c);
    }
    public LexState endOfFile() throws IOException, RuleActionException, LexException {
	/**@todo Implement this com.antlersoft.analyzecxx.LexState method*/
	throw new java.lang.UnsupportedOperationException("Method endOfFile() not yet implemented.");
    }
}