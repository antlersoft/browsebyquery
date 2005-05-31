/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
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