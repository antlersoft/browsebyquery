/**
 * Copyright (c) 2007 Michael A. MacDonald
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
public class PermissionContentState extends LexStateBase {
	private int braceCount;

	/**
	 * @param parent
	 * @param reader
	 */
	public PermissionContentState(LexState parent, IldasmReader reader) {
		super(parent, reader);
		braceCount=0;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.parser.lex.LexState#endOfFile()
	 */
	public LexState endOfFile() throws IOException, RuleActionException,
			LexException {
		return m_parent.endOfFile();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.parser.lex.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		LexState result=this;
		if ( c=='}')
		{
			if ( braceCount>0)
				--braceCount;
			else
			{
				m_reader.processToken(IldasmParser.t_permissionContentsDirective, "");
				result=new PunctuationState(m_parent, m_reader).nextCharacter(c);
			}
		}
		else if ( c=='{')
		{
			++braceCount;
		}
		return result;
	}

}
