package com.antlersoft.query;

import java.util.Enumeration;

public class MultiEnum implements Enumeration {
	private Enumeration m_base;
	private Enumeration m_current;
	public MultiEnum( Enumeration base)
	{
		m_base=base;
	}
    public boolean hasMoreElements() {
		while ( m_current==null || ! m_current.hasMoreElements())
		{
			if ( m_base.hasMoreElements())
			{
				m_current=(Enumeration)m_base.nextElement();
			}
			else
				return false;
		}
		return true;
    }
    public Object nextElement() {
		if ( ! hasMoreElements())
			throw new java.util.NoSuchElementException();
		return m_current.nextElement();
    }
}