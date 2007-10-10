package com.antlersoft.ilanalyze.parseildasm;

import java.io.IOException;

import com.antlersoft.parser.RuleActionException;
import com.antlersoft.parser.lex.LexException;
import com.antlersoft.parser.lex.LexState;

/**
 * Default state (not within any known tokens) while lexing il file.
 * @author Michael A. MacDonald
 *
 */
class InitialState implements LexState {
	private IldasmReader m_reader;
	InitialState( IldasmReader reader)
	{
		m_reader=reader;
	}

	public LexState endOfFile() throws IOException, RuleActionException,
			LexException {
		return null;
	}

	public LexState nextCharacter(char c) throws IOException,
			RuleActionException, LexException {
		LexState result=null;
		switch ( c)
		{
		case '\'' :
		case '"' :
			result=new QuoteString( this, m_reader, c);
			break;
		case '/' :
			result=new InitialSlash( this, m_reader);
			break;
		case '.' :
			result=new InitialPeriod( this, m_reader);
			break;
		case '-' :
			result=new InitialMinus( this, m_reader);
			break;
		default :
			if ( CharClass.isWhiteSpace(c) || c=='\032' || c=='\000')
				result=this;
			else if ( CharClass.isHexDigit(c) && m_reader.expectedReserved(IldasmParser.t_HEXBYTE.toString())!=null)
				result=new HexByteState( this, m_reader, c);
			else if ( c=='0')
				result=new InitialZero( this, m_reader);
			else if ( CharClass.isDigit(c))
			{
				result=new NumberState( this, m_reader);
				result=result.nextCharacter(c);
			}
			else if ( CharClass.isIDStart(c))
			{
				result=new IdentifierState( this, m_reader);
				result=result.nextCharacter(c);
			}
			else
			{
				result=new PunctuationState( this, m_reader);
				result=result.nextCharacter(c);
			}
		}
		return result;
	}

}
