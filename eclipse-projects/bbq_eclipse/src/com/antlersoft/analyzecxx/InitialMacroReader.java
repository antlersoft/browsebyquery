package com.antlersoft.analyzecxx;

import java.util.ArrayList;

import com.antlersoft.parser.RuleActionException;

public class InitialMacroReader implements LexReader {
	private ArrayList m_list=new ArrayList();
    public void processToken(LexToken next_token) throws com.antlersoft.parser.RuleActionException, LexException {
		m_list.add( next_token);
    }

	public void noMoreTokens() {}

	ArrayList getTokens()
	{
		return m_list;
	}
}