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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import com.antlersoft.util.IteratorEnumeration;

public class SetOperatorExpression extends SetExpression {
	public SetOperatorExpression( int operator, SetExpression a,
								  SetExpression b)
	throws BindException
	{
		m_result=a.getResultClass();
		Class other_result=b.getResultClass();
		if ( ! m_result.isAssignableFrom( other_result))
			if ( other_result.isAssignableFrom( m_result))
				m_result=other_result;
			else
				throw new BindException( m_result.getName()+" can't be in set operator expression with "+
		other_result.getName());
		m_use_ordering=( a.getOrdering()!=null && b.getOrdering()!=null &&
			a.getOrdering().equals( b.getOrdering()));
		m_a=a;
		m_b=b;
		m_operator=operator;
	}
    public Enumeration evaluate(DataSource source) {
		if ( m_use_ordering)
			return SetOperator.SetOperatorFactory( m_operator, m_a.getOrdering(),
				m_a.evaluate( source), m_b.evaluate( source));

		ArrayList a_list=collectionFromEnumeration( m_a.evaluate( source));
		Collections.sort( a_list);
		ArrayList b_list=collectionFromEnumeration( m_b.evaluate( source));
		Collections.sort( b_list);
		return SetOperator.SetOperatorFactory( m_operator, null,
		new IteratorEnumeration( a_list.iterator()),
		new IteratorEnumeration( b_list.iterator()));
	}
    public Class getResultClass() {
		return m_result;
    }

	public static ArrayList collectionFromEnumeration( ArrayList result, Enumeration e)
	{
		while ( e.hasMoreElements())
			result.add( e.nextElement());
		return result;
	}

	public static ArrayList collectionFromEnumeration( Enumeration e)
	{
		return collectionFromEnumeration( new ArrayList(), e);
	}

	Class m_result;
	int m_operator;
	boolean m_use_ordering;
	SetExpression m_a;
	SetExpression m_b;
}