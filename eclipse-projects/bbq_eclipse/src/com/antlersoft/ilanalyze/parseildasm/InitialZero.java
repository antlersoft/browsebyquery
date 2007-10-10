/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * 0 encounted at the beginning of a token-- might be a hex int, might just be a zero
 * @author Michael A. MacDonald
 *
 */
class InitialZero extends LexStateBase {

	/**
	 * @param parent
	 * @param reader
	 */
	public InitialZero(LexState parent, IldasmReader reader) {
		super(parent, reader);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		if ( c=='x')
		{
			return new HexNumber( m_parent, m_reader);
		}
		return new NumberState( m_parent, m_reader).nextCharacter('0').nextCharacter(c);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.parseildasm.LexStateBase#endOfFile()
	 */
	public LexState endOfFile() throws IOException, RuleActionException, LexException {
		return new NumberState( m_parent, m_reader).nextCharacter('0').endOfFile();
	}

}
