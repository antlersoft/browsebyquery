package com.antlersoft.analyzecxx;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;

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
		m_preprocessor_state=new PreprocessorTokens( reader.m_lex_to_preprocess,
			reader.m_preprocess_parser);
		m_eol_backslash_state=false;
	}

	/** Call to send next character to parser; this function
	 * implements first two phases described in spec */
	public LexState nextCharacter( char c)
	throws RuleActionException, IOException, LexException
	{
		// This is where we would do trigraph processing and unsupported
		// char processing, if we were doing that
		switch (c) {
			case '\n':
				if (m_eol_backslash_state) {
					m_reader.m_line++;
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
			m_eol_backslash_state=false;
		}
		m_preprocessor_state=m_preprocessor_state.nextCharacter( c);
		if ( c=='\n')
			m_reader.m_line++;
		return this;
	}
	/** Call to indicate no more characters in file to parser */
	public LexState endOfFile( )
		throws RuleActionException, IOException, LexException
	{
		m_preprocessor_state=m_preprocessor_state.nextCharacter( '\n');
		m_preprocessor_state=m_preprocessor_state.endOfFile();
		if ( m_eol_backslash_state)
			throw new LexException( "File ends with backslash");
		m_reader.m_driver.popIncludeFile( m_reader);
		return this;
	}
}
