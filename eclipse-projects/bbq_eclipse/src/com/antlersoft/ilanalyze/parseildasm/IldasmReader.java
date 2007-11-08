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
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.antlersoft.ilanalyze.DBDriver;
import com.antlersoft.ilanalyze.LoggingDBDriver;

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
	/** A format string for forming the disassembly command for an assembly from the assembly's path */
	private String m_disassembly_command_format;
	/** Number of lines seen */
	private int m_line_count;
	/** Text of current line (for error messages) */
	private StringBuilder m_line;
	/** Pattern directory must match to pull .dll's and .exe's */
	private Pattern m_directory_match;
	/** Oldest modification date to consider for file to add to database */
	private Date m_oldest;
	
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
		if ( System.getProperty("os.name", "Finux").contains("Windows"))
		{
			m_disassembly_command_format="ildasm /text /nobar /linenumber {0}";
			try
			{
				m_directory_match=Pattern.compile( "obj\\\\Debug\\z");
			}
			catch ( PatternSyntaxException pse)
			{
				logger.warning(pse.getLocalizedMessage());
			}
		}
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
				if ( i>256)
					continue;
				char c=(char)i;
				m_line.append(c);
				state=state.nextCharacter(c);
				if ( c=='\n')
				{
					m_line_count++;
					m_line.setLength(0);
				}
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
		if ( logger.isLoggable(Level.FINEST))
			logger.finest("Parsing "+value.toString());
		if ( m_parser.parse(symbol, value))
		{
			String message=m_parser.getRuleMessage();
			if ( message==null)
			{
				message="Parsing error parsing "+symbol.toString()+": '"+value.toString()+"'";
			}
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
	
	/**
	 * Set a date; won't add .exe or .dll files to the database older than that date
	 * @param oldest Files older than this won't be added to the database (it assumes most recent version
	 * is already there)
	 */
	public void setOldestFileDate( Date oldest)
	{
		m_oldest=oldest;
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
				driver.startAnalyzedFile(file.getAbsolutePath());
				Read( driver, new FileReader(file));
				driver.endAnalyzedFile();
			}
			else if ( lower_file.endsWith(".dll") || lower_file.endsWith(".exe"))
			{
				if ( m_directory_match!=null)
				{
					if ( ! m_directory_match.matcher( file.getParentFile().getAbsolutePath()).find())
					{
						if ( logger.isLoggable(Level.FINER))
							logger.finer( "Rejecting "+file.getAbsolutePath()+" because directory doesn't match "+m_directory_match.pattern());
						return;
					}
				}
				if ( m_oldest!=null)
				{
					if ( file.lastModified()<m_oldest.getTime())
					{
						if ( logger.isLoggable(Level.FINER))
							logger.finer( "Rejecting "+file.getAbsolutePath()+" because it is older than "+m_oldest.toString() );
						return;						
					}
				}
				logger.fine("Reading assembly file "+file.getAbsolutePath());
				driver.startAnalyzedFile(file.getAbsolutePath());
				Process process=Runtime.getRuntime().exec(MessageFormat.format(m_disassembly_command_format, new Object[] { file.getAbsolutePath() }));
				Read( driver, new InputStreamReader(process.getInputStream()));
				driver.endAnalyzedFile();
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
		com.antlersoft.ilanalyze.db.ILDB db=new com.antlersoft.ilanalyze.db.ILDB(new File(args[1]));
		try
		{
			new IldasmReader().sendFileToDriver( new File(args[0]), new LoggingDBDriver( new com.antlersoft.ilanalyze.db.ILDBDriver( db)));
		}
		finally
		{
			db.close();
		}
	}
}
