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

import java.util.List;

public class LogicalOpFilter extends Filter {
	public final static int AND=1;
	public final static int OR=2;
	public final static int XOR=3;
	public LogicalOpFilter( int type, Filter a , Filter b)
	throws BindException
	{
		m_binding=new ResolvePairBinding( a, b);
		m_context=new ResolvePairContext( a, b);
		m_a=a;
		m_b=b;
		m_type=type;
	}

    public List getValueCollection() {
		return m_context.getValueCollection();
    }
    public void lateBindApplies(Class new_applies) throws com.antlersoft.query.BindException {
		m_binding.lateBindApplies( new_applies);
    }
    public Class resultClass() {
		return m_binding.resultClass();
    }
    public Class appliesClass() {
		return m_binding.appliesClass();
    }
    public boolean booleanValue() {
		boolean result=false;
		switch ( m_type)
		{
			case AND : result=m_a.adjustedBooleanValue() && m_b.adjustedBooleanValue();
							break;
			case OR : result=m_a.adjustedBooleanValue() || m_b.adjustedBooleanValue();
				break;
			case XOR : result=( m_a.adjustedBooleanValue() || m_b.adjustedBooleanValue())
								&& ! ( m_a.adjustedBooleanValue() && m_b.adjustedBooleanValue());
							break;
			default :
			throw new IllegalArgumentException();
		}
		return result;
    }
    public void lateBindResult(Class new_result) throws com.antlersoft.query.BindException {
		m_binding.lateBindResult( new_result);
    }
    public ValueContext getContext() {
		return m_context;
    }

	private ResolvePairBinding m_binding;
	private ResolvePairContext m_context;
	private int m_type;
	private Filter m_a, m_b;
}