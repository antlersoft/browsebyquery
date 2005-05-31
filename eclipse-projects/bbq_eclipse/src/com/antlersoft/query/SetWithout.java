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
