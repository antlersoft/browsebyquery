package com.antlersoft.analyzecxx;

import com.antlersoft.parser.RuleActionException;
import java.io.IOException;

public class QuotedLiteral implements LexState
{
	private CxxReader m_reader;
	private LexState m_caller;
	private char m_closing_quote;
	private boolean m_is_escaped;
	private LexToken m_token_to_send;
	private StringBuffer m_buffer;
	private boolean m_wide;

	QuotedLiteral( CxxReader reader, LexState caller, char closing_quote,
	LexToken token_to_send, boolean wide)
	{
		m_reader=reader;
		m_caller=caller;
		m_closing_quote=closing_quote;
		m_token_to_send=token_to_send;
		m_is_escaped=false;
		m_buffer=new StringBuffer();
	}
    public LexState nextCharacter(char c) throws IOException, RuleActionException, LexException {
		LexState result;
		if ( c==m_closing_quote && ! m_is_escaped)
		{
			m_token_to_send.value=m_buffer.toString();
			m_reader.m_lex_to_preprocess.processToken( m_token_to_send);
			result=m_caller;
		}
		else if ( c=='\n')
		{
			throw new LexException( "new-line in literal");
		}
		else
		{
			result=this;
			if ( m_is_escaped)
				m_is_escaped=false;
			else if ( c=='\\')
				m_is_escaped=true;
			m_buffer.
		}
		return
    }
    public LexState endOfFile() throws IOException, RuleActionException, LexException {
	/**@todo Implement this com.antlersoft.analyzecxx.LexState method*/
	throw new java.lang.UnsupportedOperationException("Method endOfFile() not yet implemented.");
    }
}