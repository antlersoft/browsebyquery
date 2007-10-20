/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.List;

/**
 * Part of the parameter list of a generic type; contains a name, a list of constraints,
 * and a properties flag which is a set of bits defined by constants.
 * @author Michael A. MacDonald
 *
 */
public class GenericTypeParameter {
	public final static int IS_COVARIANT=1;
	public final static int IS_CONTRAVARIANT=2;
	
	/** List of ReadType objects representing type constraints on the parameter */
	private List m_constraints;
	
	/** Name in parameter list (we probably don't need this) */
	private String m_name;
	
	/** Bitwise OR of property bits defined above */
	private int m_properties;
	
	public GenericTypeParameter( String name, List constraints, int properties)
	{
		m_name=name;
		m_constraints=constraints;
		m_properties=properties;
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append( m_name);
		sb.append(' ');
		sb.append( m_properties);
		
		if ( m_constraints!=null && m_constraints.size()>0)
		{
			sb.append('(');
			LoggingDBDriver.formatList( sb, m_constraints);
			sb.append(')');
		}
		return sb.toString();
	}
}
