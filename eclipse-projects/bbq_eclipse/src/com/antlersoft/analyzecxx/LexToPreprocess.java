package com.antlersoft.analyzecxx;

import java.util.Enumeration;

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
			if ( ( next_token instanceof AltSymbolToken) && ((AltSymbolToken)next_token).m_alt_symbol == PreprocessParser.pp_hash) {
				m_preprocess_line = true;
				m_start_of_line=false;
				m_reader.m_preprocess_parser.parse( PreprocessParser.pp_hash, "#");
			}
			else
			{
				if (next_token.symbol != PreprocessParser.lex_new_line)
					m_start_of_line = false;
				if (!m_reader.m_preprocess_parser.m_skipping)
					m_reader.m_preprocess_parser.parse(next_token.symbol,
						next_token);
			}
		}
		else if ( next_token.symbol==PreprocessParser.lex_new_line)
		{
			if ( m_preprocess_line || ! m_reader.m_preprocess_parser.m_skipping)
				m_reader.m_preprocess_parser.parse( next_token.symbol, next_token);
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
						m_reader.m_preprocess_parser.parse(s);
						alt=null;
						break;
					}
				}
				if ( alt!=null)
				{
					m_reader.m_preprocess_parser.parse( alt.symbol,
						alt);
				}
			}
			else
			{
				m_reader.m_preprocess_parser.parse( next_token.symbol,
					next_token);
			}
		}
		else if ( ! m_reader.m_preprocess_parser.m_skipping)
		{
			m_reader.m_preprocess_parser.parse( next_token.symbol,
												next_token);
		}
	}

	public void noMoreTokens()
	{
	}
}