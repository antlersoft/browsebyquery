package com.antlersoft.analyzecxx;

import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.Token;

/**
 * Base class of tokens used in the analyzer; really just a marker class.
 * The values used by the parser with be the tokens themselves; objects of
 * this class (or derived classes).
 */
class LexToken extends Token
{
	LexToken( Symbol symbol, String value)
	{
		super( symbol, value);
	}
}