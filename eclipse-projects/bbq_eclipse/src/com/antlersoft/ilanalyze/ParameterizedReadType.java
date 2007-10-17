/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Michael A. MacDonald
 *
 */
public class ParameterizedReadType implements ReadType {
	
	private String m_name;
	
	/** Collection of types (ReadType) that parameterize this type */
	private Collection m_gen_args;
	
	/**
	 * 
	 * @param name Simple name of type that is parameterized
	 * @param gen_args Parameterizing types
	 */
	public ParameterizedReadType( String name, Collection gen_args)
	{
		m_name=name;
		m_gen_args=gen_args;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.ReadType#getGenericArgs()
	 */
	public Collection getGenericArgs() {
		return m_gen_args;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.ReadType#isFullySpecified()
	 */
	public boolean isFullySpecified() {
		boolean specified=true;
		
		for ( Iterator i=m_gen_args.iterator(); specified && i.hasNext(); )
		{
			specified=((ReadType)i.next()).isFullySpecified();
		}
		
		return specified;
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append( m_name);
		sb.append( '<');
		boolean first=true;
		for ( Iterator i=m_gen_args.iterator(); i.hasNext();)
		{
			if ( first)
				first=false;
			else
				sb.append(',');
			sb.append(i.next().toString());
		}
		sb.append('>');
		
		return sb.toString();
	}

}
