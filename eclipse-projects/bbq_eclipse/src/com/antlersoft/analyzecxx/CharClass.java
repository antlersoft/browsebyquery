package com.antlersoft.analyzecxx;

public class CharClass
{
	public static final boolean isWhiteSpace( char c)
	{
		boolean result=false;
		switch ( c)
		{
		case ' ':
		case '\t':
		case '\r':
		case '\b':
		case '\n':
		case '\f':
			result=true;
		}
		return result;
	}

	public static final boolean isDigit( char c)
	{
		return c>='0' && c<='9';
	}

	public static final boolean isOctalDigit( char c)
	{
		return c>='0' && c<='7';
	}

	public static final boolean isHexDigit( char c)
	{
		return isDigit( c) || ( c>='A' && c<='F' ) || ( c>='a' && c<='f');
	}

	public static final boolean isIdentifierStart( char c)
	{
		return c=='_' || ( c>='a' && c<='z' ) || ( c>='A' && c<='Z');
	}

	public static final boolean isIdentifierPart( char c)
	{
		return isIdentifierStart( c) || isDigit( c);
	}

	public static final boolean isOperator( char c)
	{
		return ! ( isIdentifierPart(c) || c=='\'' || c=='"' || isWhiteSpace(c));
	}
}