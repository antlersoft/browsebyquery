package com.antlersoft.analyzecxx;

import java.io.IOException;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.RuleActionException;

/**
 * Processes whitespace found by the calling LexState (PreprocessorTokens)
 */
public class WhiteSpace implements LexState
{
	private LexState m_caller;
	private LexReader m_lex;
	static LexToken m_white_space_token=new LexToken( PreprocessParser.lex_white_space, "");
	static LexToken m_new_line_token=new LexToken( PreprocessParser.lex_new_line, "");

	WhiteSpace( LexReader lex, LexState caller, char c)
		throws RuleActionException, IOException, LexException
	{
		m_lex=lex;
		m_caller=caller;
		nextCharacter( c);
	}

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException
	{
		if ( CharClass.isWhiteSpace( c))
		{
			if ( c=='\n')
			{
				m_lex.processToken(m_new_line_token);
				return m_caller;
			}
			return this;
		}
		m_lex.processToken( m_white_space_token);
		return m_caller.nextCharacter(c);
    }

    public LexState endOfFile() throws IOException, RuleActionException, LexException {
		m_lex.processToken( m_white_space_token);
		return m_caller.endOfFile();
    }
}