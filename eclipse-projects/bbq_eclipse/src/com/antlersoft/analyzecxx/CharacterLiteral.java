package com.antlersoft.analyzecxx;

class CharacterLiteral extends LexToken
{
	private boolean m_wide;

	CharacterLiteral( String value, boolean wide)
	{
		super( PreprocessParser.lex_character_literal, value);
		m_wide=wide;
	}

	boolean isWide() { return m_wide; }

	int intValue()
	{
		return (int)value.charAt(0);
	}
}