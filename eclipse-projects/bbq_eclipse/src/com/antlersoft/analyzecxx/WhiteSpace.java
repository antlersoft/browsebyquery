package com.antlersoft.analyzecxx;

import java.io.IOException;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.RuleActionException;

/**
 * Processes whitespace found by the calling LexState (PreprocessorTokens)
 */
public class WhiteSpace implements LexState
{
	public static final boolean isWhiteSpace( char c)
	{
		boolean result=false;
		switch ( c)
		{
		case ' ':
		case '\t':
		case '\r':
		case '\b':
		case '\n':
		case '\f':
			result=true;
		}
		return result;
	}

	private boolean m_is_new_line;
	private LexState m_caller;
	private LexToPreprocess m_lex;

	WhiteSpace( LexToPreprocess lex, LexState caller, char c)
	{
		m_lex=lex;
		m_caller=caller;
		m_is_new_line=false;
		nextCharacter( c);
	}

    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException
	{
		if ( isWhiteSpace( c))
		{
			if ( c=='\n')
				m_is_new_line=true;
			return this;
		}
		m_parser.parse( m_is_new_line ? PreprocessParser.NEW_LINE : PreprocessParser.WHITE_SPACE);
		return m_caller.nextCharacter(c);
    }
    public LexState endOfFile() throws IOException, RuleActionException, LexException {
		m_parser.parse( m_is_new_line ? PreprocessParser.NEW_LINE : PreprocessParser.WHITE_SPACE);
		return m_caller.endOfFile();
    }
}