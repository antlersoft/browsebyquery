package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;

public class SetIntersection extends SetOperator {
	public SetIntersection( Comparator comp, Enumeration a, Enumeration b)
	{
		super( comp, a, b);
	}

	protected Object determineNext()
	{
		Object result=null;
		while ( nextPairInOrder())
		{
			if ( m_comp.compare( m_current_a, m_current_b)==0)
		    {
				result=m_current_a;
				break;
			}
		}
		return result;
	}
}
