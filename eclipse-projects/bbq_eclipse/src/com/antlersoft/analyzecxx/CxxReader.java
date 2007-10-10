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

import java.io.IOException;

import java.util.Properties;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Parser;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * This class processes C++ files according to the spec implemented.
 * It acts as a container for the data that the different steps in the
 * processing must share.
 * Actual IO has been relegated to the ReaderDriver class, which
 * sends next available character and end-of-file events to this object.
 */
public class CxxReader
{
	/**
	 * This is what we call when we include a file
	 */
	ReaderDriver m_driver;

	/**
	 * Interface to record things read
	 */
	DBDriver m_db;

	/**
	 * When a LexState object definitively identifies a token, it sends it
	 * here
	 */
	LexToPreprocess m_lex_to_preprocess;

	/**
	 * Parser derived class that handles inclusions and preprocessings
	 */
	PreprocessParser m_preprocess_parser;

	/**
	 * Preprocessing output goes here
	 */
	LexReader m_preprocessing_output;

	/**
	 * Initial definitions that are passed to preprocessor
	 */
	Properties m_initial_defines;

	/**
	 * Handles the first phase of processing input
	 */
	private LexState m_first_phase;

	public CxxReader( ReaderDriver driver, DBDriver db,
					  Properties initial_defines)
	{
		m_driver=driver;
		m_db=db;
		m_preprocessing_output=new DebugReader();
		m_initial_defines=initial_defines;
	}

	public void startTranslationUnit( String translation_unit)
	{
		m_lex_to_preprocess=new LexToPreprocess( this);
		m_preprocess_parser=new PreprocessParser( this, m_initial_defines);
		m_first_phase=new LineSplicer( this);
		m_db.startTranslationUnit( translation_unit);
	}

	public void finishTranslationUnit()
	{
		m_db.finishTranslationUnit();
	}

	public void setFileAndLine( String file, int line)
	{
		m_db.setCurrentFile( file);
		m_db.setCurrentLine( line);
	}

	public String getFileName()
	{
		return m_db.getCurrentFile();
	}

	public int getLineNumber()
	{
		return m_db.getCurrentLine();
	}

	public String getLocation()
	{
		StringBuffer sb=new StringBuffer( " at line ");
		sb.append( getLineNumber());
		sb.append( " in ");
		sb.append( getFileName());
		return sb.toString();
	}

	void incrementLineNumber()
	{
		m_db.setCurrentLine( m_db.getCurrentLine()+1);
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
			System.out.println( m_db.getCurrentFile()+" at line "+m_db.getCurrentLine());
			System.out.println( e.getMessage());
		}
	}

	public void endOfFile()
		throws IOException, RuleActionException, LexException
	{
		m_first_phase=m_first_phase.endOfFile();
	}

	static class DebugReader implements LexReader
	{
		public void processToken( LexToken token)
		{
			//System.out.println( token.symbol.toString()+":"+token.value);
			if ( token.symbol==PreprocessParser.lex_white_space)
				System.out.print(" ");
			else if ( token.symbol==PreprocessParser.lex_new_line)
				System.out.println("");
			else if ( token.symbol==PreprocessParser.lex_character_literal)
			{
				System.out.print('\'');
				System.out.print( token.value);
				System.out.print( '\'');
			}
			else if ( token.symbol==PreprocessParser.lex_string_literal)
			{
				System.out.print('"');
				System.out.print( token.value);
				System.out.print( '"');
			}
			else
				System.out.print( token.value);
		}
		public void noMoreTokens(){}
	}
}


