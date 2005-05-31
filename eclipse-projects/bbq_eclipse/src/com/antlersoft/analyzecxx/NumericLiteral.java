/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
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

class NumericLiteral extends LexToken
{
	static final int DECIMAL=1;
	static final int FLOAT=2;
	static final int HEX=3;
	static final int OCTAL=4;

	private int m_type;
	private boolean m_is_long;
	private boolean m_is_unsigned;
	private boolean m_is_single_precision;

	NumericLiteral()
	{
		super( PreprocessParser.lex_number, null);
		m_type=1;
		m_is_long=false;
		m_is_unsigned=false;
		m_is_single_precision=false;
	}

	NumericLiteral( int i)
	{
		this();
		value=Integer.toString( i);
	}

	void setRepresentation( String rep)
	{
		value=rep;
	}

	void setType( int type)
	{
		m_type=type;
	}

	void setFlag( char c)
	{
		switch ( c)
		{
			case 'U' : m_is_unsigned=true; break;
							case 'L' : m_is_long=true; break;
							case 'F' : m_is_single_precision=true; break;
		}
	}

	int intValue()
	throws LexException
	{
		int offset=0;
		int radix=10;
		int i=value.length()-1;
		for ( ; i>=0 && ( m_type==HEX ? ! CharClass.isHexDigit( value.charAt( i)) : ! CharClass.isDigit( value.charAt( i))); --i);
		int good_length=i+1;
		if ( m_type==FLOAT)
		{
			return (int)Double.parseDouble( value);
		}
		else if ( m_type==HEX)
		{
			offset = 2;
			radix=16;
		}
		else if ( m_type==OCTAL)
		{
			offset = 1;
			radix=8;
		}
		if ( good_length-offset<1)
			throw new LexException( "No good digits in string");
		if ( offset==0 && good_length==value.length())
			return (int)Long.parseLong( value);
		return (int)Long.parseLong( value.substring( offset, good_length-offset), radix);
	}
}