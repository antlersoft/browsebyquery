package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;

public class SetDeintersection extends SetOperator {
	public SetDeintersection( Comparator comp, Enumeration a, Enumeration b)
	{
		super( comp, a, b);
		m_candidate_a=null;
		m_candidate_b=null;
		m_first=true;
	}

	protected Object determineNext()
	{
		Object result=null;
		while ( result==null && nextPairInOrder())
		{
			if ( m_next_advance_a || m_first)
			{
				if ( m_candidate_a!=null)
				{
					result=m_candidate_a;
				}
				m_candidate_a=m_current_a;
			}
			if ( ! m_next_advance_a || m_first)
			{
				if ( m_candidate_b!=null)
				{
					result=m_candidate_b;
				}
				m_candidate_b=m_current_b;
			}
			if ( m_candidate_a!=null && m_current_b!=null && m_comp.compare( m_candidate_a, m_current_b)==0)
				m_candidate_a=null;
			if ( m_candidate_b!=null && m_current_a!=null && m_comp.compare( m_candidate_b, m_current_a)==0)
				m_candidate_b=null;
			m_first=false;
		}
		if ( result==null)
		{
			if ( m_candidate_a!=null)
			{
				if ( m_candidate_b!=null)
				{
					if ( m_comp.compare( m_candidate_a, m_candidate_b)<0)
					{
						result=m_candidate_a;
						m_candidate_a=null;
					}
					else
					{
						result=m_candidate_b;
						m_candidate_b=null;
					}
				}
				else
				{
					result=m_candidate_a;
					m_candidate_a=null;
				}
			}
			else
			{
				result=m_candidate_b;
				m_candidate_b=null;
			}
		}
		return result;
	}

	private Object m_candidate_a, m_candidate_b;
	private boolean m_first;
}
