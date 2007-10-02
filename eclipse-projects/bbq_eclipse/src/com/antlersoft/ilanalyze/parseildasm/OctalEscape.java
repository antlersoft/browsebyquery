/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.analyzecxx.LexException;
import com.antlersoft.analyzecxx.LexState;
import com.antlersoft.parser.RuleActionException;

/**
 * @author Michael A. MacDonald
 *
 */
class OctalEscape extends LexStateBase {
	StringBuilder m_sb;
	int m_digits;
	int m_value;

	/**
	 * @param parent
	 * @param reader
	 */
	public OctalEscape(LexState parent, IldasmReader reader, StringBuilder sb) {
		super(parent, reader);
		m_sb=sb;
		m_digits=0;
		m_value=0;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		if ( ! CharClass.isOctalDigit(c))
			throw new LexException( "Bad digit in Octal string escape");
		m_digits+=1;
		m_value*=8;
		m_value+=c-'0';
		if ( m_digits==3)
		{
			m_sb.append( (char)m_value);
			return m_parent;
		}
		return this;
	}

}
