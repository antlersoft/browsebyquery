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

public class TransformTransform extends Transform {

	public TransformTransform( Transform primary, Transform secondary)
	throws BindException
	{
		m_binding=new ResolveCompoundBinding(primary, secondary);
		m_primary=primary;
		m_secondary=secondary;
	}
    public Enumeration finishEvaluation(DataSource source) {
		TransformSet.BaseAdapter base=new TransformSet.BaseAdapter(
				  source, m_secondary, m_primary.finishEvaluation( source));

		CombineEnum result=new CombineEnum(
				  new MultiEnum( base), null);
		base.m_combined=result;
		return result;
    }
    public void lateBindApplies(Class new_applies) throws BindException {
		m_binding.lateBindApplies( new_applies);
    }
    public void startEvaluation(DataSource source) {
		m_primary.startEvaluation( source);
		m_secondary.startEvaluation( source);
    }
    public Class resultClass() {
		return m_binding.resultClass();
    }
    public Class appliesClass() {
		return m_binding.appliesClass();
    }
    public void lateBindResult(Class new_result) throws BindException {
		m_binding.lateBindResult(new_result);
    }
    public Enumeration transformObject(DataSource source, Object to_transform) {
		return new MultiEnum(
				  new TransformSet.BaseAdapter(
						source, m_secondary, m_primary.finishEvaluation( source)
						));
    }

	public boolean isAbleToUseMore()
	{
		return m_primary.isAbleToUseMore() && m_secondary.isAbleToUseMore();
	}

	public Comparator getOrdering()
	{
		return m_secondary.getOrdering();
	}

	public void bindOrdering( Comparator comp)
	{
		m_primary.bindOrdering( comp);
		m_secondary.bindOrdering( m_primary.getOrdering());
	}

	private ResolveCompoundBinding m_binding;
	private Transform m_primary, m_secondary;
}