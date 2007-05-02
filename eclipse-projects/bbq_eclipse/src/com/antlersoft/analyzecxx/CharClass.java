/*
 * <p>Copyright (c) 2000-2007  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.analyzecxx;

public class CharClass extends com.antlersoft.util.CharClass
{
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