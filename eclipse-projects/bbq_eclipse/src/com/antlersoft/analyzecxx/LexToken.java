package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.Token;

class LexToken extends Token
{
	Symbol m_alt_symbol;

	LexToken( Symbol symbol, String value, Symbol alt_symbol)
	{
		super( symbol, value);
		m_alt_symbol=alt_symbol;
	}
}