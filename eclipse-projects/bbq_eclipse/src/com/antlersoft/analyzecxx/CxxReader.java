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

	/**
	 * This is what we call when we include a file
	 */
	private ReaderDriver m_driver;

	/**
	 * Parser derived class that handles inclusions and preprocessings
	 */
	PreprocessParser m_preprocess_parser;

	/**
	 * Handles the first phase of processing input
	 */
	private LexState m_first_phase;

	public CxxReader( ReaderDriver driver, Properties initial_defines)
	{
		m_driver=driver;
		m_preprocess_parser=new PreprocessParser();
		m_first_phase=new PreprocessorTokens();
	}

	public void nextCharacter( char c)
	throws IOException, RuleActionException, LexException
	{
		m_first_phase=m_first_phase.nextCharacter( c);
	}

	public void endOfFile()
		throws IOException, RuleActionException, LexException
	{
		m_first_phase=m_first_phase.endOfFile()
;	}
}


