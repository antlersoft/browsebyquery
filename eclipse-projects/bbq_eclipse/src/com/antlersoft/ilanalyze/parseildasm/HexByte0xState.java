/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * @author Michael A. MacDonald
 *
 */
class HexByte0xState extends LexStateBase {
	private char m_buf[];
	int len;
	HexByte0xState(LexState parent, IldasmReader reader)
	{
		super(parent, reader);
		m_buf = new char[2];
	}
	/* (non-Javadoc)
	 * @see com.antlersoft.parser.lex.LexState#nextCharacter(char)
	 */
	@Override
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		m_buf[len++] = c;
		
		if (! CharClass.isHexDigit(c))
		{
			throw new LexException("Hex Byte with 0x -- bad digit " + c);
		}
		if (len == 2)
		{
			m_reader.processToken( IldasmParser.t_HEXBYTE, new Integer(Integer.parseInt( new String(m_buf), 16)));
			return m_parent;
		}
		return this;
	}

}
