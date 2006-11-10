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

public class SetOperatorTransform extends Transform {
	public SetOperatorTransform( int operator, Transform a, Transform b)
	throws BindException
	{
		m_operator_type=operator;
		m_a=a;
		m_b=b;
		m_binding=new ResolvePairBinding( a, b);
	}
    public Enumeration finishEvaluation(DataSource source) {
		if ( m_common_ordering!=null)
		{
			return m_operator.getSortedEnum(
				m_common_ordering, m_a.finishEvaluation( source),
				m_b.finishEvaluation( source));
		}
		m_operator.processEnumerationFromA(
				m_a.finishEvaluation( source));
		m_operator.processEnumerationFromB(
			m_b.finishEvaluation( source));
		
		return m_operator.getUnsortedEnumeration();
    }
    public void lateBindApplies(Class parm1) throws com.antlersoft.query.BindException {
		m_binding.lateBindApplies( parm1);
    }
    public void startEvaluation(DataSource source) {
    	
    	m_operator=SetOperator.SetOperatorFactory( m_operator_type);

		m_a.startEvaluation( source);
		m_b.startEvaluation( source);

		resolveOrdering();
    }
    public Class resultClass() {
		return m_binding.resultClass();
    }
    public Class appliesClass() {
		return m_binding.appliesClass();
    }
    public void lateBindResult(Class new_result) throws com.antlersoft.query.BindException {
		m_binding.lateBindResult( new_result);
    }
    public Enumeration transformObject(DataSource source, Object to_transform) {
		if ( m_common_ordering!=null)
		{
			return m_operator.getSortedEnum( m_common_ordering,
				m_a.transformObject( source, to_transform),
				m_b.transformObject( source, to_transform));
		}
		m_operator.processEnumerationFromA(
			m_a.transformObject( source, to_transform));
		m_operator.processEnumerationFromB(
			m_b.transformObject( source, to_transform));
		return EmptyEnum.empty;
    }

	public boolean isAbleToUseMore()
	{
		return m_a.isAbleToUseMore() || m_b.isAbleToUseMore();
	}

	public void bindOrdering( Comparator comp)
	{
		m_a.bindOrdering( comp);
		m_b.bindOrdering( comp);
	}

	public Comparator getOrdering()
	{
		resolveOrdering();
		return m_common_ordering;
	}

	private void resolveOrdering()
	{
		m_common_ordering=null;
		Comparator a=m_a.getOrdering();
		Comparator b=m_b.getOrdering();
		if ( a!=null && b!=null && a.equals( b))
			m_common_ordering=a;
	}

	private Transform m_a, m_b;
	private Comparator m_common_ordering;
	private int m_operator_type;
	private SetOperator m_operator;
	private ResolvePairBinding m_binding;
}