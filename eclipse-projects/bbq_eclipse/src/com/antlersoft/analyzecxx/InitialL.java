package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;
import java.io.IOException;

class InitialL implements LexState {

	private LexReader m_reader;
	private LexState m_caller;

	InitialL( LexReader reader, LexState caller)
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
			case '\'' :
				result=new QuotedLiteral( m_reader, this, c,
						  new CharacterLiteral( null, true));
			break;
			case '"' :
				result=new QuotedLiteral( m_reader, this, c,
						  new StringLiteral( null, true));
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
