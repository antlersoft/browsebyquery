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
}