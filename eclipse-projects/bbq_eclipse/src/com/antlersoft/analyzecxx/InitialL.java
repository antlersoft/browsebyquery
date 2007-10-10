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

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

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
			case '#' :
			    m_reader.processToken( new PunctuationToken( PreprocessParser.pp_wide_stringize.toString(),
					PreprocessParser.pp_wide_stringize));
			    result=m_caller;
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
