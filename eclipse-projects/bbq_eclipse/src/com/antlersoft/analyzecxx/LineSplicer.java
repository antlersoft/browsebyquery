package com.antlersoft.analyzecxx;

/**
 * Performs first two stages of translation processing described in
 * the spec.
 */
class LineSplicer implements LexState
{
	/**
	 * Set to true when we have encountered backslash that might be
	 * prior to end of line
	 */
	private boolean m_eol_backslash_state;

	/**
	 * State called with character after line splice processing
	 */
	private LexState m_preprocessor_state;

	private CxxReader m_reader;

	LineSplicer( CxxReader reader)
	{
		m_reader=reader;
		m_preprocessor_state=new PreprocessorTokens();
		m_eol_backslash_state=false;
	}

	/** Call to send next character to parser; this function
	 * implements first two phases described in spec */
	public LexState nextCharacter( char c)
	{
		// This is where we would do trigraph processing and unsupported
		// char processing, if we were doing that
		switch (c) {
			case '\n':
				m_reader.m_line++;
				if (m_eol_backslash_state) {
					m_eol_backslash_state = false;
					return this;
				}
				break;
			case '\\':
				if (!m_eol_backslash_state) {
					m_eol_backslash_state = true;
					return this;
				}
				break;
		}
		if (m_eol_backslash_state)
		{
			m_preprocessor_state=m_preprocessor_state.nextCharacter('\\');
		}
		m_preprocessor_state=m_preprocessor_state.nextCharacter( c);
		return this;
	}
	/** Call to indicate no more characters in file to parser */
	public LexState endOfFile( )
	throws LexException
	{
		m_preprocessor_state=m_preprocessor_state.endOfFile( );
		if ( m_eol_backslash_state)
			throw new LexException( "File ends with \\");
		return this;
	}
}
