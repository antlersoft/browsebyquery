package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Symbol;

class PunctuationToken extends LexToken
{
	Symbol m_alt_symbol;

	PunctuationToken( String value, Symbol alt_symbol)
	{
		super( PreprocessParser.lex_preprocessing_op_or_punc, value);
		m_alt_symbol=alt_symbol;
	}
}