package com.antlersoft.analyzecxx;

import java.io.IOException;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.RuleActionException;

public class LineComment implements LexState {
	private LexReader m_reader;
	private LexState m_caller;

	LineComment( LexReader reader, LexState caller)
	{
		m_reader=reader;
		m_caller=caller;
	}

	public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException
	{
		if ( c=='\n')
		{
			m_reader.processToken( WhiteSpace.m_new_line_token);
			return m_caller;
		}
		return this;
	}
	public LexState endOfFile() throws IOException, RuleActionException, LexException {
		m_reader.processToken( WhiteSpace.m_new_line_token);
		return m_caller.endOfFile();
	}
}
