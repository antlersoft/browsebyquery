/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query;

import java.util.List;

import java.util.regex.Pattern;

/**
 * Implement the reg expression matching operator in a value context
 * (~=)
 * @author Michael A. MacDonald
 *
 */
public class ValueRegExpMatch extends Filter {

	/**
	 * 
	 */
	public ValueRegExpMatch( ValueExpression a, ValueExpression pattern_value)
	throws BindException
	{
		m_a=a;
		m_pattern_value=pattern_value;
		m_context=new ResolvePairContext( m_a, m_pattern_value);
		m_binding=new ResolveAppliesBinding( m_a, pattern_value);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Filter#booleanValue()
	 */
	public boolean booleanValue() {
		String next_pattern=m_pattern_value.getValue().toString();
		if ( m_pattern==null || ! m_pattern.pattern().equals( next_pattern))
		{
			m_pattern=Pattern.compile( next_pattern);
		}
		return m_pattern.matcher(m_a.getValue().toString()).find();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueObject#getContext()
	 */
	public ValueContext getContext() {
		return m_context;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.ValueObject#getValueCollection()
	 */
	public List getValueCollection() {
		return m_context.getValueCollection();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#appliesClass()
	 */
	public Class appliesClass() {
		return m_binding.appliesClass();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindApplies(java.lang.Class)
	 */
    public void lateBindApplies(Class new_applies) throws BindException {
    	m_binding.lateBindApplies( new_applies);
    }
	
	private ValueExpression m_a, m_pattern_value;
	private ResolvePairContext m_context;
	private Pattern m_pattern;
	private ResolveAppliesBinding m_binding;
	
	/**
	 * Resolves only the applies side of two bindables that will be used in the same context.
	 * @author Michael A. MacDonald
	 *
	 */
	static class ResolveAppliesBinding
	{
		ResolveAppliesBinding( Bindable a, Bindable b)
		throws BindException
		{
			m_a=a;
			m_b=b;
			resolveBindingApplies();
		}
		void lateBindApplies(Class new_applies) throws BindException {
			m_a.lateBindApplies( new_applies);
			m_b.lateBindApplies( new_applies);
			resolveBindingApplies();
	    }
		
		Class appliesClass() {
			return m_applies;
		}

		private void resolveBindingApplies()
		throws BindException
		{
			Class appliesA=m_a.appliesClass();
			Class appliesB=m_b.appliesClass();
			if ( appliesA==appliesB)
			{
				m_applies=appliesA;
			}
			else
			{
				if ( appliesB==null)
				{
					m_b.lateBindApplies( appliesA);
					appliesB=m_b.appliesClass();
				}
				if ( appliesA==null)
				{
					m_a.lateBindApplies( appliesB);
					appliesA=m_a.appliesClass();
				}
				if ( appliesA==appliesB)
				{
					m_applies=appliesA;
				}
				else
				{
					m_applies=ResolvePairBinding.commonSubType( appliesA, appliesB, false);
				}
			}
		}
		private Bindable m_a, m_b;
		private Class m_applies;
	}
}
