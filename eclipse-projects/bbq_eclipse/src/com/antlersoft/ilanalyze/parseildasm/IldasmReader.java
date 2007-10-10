/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;

import java.text.MessageFormat;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Logger;


import com.antlersoft.ilanalyze.DBDriver;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Manages reading an IL assembly-language file and putting the information in the BBQ database
 * (via the DBDriver interface)
 * @author Michael A. MacDonald
 *
 */
public class IldasmReader {
	
    private static Logger logger=Logger.getLogger( IldasmReader.class.getName());
	private IldasmParser m_parser;
	private DBDriver m_driver;
	/** A format string for forming the disassembly command for an assembly from the commands path */
	private String m_disassembly_command_format;
	/** Number of lines seen */
	private int m_line_count;
	/** Text of current line (for error messages) */
	private StringBuilder m_line;
	
	/**
	 * Map of symbol name to symbol; cache of expected symbols so we can identify if multiple symbols
	 * are expected efficiently.
	 */
	private HashMap m_expected;
	
	public IldasmReader()
	{
		m_parser=new IldasmParser();
		m_line=new StringBuilder();
		m_disassembly_command_format="monodis {0}";
	}
	
	/**
	 * Read a sequence of characters that represents an ildasm file, and add the contents to
	 * the database
	 * @param reader
	 * @throws IOException
	 * @throws LexException
	 * @throws RuleActionException
	 */
	public void Read( DBDriver driver, Reader reader) throws IOException, RuleActionException
	{
		LexState state=new InitialState( this);
		m_driver=driver;
		m_parser.reset();
		m_parser.setDriver(m_driver);
		m_expected=null;
		m_line_count=1;
		m_line.setLength(0);
		try
		{
			for ( int i=reader.read(); i>=0; i=reader.read())
			{
				char c=(char)i;
				if ( c=='\n')
				{
					m_line_count++;
					m_line.setLength(0);
				}
				else
					m_line.append(c);
			}
			state.endOfFile();
			logger.fine( "lines read: "+m_line_count);
		}
		catch ( Exception e)
		{
			RuleActionException rae=new RuleActionException( "error: "+e.getMessage()+" at line: "+m_line_count+"\n"+m_line.toString());
			rae.setStackTrace(e.getStackTrace());
			throw rae;
		}
	}
	
	/**
	 * Process a token from the lexing, sending it to the parser
	 * @param symbol Symbol representing token from the IL file
	 * @param value Value of the symbol
	 */
	void processToken( Symbol symbol, Object value) throws RuleActionException
	{
		m_expected=null;
		if ( m_parser.parse(symbol, value))
		{
			String message=m_parser.getRuleMessage();
			if ( message==null)
				message="Parsing error";
			throw new RuleActionException(message);
		}
	}

	/**
	 * Find if a string matches one of the expected terminals of the parser
	 * @param found String that has been found by the lexer
	 * @return Symbol expected by the parser, or null if no matching symbol
	 */
	Symbol expectedReserved( String found)
	{
		if ( m_expected==null)
		{
			m_expected=new HashMap();
			for ( Enumeration e=m_parser.getExpectedSymbols(); e.hasMoreElements();)
			{
				Symbol s=(Symbol)e.nextElement();
				m_expected.put(s.toString(), s);
			}
		}
		return (Symbol)m_expected.get( found);
	}
	
	/**
	 * Return a collection of strings representing the parser's reserved words
	 * @return Collection of strings
	 */
	Collection getReservedWords()
	{
		return m_parser.getReservedScope().getReservedStrings();
	}
	
	public void sendFileToDriver( File file, DBDriver driver) throws IOException, RuleActionException
	{
		if ( file.isDirectory())
		{
			File[] files=file.listFiles();
			for ( int i=0; i<files.length; ++i)
			{
				sendFileToDriver( files[i], driver);
			}
		}
		else
		{
			String lower_file=file.getName().toLowerCase();
			if ( lower_file.endsWith(".il"))
			{
				logger.fine( "Reading .il file "+file.getAbsolutePath());
				Read( driver, new FileReader(file));
			}
			else if ( lower_file.endsWith(".dll") || lower_file.endsWith(".exe"))
			{
				logger.fine("Reading assembly file "+file.getAbsolutePath());
				Process process=Runtime.getRuntime().exec(MessageFormat.format(m_disassembly_command_format, new Object[] { file.getAbsolutePath() }));
				Read( driver, new InputStreamReader(process.getInputStream()));
				try
				{
					process.waitFor();
				}
				catch ( InterruptedException ie)
				{
					logger.warning( "Interrupted waiting for reader process: " + ie.getMessage());
				}
			}
		}
	}
	
	public static void main( String[] args) throws Exception
	{
		new IldasmReader().sendFileToDriver( new File(args[0]), null);
	}
}
