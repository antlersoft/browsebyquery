package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Symbol;

class PunctuationToken extends AltSymbolToken
{
	PunctuationToken( String value, Symbol alt_symbol)
	{
		super( PreprocessParser.lex_preprocessing_op_or_punc, value, alt_symbol);
	}
}