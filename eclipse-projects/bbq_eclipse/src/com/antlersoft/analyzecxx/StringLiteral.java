package com.antlersoft.analyzecxx;

class StringLiteral extends LexToken
{
	private boolean m_wide;

	StringLiteral( String value, boolean wide)
	{
		super( PreprocessParser.lex_string_literal, value);
		m_wide=wide;
	}

	boolean isWide() { return m_wide; }
}
