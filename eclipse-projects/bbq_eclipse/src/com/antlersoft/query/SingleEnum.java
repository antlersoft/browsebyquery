package com.antlersoft.query;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class SingleEnum implements Enumeration {
	public SingleEnum( Object obj)
	{
		m_obj=obj;
		m_used=false;
	}
    public boolean hasMoreElements() {
		return ! m_used;
    }
    public Object nextElement() {
		if ( m_used)
			throw new NoSuchElementException();
		m_used=true;
		return m_obj;
    }
	private Object m_obj;
	private boolean m_used;
}