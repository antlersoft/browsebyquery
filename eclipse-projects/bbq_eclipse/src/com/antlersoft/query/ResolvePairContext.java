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
import java.util.List;

public class ResolvePairContext implements ScalarValueContext {
	public ResolvePairContext( ValueObject a, ValueObject b)
	throws BindException
	{
		m_context_type=a.getContext().getContextType();
		int b_type=b.getContext().getContextType();
		if ( b_type!=m_context_type)
		{
			if ( b_type!=SCALAR)
			{
				if ( m_context_type==SCALAR)
					m_context_type=b_type;
				else
					throw new BindException( "Incompatible context types in value expression.");
			}
		}
		m_objs=new ArrayList(2);
		m_objs.add( a);
		m_objs.add( b);
		m_a=a;
		m_a_context=a.getContext();
		m_b=b;
		m_b_context=b.getContext();
	}
    public void startGroup(ValueObject vobj, DataSource source) {
		((GroupValueContext)m_a_context).startGroup( m_a, source);
		((GroupValueContext)m_b_context).startGroup( m_b, source);
    }
    public boolean addObject(ValueObject vobj, DataSource source, Object to_add) {
		return ((GroupValueContext)m_a_context).addObject( m_a, source, to_add) ||
		((GroupValueContext)m_b_context).addObject( m_b, source, to_add);
    }
    public void finishGroup(ValueObject vobj, DataSource source) {
		((GroupValueContext)m_a_context).finishGroup( m_a, source);
		((GroupValueContext)m_b_context).finishGroup( m_b, source);
    }
    public void inputObject(ValueObject value, DataSource source, Object input) {
		((CountPreservingValueContext)m_a_context).inputObject( m_a,
			source, input);
		((CountPreservingValueContext)m_b_context).inputObject( m_b,
			source, input);
    }
    public int getContextType() {
		return m_context_type;
    }
	public List getValueCollection()
	{
		return m_objs;
	}

	private ArrayList m_objs;
	private ValueContext m_a_context;
	private ValueContext m_b_context;
	private ValueObject m_a;
	private ValueObject m_b;
	private int m_context_type;
}