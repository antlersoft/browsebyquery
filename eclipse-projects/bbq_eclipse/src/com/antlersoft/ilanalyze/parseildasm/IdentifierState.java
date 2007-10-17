/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Lexing an idetifier or reserved word
 * @author Michael A. MacDonald
 *
 */
public class IdentifierState extends LexStateBase {
	private StringBuilder m_sb;

	/**
	 * @param parent
	 * @param reader
	 */
	public IdentifierState(LexState parent, IldasmReader reader) {
		super(parent, reader);
		m_sb=new StringBuilder();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.analyzecxx.LexState#nextCharacter(char)
	 */
	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		if ( c=='.' || CharClass.isIDPart(c))
		{
			m_sb.append(c);
			return this;
		}
		processToken();
		return m_parent.nextCharacter( c);
	}
	
	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.parseildasm.LexStateBase#endOfFile()
	 */
	public LexState endOfFile() throws IOException, RuleActionException, LexException {
		processToken();
		return m_parent.endOfFile();
	}

	/**
	 * Send an identifier to the parser
	 * @throws RuleActionException -- Parsing the identifier causes an error
	 */
	private void processToken() throws RuleActionException
	{
		String value=m_sb.toString();
		Symbol symbol=m_reader.expectedReserved(value);
		if ( symbol!=null)
			m_reader.processToken(symbol, value);
		else
		{
			Symbol id_symbol=IldasmParser.t_ID;
			if ( value.indexOf('.')!= -1 && m_reader.expectedReserved(IldasmParser.t_DOTTEDNAME.toString())!=null)
					id_symbol=IldasmParser.t_DOTTEDNAME;
			m_reader.processToken( id_symbol, value);
		}
	}
}
