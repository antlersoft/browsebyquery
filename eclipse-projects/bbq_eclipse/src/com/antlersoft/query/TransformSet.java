/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;

public class TransformSet extends SetExpression {
	Transform m_transform;
	SetExpression m_set;

	public TransformSet( Transform transform, SetExpression base)
	throws BindException
	{
		transform.lateBindApplies( base.getResultClass());
		transform.bindOrdering( base.getOrdering());
		m_transform=transform;
		m_set=base;
	}
    public Enumeration evaluate( final DataSource source) {
		BaseAdapter base=new BaseAdapter( source, m_transform,
										  m_set.evaluate( source));
		CombineEnum result=new CombineEnum( new MultiEnum( base), null);
		base.m_combined=result;
		m_transform.startEvaluation( source);
		return result;
    }
    public Class getResultClass() {
		return m_transform.resultClass();
    }
	public Comparator getOrdering()
	{
		return m_transform.getOrdering();
	}

	static class BaseAdapter implements Enumeration
	{
		private DataSource m_source;
		private Transform m_transform;
		private Enumeration m_underlying;
		private boolean m_has_more;
		CombineEnum m_combined;

		BaseAdapter( DataSource source, Transform transform, Enumeration base)
		{
			m_source=source;
			m_transform=transform;
			m_underlying=base;
			m_has_more=true;
			m_combined=null;
		}

		public boolean hasMoreElements()
		{
			if ( m_has_more)
			{
				m_has_more=m_transform.isAbleToUseMore();
				if ( m_has_more)
					m_has_more=m_underlying.hasMoreElements();
				if ( ! m_has_more && m_combined!=null)
					m_combined.m_second=m_transform.finishEvaluation( m_source);
			}
			return m_has_more;
		}

		public Object nextElement()
		{
			return m_transform.transformObject( m_source, m_underlying.nextElement());
		}
	}
}