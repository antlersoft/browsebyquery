/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Handles the character right after a string escape
 * @author Michael A. MacDonald
 *
 */
class EscapeInStringState extends LexStateBase {
	private StringBuilder m_sb;

	/**
	 * @param parent
	 * @param reader
	 */
	public EscapeInStringState(LexState parent, IldasmReader reader, StringBuilder sb) {
		super(parent, reader);
		m_sb=sb;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		if ( CharClass.isOctalDigit(c))
			return new OctalEscape( m_parent, m_reader, m_sb).nextCharacter( c);
		if ( CharClass.isWhiteSpace(c))
			return new EndOfLineEscape( m_parent, m_reader).nextCharacter( c);
		if ( c=='n')
		{
			m_sb.append( '\n');
			return m_parent;
		}
		if ( c=='t')
		{
			m_sb.append('\t');
			return m_parent;
		}
		if ( c=='\\')
		{
			m_sb.append('\\');
			return m_parent;
		}
		if ( c=='\'' || c=='"')
		{
			m_sb.append(c);
			return m_parent;
		}
		if ( c=='r')
		{
			m_sb.append('\r');
			return m_parent;
		}
		if ( c=='?')
		{
			m_sb.append('?');
			return m_parent;
		}
		StringBuilder excep_builder=new StringBuilder( "bad escaped character in string: \"");
		excep_builder.append( m_sb.toString());
		excep_builder.append( "\":");
		excep_builder.append( (int)c);
		throw new LexException( excep_builder.toString());
	}

}
