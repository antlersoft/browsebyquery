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

public class Matches extends Filter implements CountPreservingValueContext {
	public Matches( String to_match)
	{
		m_to_match=to_match;
		m_binding=new BindImpl( Boolean.class, null);
	}
    public List getValueCollection() {
		return NO_SUBS;
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
		return m_result;
    }
    public void lateBindResult(Class new_result) throws com.antlersoft.query.BindException {
		m_binding.lateBindResult( new_result);
    }
    public ValueContext getContext() {
		return this;
    }
	public int getContextType() {
		return COUNT_PRESERVING;
	}
	public void inputObject( ValueObject obj, DataSource source, Object to_transform)
	{
		m_result=to_transform.toString().indexOf( m_to_match)!= -1;
	}

	private BindImpl m_binding;
	private String m_to_match;
	private boolean m_result;
}