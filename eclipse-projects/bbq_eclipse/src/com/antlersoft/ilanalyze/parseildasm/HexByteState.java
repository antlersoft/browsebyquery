/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Lexing a hex byte in IL assembly language
 * @author Michael A. MacDonald
 *
 */
public class HexByteState extends LexStateBase {
	char m_buf[];

	/**
	 * @param parent
	 * @param reader
	 */
	public HexByteState(LexState parent, IldasmReader reader, char c) {
		super(parent, reader);
		m_buf=new char[2];
		m_buf[0]=c;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		if (c == 'x' && m_buf[0] == '0')
			return new HexByte0xState(m_parent, m_reader);
			
		m_buf[1]=c;
		String value=new String(m_buf);
		if ( ! CharClass.isHexDigit(c))
			throw new LexException( "Bad hex byte: "+value);
		m_reader.processToken( IldasmParser.t_HEXBYTE, new Integer(Integer.parseInt( value, 16)));

		return m_parent;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.parseildasm.LexStateBase#endOfFile()
	 */
	public LexState endOfFile() throws IOException, RuleActionException, LexException {
		throw new LexException( "End-of-file in hex byte");
	}

}
