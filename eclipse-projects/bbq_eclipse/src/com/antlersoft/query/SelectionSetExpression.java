/**
 * Copyright (c) 2011 Michael A. MacDonald
 */
package com.antlersoft.query;

import java.util.ArrayList;
import java.util.Enumeration;

import com.antlersoft.util.IteratorEnumeration;

/**
 * A SetExpression representing an arbitrary set of objects that were selected
 * @author Michael A. MacDonald
 *
 */
public class SelectionSetExpression extends SetExpression {
	private Class<?> m_resultClass;
	private ArrayList<Object> m_contents;
	
	public SelectionSetExpression(Class<?> resultClass)
	{
		m_resultClass = resultClass;
		m_contents = new ArrayList<Object>();
	}
	
	public SelectionSetExpression()
	{
		this(null);
	}
	
	public void clear()
	{
		m_contents.clear();
	}
	
	public void add(Object o)
	{
		Class<?> oclass = o.getClass();
		if (m_resultClass == null)
		{
			m_resultClass = oclass;
		}
		else
		{
			if (! m_resultClass.isAssignableFrom(oclass))
			{
				if (m_contents.size() == 0 || oclass.isAssignableFrom(m_resultClass))
				{
					m_resultClass = oclass;
				}
				else
				{
					m_resultClass = Object.class;
				}
			}
		}
		m_contents.add(o);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.SetExpression#evaluate(com.antlersoft.query.DataSource)
	 */
	@Override
	public Enumeration<?> evaluate(DataSource source) {
		return new IteratorEnumeration(m_contents.iterator());
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.SetExpression#getResultClass()
	 */
	@Override
	public Class<?> getResultClass() {
		if (m_resultClass == null)
			return Object.class;
		return m_resultClass;
	}

}
