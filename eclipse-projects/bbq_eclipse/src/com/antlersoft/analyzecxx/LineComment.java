package com.antlersoft.analyzecxx;

import java.io.IOException;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.RuleActionException;

public class LineComment implements LexState {
	private CxxReader m_reader;
	private LexState m_caller;

	LineComment( CxxReader reader, LexState caller)
	{
		m_reader=reader;
		m_caller=caller;
System.out.println( "Created line comment at line "+m_reader.m_line);
	}

	public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException
	{
		if ( c=='\n')
		{
			m_reader.m_lex_to_preprocess.processToken( WhiteSpace.m_new_line_token);
			return m_caller;
		}
		return this;
	}
	public LexState endOfFile() throws IOException, RuleActionException, LexException {
		m_reader.m_lex_to_preprocess.processToken( WhiteSpace.m_new_line_token);
		return m_caller.endOfFile();
	}
}
