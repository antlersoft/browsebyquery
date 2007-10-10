/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Lexer has found a token that starts with a period; this may be just a period,
 * or it may be the start of a directive (or a float?)
 * @author Michael A. MacDonald
 *
 */
class InitialPeriod extends LexStateBase {

	/**
	 * @param parent
	 * @param reader
	 */
	public InitialPeriod(LexState parent, IldasmReader reader) {
		super(parent, reader);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		LexState result;
		if ( CharClass.isIDStart(c))
		{
			result=new IdentifierState( m_parent, m_reader);
			result=result.nextCharacter('.');
			result=result.nextCharacter(c);
		}
		else if ( CharClass.isDigit(c))
		{
			result=new FloatState(m_parent, m_reader, "0.");
			result=result.nextCharacter(c);
		}
		else
		{
			result=new PunctuationState( m_parent, m_reader);
			result=result.nextCharacter('.');
			result=result.nextCharacter(c);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#endOfFile()
	 */
	public LexState endOfFile() throws IOException, RuleActionException,
			LexException {
		m_reader.processToken( IldasmParser.t_period, ".");
		return m_parent.endOfFile();
	}
}
