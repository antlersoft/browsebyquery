/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.analyzecxx.LexException;
import com.antlersoft.analyzecxx.LexState;
import com.antlersoft.parser.RuleActionException;

/**
 * A single or double quoted string in an IL file
 * @author Michael A. MacDonald
 *
 */
public class QuoteString extends LexStateBase {
	private StringBuilder m_sb;
	private IldasmReader m_reader;
	private char m_quote;
	private LexState m_parent;
	
	/**
	 * 
	 * @param reader
	 * @param c which character quotes the string?
	 */
	QuoteString( LexState parent, IldasmReader reader, char c)
	{
		super( parent, reader);
		m_quote=c;
		m_sb=new StringBuilder();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		if ( c=='\\')
		{
			return new EscapeInStringState( this, m_reader, m_sb);
		}
		if ( c==m_quote)
		{
			m_reader.processToken(
					m_quote=='"' ? IldasmParser.t_QSTRING : IldasmParser.t_SQSTRING,
							m_sb.toString()
					);
			return m_parent;
		}
		m_sb.append( c);
		return this;
	}
	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#endOfFile()
	 */
	public LexState endOfFile() throws IOException, RuleActionException,
			LexException {
		throw new LexException( "Unexpected end of file in string");
	}
}
