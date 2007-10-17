/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Lexer has seen 0x at the start of a token-- a hex integer follows
 * @author Michael A. MacDonald
 *
 */
class HexNumber extends LexStateBase {
	private StringBuilder m_sb;

	/**
	 * @param parent
	 * @param reader
	 */
	public HexNumber(LexState parent, IldasmReader reader) {
		super(parent, reader);
		m_sb=new StringBuilder();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		LexState result;
		if ( CharClass.isHexDigit(c))
		{
			m_sb.append( c);
			result=this;
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
	
	/**
	 * Send a hex INT64 to the parser
	 * @throws RuleActionException
	 * @throws LexException
	 */
	private void processToken() throws RuleActionException, LexException
	{
		try
		{
			if ( m_sb.length()==16 && m_sb.charAt(0)>'7')
			{
				// Large negative number
				m_reader.processToken(IldasmParser.t_INT64, new Long(0));
				return;
			}
			long val=Long.parseLong( m_sb.toString(), 16);
			m_reader.processToken(IldasmParser.t_INT64, new Long(val));
		}
		catch ( NumberFormatException nfe)
		{
			throw new LexException( "Bad hex int format: "+m_sb.toString()+" "+nfe.getMessage());
		}
	}
}
