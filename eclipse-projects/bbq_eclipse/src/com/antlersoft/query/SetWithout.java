package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;

public class SetWithout extends SetOperator {
	public SetWithout( Comparator comp, Enumeration a, Enumeration b)
	{
		super( comp, a, b);
		m_candidate=null;
		m_first=true;
	}

	protected Object determineNext()
	{
		Object result=null;
		while ( result==null && nextPairInOrder())
		{
			if ( m_next_advance_a || m_first)
			{
				if ( m_candidate!=null)
				{
					result=m_candidate;
				}
				m_candidate=m_current_a;
			}
			if ( m_candidate!=null && m_current_b!=null && m_comp.compare( m_candidate, m_current_b)==0)
				m_candidate=null;
			m_first=false;
		}
		if ( m_candidate!=null && result==null)
		{
			result = m_candidate;
			m_candidate=null;
		}
		return result;
	}

	private Object m_candidate;
	private boolean m_first;
}
