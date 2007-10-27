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
	
	public ReadType getType()
	{
		return m_type;
	}
	
	public String getName()
	{
		return m_name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if ( m_type==null)
			return "...";
		StringBuilder sb=new StringBuilder();
		sb.append( m_type.toString());
		if ( m_name!=null)
		{
			sb.append(' ');
			sb.append( m_name);
		}
		return sb.toString();
	}
}




