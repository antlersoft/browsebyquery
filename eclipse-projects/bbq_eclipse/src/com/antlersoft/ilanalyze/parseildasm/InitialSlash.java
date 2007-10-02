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
class InitialSlash extends LexStateBase {

	/**
	 * @param parent
	 * @param reader
	 */
	public InitialSlash(LexState parent, IldasmReader reader) {
		super(parent, reader);
	}


	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		if ( c!='/')
		{
			m_reader.processToken( IldasmParser.t_slash, "/");
			return m_parent;
		}
		
		return new CommentState( m_parent, m_reader);
	}

}
