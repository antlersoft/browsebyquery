package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;
import java.io.IOException;

class InitialL implements LexState {

	private CxxReader m_reader;
	private LexState m_caller;

	InitialL( CxxReader reader, LexState caller)
	{
		m_reader=reader;
		m_caller=caller;
	}
    public LexState nextCharacter(char c)
	throws IOException, RuleActionException, LexException
	{
		LexState result;
		switch ( c)
		{
			case '\'' : result=new CharacterLiteral( m_reader, m_caller, true);
			break;
			case '"' : result=new StringLiteral( m_reader, m_caller, true);
			break;
			default : result=new LexIdentifier( m_reader, m_caller, 'L');
				result=result.nextCharacter( c);
		}
		return result;
    }
    public LexState endOfFile() throws IOException, RuleActionException, LexException {
	/**@todo Implement this com.antlersoft.analyzecxx.LexState method*/
	throw new java.lang.UnsupportedOperationException("Method endOfFile() not yet implemented.");
    }
}