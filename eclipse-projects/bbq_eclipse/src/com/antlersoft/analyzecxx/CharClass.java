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

	static String convertEscapes( String s)
	throws LexException
	{
		StringBuffer sb=new StringBuffer();
		int len=s.length();
		boolean escaped=false;
		for ( int i=0; i<len; ++i)
		{
			char ch=s.charAt( i);
			if ( escaped)
			{
				switch ( ch)
				{
					case '\'' :
					case '\\' :
					case '?' :
					case '"' :
						sb.append( ch);
						break;
					case 'a' :
						sb.append( '\07');
						break;
					case 'b' :
						sb.append( '\b');
						break;
					case 'f' :
						sb.append( '\f');
						break;
					case 'n' :
						sb.append( '\n');
						break;
					case 'r' :
						sb.append( '\r');
						break;
					case 't' :
						sb.append( '\t');
						break;
					case 'v' :
						sb.append( '\026');
						break;
					case 'x': {
						StringBuffer hex_buffer = new StringBuffer();
						++i;
						for (; i < len && isHexDigit(s.charAt(i)); ++i)
							hex_buffer.append(ch);
						if (hex_buffer.length() == 0)
							throw new LexException("Empty hex escape in " + s);
						--i;
						sb.append( (char) Integer.parseInt(hex_buffer.
							toString(), 16));
					}
					break;
					default :
					if ( isOctalDigit( ch))
					{
						StringBuffer octal_buffer=new StringBuffer();
						for ( ; i<len && isOctalDigit( s.charAt(i)); ++i)
							octal_buffer.append(ch);
						--i;
						sb.append( (char)Integer.parseInt( octal_buffer.toString(), 8));
					}
					else
						throw new LexException( "Bad character escape in "+s);
				}
			}
			else
			{
				if ( ch=='\\')
					escaped=true;
				else
					sb.append( ch);
			}
		}

		return sb.toString();
	}

}