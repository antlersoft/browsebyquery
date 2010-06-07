/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.antlersoft.ilanalyze.DBDriver;
import com.antlersoft.ilanalyze.LoggingDBDriver;
import com.antlersoft.ilanalyze.db.ILDB;
import com.antlersoft.ilanalyze.db.ILDBDriver;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

import com.antlersoft.query.environment.Lexer;

/**
 * Manages reading a single IL assembly-language file and putting the information in the BBQ database
 * (via the DBDriver interface)
 * @author Michael A. MacDonald
 *
 */
public class IldasmReader {
	
    static Logger logger=Logger.getLogger( IldasmReader.class.getName());
	private IldasmParser m_parser;
	private ResourceParser m_resource_parser;
	/** Number of lines seen */
	private int m_line_count;
	/** Text of current line (for error messages) */
	private StringBuilder m_line;
	/**
	 * Map of symbol name to symbol; cache of expected symbols so we can identify if multiple symbols
	 * are expected efficiently.
	 */
	private HashMap<String,Symbol> m_expected;
	
	public IldasmReader()
	{
		m_parser=new IldasmParser();
		m_resource_parser=new ResourceParser();
		m_line=new StringBuilder();
	}
	
	/**
	 * Reads characters from a Reader and sends them to a lexer represented by an initial state,
	 * keeping track of line number and position.
	 * @param reader
	 * @param state
	 * @throws IOException
	 * @throws RuleActionException
	 */
	private void readToLexer( Reader reader, LexState state)
	 throws IOException, RuleActionException
	{
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
		catch ( LexException le)
		{
			RuleActionException rae=new RuleActionException( "error: "+le.getMessage()+" at line: "+m_line_count+"\n"+m_line.toString(), le);
			rae.setStackTrace(le.getStackTrace());
			throw rae;			
		}
		catch ( RuleActionException e)
		{
			RuleActionException rae=new RuleActionException( "error: "+e.getMessage()+" at line: "+m_line_count+"\n"+m_line.toString(),e);
			rae.setStackTrace(e.getStackTrace());
			throw rae;
		}		
	}
	
	/**
	 * Read a sequence of characters that represents an ildasm file, and add the contents to
	 * the database
	 * @param driver
	 * @param reader
	 * @throws IOException
	 * @throws RuleActionException
	 */
	public void Read( DBDriver driver, Reader reader) throws IOException, RuleActionException
	{
		LexState state=new InitialState( this);
		m_parser.reset();
		m_parser.setDriver(driver);
		m_parser.setReader(reader);
		m_expected=null;
		readToLexer(reader, state);
	}
	
	public void readResources( DBDriver driver, Reader reader) throws IOException, RuleActionException
	{
		m_resource_parser.setDriver(driver);
		m_resource_parser.reset();
		Lexer lexer=new Lexer( m_resource_parser);
		readToLexer( reader, lexer);
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
			m_expected=new HashMap<String,Symbol>();
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
	
}
