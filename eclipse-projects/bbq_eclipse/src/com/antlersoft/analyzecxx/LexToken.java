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

	/**
	 * Compares symbol and value for equality; used to match up
	 * replacement lists in duplicate macro definitions.
	 */
	public boolean equals( Object o)
	{
		boolean result=( o instanceof LexToken);
		if ( result)
		{
			LexToken other=(LexToken)o;
			if ( other.symbol==symbol)
			{
				result=( other.value==null) ? ( value==null) :
					( other.value.equals( value));
			}
			else
				result=false;
		}
		return result;
	}
}