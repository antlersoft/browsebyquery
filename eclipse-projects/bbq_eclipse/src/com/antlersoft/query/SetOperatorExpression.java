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