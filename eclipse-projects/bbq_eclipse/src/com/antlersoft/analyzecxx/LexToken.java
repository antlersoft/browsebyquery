package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.Token;

class LexToken extends Token
{
	LexToken( Symbol symbol, String value)
	{
		super( symbol, value);
	}

	Object getValue()
	{
		return value;
	}
}