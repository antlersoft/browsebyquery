package com.antlersoft.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import com.antlersoft.util.IteratorEnumeration;

public class SetOperatorTransform extends Transform {
	public SetOperatorTransform( int operator, Transform a, Transform b)
	throws BindException
	{
		m_operator=operator;
		m_a=a;
		m_b=b;
		m_binding=new ResolvePairBinding( a, b);
	}
    public Enumeration finishEvaluation(DataSource source) {
		if ( m_common_ordering!=null)
		{
			return SetOperator.SetOperatorFactory( m_operator,
				m_common_ordering, m_a.finishEvaluation( source),
				m_b.finishEvaluation( source));
		}
		Collections.sort( m_a_list);
		Collections.sort( m_b_list);
		return SetOperator.SetOperatorFactory( m_operator, null,
		new IteratorEnumeration( m_a_list.iterator()),
		new IteratorEnumeration( m_b_list.iterator())
		);
    }
    public void lateBindApplies(Class parm1) throws com.antlersoft.query.BindException {
		m_binding.lateBindApplies( parm1);
    }
    public void startEvaluation(DataSource source) {

		m_a.startEvaluation( source);
		m_b.startEvaluation( source);

		resolveOrdering();

		if ( m_common_ordering==null)
		{
			m_a_list = new ArrayList();
			m_b_list = new ArrayList();
		}
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
			return SetOperator.SetOperatorFactory( m_operator, m_common_ordering,
				m_a.transformObject( source, to_transform),
				m_b.transformObject( source, to_transform));
		}
		SetOperatorExpression.collectionFromEnumeration( m_a_list,
			m_a.transformObject( source, to_transform));
		SetOperatorExpression.collectionFromEnumeration( m_b_list,
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
	private int m_operator;
	private ResolvePairBinding m_binding;
	private ArrayList m_a_list;
	private ArrayList m_b_list;
}