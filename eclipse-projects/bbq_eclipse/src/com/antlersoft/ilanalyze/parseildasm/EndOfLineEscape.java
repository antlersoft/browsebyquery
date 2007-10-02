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
class EndOfLineEscape extends LexStateBase {

	private boolean m_seen_nl;
	/**
	 * @param parent
	 * @param reader
	 */
	public EndOfLineEscape(LexState parent, IldasmReader reader) {
		super(parent, reader);
		m_seen_nl=false;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		if ( m_seen_nl)
		{
			if ( ! CharClass.isWhiteSpace( c))
				return m_parent.nextCharacter( c);
		}
		if ( c=='\n')
			m_seen_nl=true;
		else if ( ! CharClass.isWhiteSpace( c))
			throw new LexException( "Unexpected character after end-of-line escape in quote");
		return this;
	}
}
