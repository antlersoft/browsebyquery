/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.analyzecxx.LexException;
import com.antlersoft.analyzecxx.LexState;
import com.antlersoft.parser.RuleActionException;

/**
 * Ignore characters until the end of line...
 * @author Michael A. MacDonald
 *
 */
class CommentState extends LexStateBase {

	/**
	 * @param parent
	 * @param reader
	 */
	public CommentState(LexState parent, IldasmReader reader) {
		super(parent, reader);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		if ( c=='\n')
			return m_parent;
		return this;
	}

}
