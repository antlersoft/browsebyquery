/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Lexing a number with a decimal and possibly and E +/-
 * @author Michael A. MacDonald
 *
 */
class FloatState extends LexStateBase {
	private StringBuilder m_sb;

	/**
	 * @param parent
	 * @param reader
	 * @param initial string of characters to put in token
	 */
	FloatState(LexState parent, IldasmReader reader, String initial) {
		super(parent, reader);
		m_sb=new StringBuilder( initial);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		LexState result=this;
		if ( c=='E' || c=='e')
		{
			if ( m_sb.indexOf("E")== -1)
				m_sb.append( 'E');
			else
			{
				processToken();
				result=m_parent.nextCharacter(c);
			}
		}
		else if ( c=='+' || c=='-')
		{
			if ( m_sb.charAt( m_sb.length()-1)=='E')
			{
				m_sb.append( c);
			}
			else
			{
				processToken();
				result=m_parent.nextCharacter(c);
			}
		}
		else if ( CharClass.isDigit(c))
		{
			m_sb.append( c);
		}
		else
		{
			processToken();
			result=m_parent.nextCharacter(c);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.parseildasm.LexStateBase#endOfFile()
	 */
	public LexState endOfFile() throws IOException, RuleActionException, LexException {
		processToken();
		return m_parent.endOfFile();
	}
	
	private void processToken() throws RuleActionException, LexException
	{
		try
		{
			double value=Double.parseDouble( m_sb.toString());
			m_reader.processToken( IldasmParser.t_FLOAT64, new Double(value));
		}
		catch ( NumberFormatException nfe)
		{
			throw new LexException( "Bad float format: "+nfe.getMessage());
		}
	}

}
