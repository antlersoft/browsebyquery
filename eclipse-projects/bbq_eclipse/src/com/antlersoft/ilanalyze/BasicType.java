/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.Collection;

/**
 * A simple type
 * @author Michael A. MacDonald
 *
 */
public class BasicType implements ReadType {
	String m_name;
	
	public BasicType( String name)
	{
		m_name=name;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.ReadType#getGenericArgs()
	 */
	public Collection getGenericArgs() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.ReadType#isFullySpecified()
	 */
	public boolean isFullySpecified() {
		return true;
	}
	
	public String toString()
	{
		return m_name;
	}

}
