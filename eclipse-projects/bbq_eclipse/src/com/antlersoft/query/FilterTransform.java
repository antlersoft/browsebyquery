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

public class FilterTransform extends Transform {
	public FilterTransform( Filter filter)
	throws BindException
    {
		if ( filter.getContext().getContextType()==ValueContext.GROUP)
			throw new BindException( "Improper filter expression context for filter transform");
		m_filter=filter;
	}
    public Enumeration finishEvaluation(DataSource source) {
		return EmptyEnum.empty;
    }
    public void lateBindApplies(Class new_applies) throws com.antlersoft.query.BindException {
		m_filter.lateBindApplies( new_applies);
    }
    public void startEvaluation(DataSource source) {
    }
    public Class resultClass() {
		return m_filter.appliesClass();
    }
    public Class appliesClass() {
		return m_filter.appliesClass();
    }
    public void lateBindResult(Class new_result) throws com.antlersoft.query.BindException {
		Class oldApplies=m_filter.appliesClass();
		if ( oldApplies==null)
		{
			m_filter.lateBindApplies( new_result);
		}
		else if ( oldApplies!=new_result && ! new_result.isAssignableFrom( oldApplies))
			throw new BindException( "Binding unfilterable type to filter transform");
    }
    public Enumeration transformObject(DataSource source, Object to_transform) {
		((CountPreservingValueContext)m_filter.getContext()).inputObject(
				  m_filter, source, to_transform);
		return m_filter.adjustedBooleanValue() ?
			(Enumeration)new SingleEnum( to_transform) :
			EmptyEnum.empty;
    }
	public void bindOrdering( Comparator ordering)
	{
		m_ordering=ordering;
	}
	public Comparator getOrdering()
	{
		return m_ordering;
	}

	private Comparator m_ordering;
	private Filter m_filter;
}