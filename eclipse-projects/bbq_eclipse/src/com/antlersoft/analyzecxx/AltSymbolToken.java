package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Symbol;

class AltSymbolToken extends LexToken
{
	Symbol m_alt_symbol;

	AltSymbolToken( Symbol symbol, String value, Symbol alt_symbol)
	{
		super( symbol, value);
		m_alt_symbol=alt_symbol;
	}
}