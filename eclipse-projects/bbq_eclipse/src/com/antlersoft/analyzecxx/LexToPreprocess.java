package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Token;

/**
 * Takes lex tokens, examines their sequence, converts them into pp tokens
 * (where appropriate) and passes them on to the preprocessor
 */
class LexToPreprocess
{
	private CxxReader m_reader;

	LexToPreprocess( CxxReader reader)
	{
		m_reader=reader;
	}

	void processToken( Token next_token)
	{
	}
}