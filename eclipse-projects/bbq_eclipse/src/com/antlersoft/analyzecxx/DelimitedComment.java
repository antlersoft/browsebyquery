package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;
import java.io.IOException;

class DelimitedComment implements LexState {
	private boolean m_found_asterisk;
	private CxxReader m_reader;
	private LexState m_caller;

	DelimitedComment( CxxReader reader, LexState caller)
	{
		m_reader=reader;
		m_caller=caller;
System.out.println( "Create delimited comment at line "+m_reader.m_line);
	}
    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException
	{
		if ( m_found_asterisk)
		{
			if ( c=='/')
			{
				m_reader.m_lex_to_preprocess.processToken( WhiteSpace.m_white_space_token);
				return m_caller;
			}
			else
				m_found_asterisk=false;
		}
		if ( c=='*')
			m_found_asterisk=true;
		return this;
    }
    public LexState endOfFile() throws IOException, RuleActionException, LexException {
	/**@todo Implement this com.antlersoft.analyzecxx.LexState method*/
	throw new java.lang.UnsupportedOperationException("Method endOfFile() not yet implemented.");
    }
}