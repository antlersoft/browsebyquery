package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;

public class SetUnion extends SetOperator {
	public SetUnion( Comparator comp, Enumeration a, Enumeration b)
	{
		super( comp, a, b);
	}

	protected Object determineNext()
	{
		Object result=null;
		while ( nextPairInOrder())
		{
			if ( m_next_advance_a)
				result=m_current_a;
			else
				result=m_current_b;
		}
		return result;
	}
}