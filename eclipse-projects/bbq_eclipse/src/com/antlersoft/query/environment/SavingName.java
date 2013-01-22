/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query.environment;

/**
 * A name
 * @author Michael A. MacDonald
 *
 */
public class SavingName {
	String m_name;
	int m_token_count;
	
	SavingName( String name, int token_count)
	{
		m_name=name;
		m_token_count=token_count;
	}
}
