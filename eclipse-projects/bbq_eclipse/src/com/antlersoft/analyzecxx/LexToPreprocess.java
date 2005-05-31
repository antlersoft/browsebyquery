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

import java.util.Enumeration;

import com.antlersoft.parser.Parser;
import com.antlersoft.parser.Symbol;

/**
 * Takes lex tokens, examines their sequence, converts them into pp tokens
 * (where appropriate) and passes them on to the preprocessor
 */
class LexToPreprocess implements LexReader
{
	private CxxReader m_reader;
	private boolean m_start_of_line;
	private boolean m_preprocess_line;

	LexToPreprocess( CxxReader reader)
	{
		m_reader=reader;
		m_start_of_line=true;
		m_preprocess_line=false;
	}

	public void processToken( LexToken next_token)
	throws com.antlersoft.parser.RuleActionException
	{
		if (m_start_of_line) {
			if ( next_token.symbol == PreprocessParser.lex_white_space)
				return;
			if ( ( next_token instanceof AltSymbolToken) && ((AltSymbolToken)next_token).m_alt_symbol == PreprocessParser.pp_hash) {
				m_preprocess_line = true;
				m_start_of_line=false;
				m_reader.m_preprocess_parser.errorParse( PreprocessParser.pp_hash, "#");
			}
			else
			{
				if (next_token.symbol != PreprocessParser.lex_new_line)
					m_start_of_line = false;
				if (!m_reader.m_preprocess_parser.m_skipping)
					m_reader.m_preprocess_parser.errorParse(next_token.symbol,
						next_token);
			}
		}
		else if ( next_token.symbol==PreprocessParser.lex_new_line)
		{
			if ( m_preprocess_line || ! m_reader.m_preprocess_parser.m_skipping)
				m_reader.m_preprocess_parser.errorParse( next_token.symbol, next_token);
			m_start_of_line = true;
			m_preprocess_line = false;
		}
		else if ( m_preprocess_line)
		{
			if ( next_token instanceof AltSymbolToken)
			{
				AltSymbolToken alt=(AltSymbolToken)next_token;
				for ( Enumeration e=m_reader.m_preprocess_parser.getExpectedSymbols();
					  e.hasMoreElements();)
				{
					Symbol s=(Symbol)e.nextElement();
					if ( alt.m_alt_symbol==s)
					{
						m_reader.m_preprocess_parser.errorParse(s, alt);
						alt=null;
						break;
					}
				}
				if ( alt!=null)
				{
					m_reader.m_preprocess_parser.errorParse( alt.symbol,
						alt);
				}
			}
			else
			{
				m_reader.m_preprocess_parser.errorParse( next_token.symbol,
					next_token);
			}
		}
		else if ( ! m_reader.m_preprocess_parser.m_skipping)
		{
			m_reader.m_preprocess_parser.errorParse( next_token.symbol,
												next_token);
		}
	}

	public void noMoreTokens()
	{
		m_reader.m_preprocess_parser.parse( Parser._end_);
	}
}