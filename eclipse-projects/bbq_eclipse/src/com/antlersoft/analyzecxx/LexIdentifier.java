package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;
import java.io.IOException;

public class LexIdentifier implements LexState {
	private CxxReader m_reader;
	private LexState m_caller;
	private StringBuffer m_buffer;

	LexIdentifier( CxxReader reader, LexState caller, char initial)
	{
		m_reader=reader;
		m_caller=caller;
		m_buffer=new StringBuffer( 10);
		m_buffer.append( initial);
	}
    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
		LexState result=this;
		if ( CharClass.isIdentifierPart( c))
		{
			m_buffer.append( c);
		}
		else
		{
			m_reader.m_lex_to_preprocess.processToken( new LexToken(
			PreprocessParser.lex_identifier, m_buffer.toString()));
			result=m_caller.nextCharacter( c);
		}
		return result;
    }
    public LexState endOfFile() throws IOException, RuleActionException, LexException {
	/**@todo Implement this com.antlersoft.analyzecxx.LexState method*/
	throw new java.lang.UnsupportedOperationException("Method endOfFile() not yet implemented.");
    }
}