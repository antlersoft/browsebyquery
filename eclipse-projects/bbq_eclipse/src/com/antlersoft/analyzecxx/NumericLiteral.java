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