package com.antlersoft.analyzecxx;

import java.util.ArrayList;

/**
 * An object-style preprocessor macro
 */
class ObjectMacro extends Macro
{
	private static final ArrayList m_empty_list=new ArrayList();

	ArrayList m_token_list;

	ObjectMacro( String identifier, ArrayList token_list)
	{
		super( identifier);
		m_token_list=token_list;
	}

	ObjectMacro( String identifier, LexToken token)
	{
		super( identifier);
		m_token_list=new ArrayList(1);
		m_token_list.add( token);
	}

	ObjectMacro( String identifier)
	{
		this( identifier, m_empty_list);
	}

	ArrayList getTokens()
	{
		return m_token_list;
	}
}