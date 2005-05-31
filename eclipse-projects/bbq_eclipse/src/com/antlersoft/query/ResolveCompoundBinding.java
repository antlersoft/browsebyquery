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

public class ResolveCompoundBinding implements Bindable {
	public ResolveCompoundBinding( Bindable primary, Bindable secondary)
	throws BindException
	{
		m_primary=primary;
		m_secondary=secondary;
		if ( m_primary.resultClass()!=null)
		{
			m_secondary.lateBindApplies( m_primary.resultClass());
		}
		else if ( m_secondary.appliesClass()!=null)
		{
			m_primary.lateBindResult( m_secondary.appliesClass());
		}
	}
    public Class resultClass() {
		return m_secondary.resultClass();
    }
    public Class appliesClass() {
		return m_primary.appliesClass();
    }
    public void lateBindResult(Class new_result) throws BindException {
		m_secondary.lateBindResult( new_result);
		if ( m_secondary.appliesClass()!=null)
		{
			m_primary.lateBindResult(m_secondary.appliesClass());
		}
    }
    public void lateBindApplies(Class new_applies) throws BindException {
		m_primary.lateBindApplies( new_applies);
		if ( m_primary.resultClass()!=null)
		{
			m_secondary.lateBindApplies( m_primary.resultClass());
		}
    }

	private Bindable m_primary;
	private Bindable m_secondary;
}