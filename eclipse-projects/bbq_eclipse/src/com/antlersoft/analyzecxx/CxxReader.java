package com.antlersoft.analyzecxx;

import java.io.IOException;

import java.util.Properties;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Parser;

/**
 * This class processes C++ files according to the spec implemented.
 * It acts as a container for the data that the different steps in the
 * processing must share.
 * Actual IO has been relegated to the ReaderDriver class, which
 * sends next available character and end-of-file events to this object.
 */
public class CxxReader
{
	/** Current file (_FILE_) */
	String m_file;

	/** Current line (_LINE_) */
	int m_line;

	/** Translation unit used as key in browse db */
	String m_translation_unit;

	/**
	 * This is what we call when we include a file
	 */
	ReaderDriver m_driver;

	/**
	 * When a LexState object definitivel identifies a token, it sends it
	 * here
	 */
	LexToPreprocess m_lex_to_preprocess;

	/**
	 * Parser derived class that handles inclusions and preprocessings
	 */
	PreprocessParser m_preprocess_parser;

	/**
	 * Handles the first phase of processing input
	 */
	private LexState m_first_phase;

	public CxxReader( ReaderDriver driver, String translation_unit,
	Properties initial_defines)
	{
		m_driver=driver;
		m_lex_to_preprocess=new LexToPreprocess( this);
		m_preprocess_parser=new PreprocessParser( initial_defines);
		m_first_phase=new LineSplicer( this);
		m_line=1;
		m_file=m_translation_unit=translation_unit;
	}

	public void nextCharacter( char c)
	throws IOException, RuleActionException, LexException
	{
		try
		{
			m_first_phase = m_first_phase.nextCharacter(c);
		}
		catch ( LexException e)
		{
			System.out.println( m_file+" at line "+m_line);
			System.out.println( e.getMessage());
		}
	}

	public void endOfFile()
		throws IOException, RuleActionException, LexException
	{
		m_first_phase=m_first_phase.endOfFile()
;	}
}


