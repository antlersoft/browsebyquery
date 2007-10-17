/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.Collection;

/**
 * @author Michael A. MacDonald
 *
 */
public class ReadArray implements ReadType {
	
	private ReadType m_underlying;
	private int m_dimensions;
	
	public ReadArray( ReadType underlying, int dimensions)
	{
		m_underlying=underlying;
		m_dimensions=dimensions;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.ReadType#getGenericArgs()
	 */
	public Collection getGenericArgs() {
		return m_underlying.getGenericArgs();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.ReadType#isFullySpecified()
	 */
	public boolean isFullySpecified() {
		return m_underlying.isFullySpecified();
	}
	
	public int getDimensions()
	{
		return m_dimensions;
	}
	
	public ReadType getUnderlying()
	{
		return m_underlying;
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder( m_underlying.toString());
		
		sb.append("[");
		for ( int i=0; i<m_dimensions; ++i)
		{
			if ( i!=0)
				sb.append(",");
			sb.append( "...");
		}
		sb.append("]");
		
		return sb.toString();
	}

}
