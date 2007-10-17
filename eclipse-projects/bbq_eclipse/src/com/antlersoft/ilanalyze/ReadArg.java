/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

/**
 * An argument to a method; a type, possibly a name, or neither (representing a an ellipsis,
 * multiple args
 * @author Michael A. MacDonald
 *
 */
public class ReadArg {
	
	private ReadType m_type;
	private String m_name;
	
	/**
	 * Declare an ellipsis
	 *
	 */
	public ReadArg() {}
	
	public ReadArg( ReadType type)
	{
		m_type=type;
	}
	
	public ReadArg( ReadType type, String name)
	{
		m_type=type;;
		m_name=name;
	}
}




