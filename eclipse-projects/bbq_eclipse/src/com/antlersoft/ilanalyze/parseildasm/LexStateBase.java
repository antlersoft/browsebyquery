/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.analyzecxx.LexException;
import com.antlersoft.analyzecxx.LexState;
import com.antlersoft.parser.RuleActionException;

/**
 * Base class with common elements for IL lexing states
 * @author Michael A. MacDonald
 *
 */
abstract class LexStateBase implements LexState {
	protected IldasmReader m_reader;
	protected LexState m_parent;
	
	LexStateBase(LexState parent, IldasmReader reader)
	{
		m_parent=parent;
		m_reader=reader;
	}
	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#endOfFile()
	 */
	public LexState endOfFile() throws IOException, RuleActionException,
			LexException {
		return m_parent.endOfFile();
	}
}
