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

public class Matches extends CountPreservingFilter {
	public Matches( String to_match)
	{
		m_to_match=to_match;
		m_binding=new BindImpl( BOOLEAN_CLASS, null);
	}
    public void lateBindApplies(Class new_applies) throws com.antlersoft.query.BindException {
		m_binding.lateBindApplies( new_applies);
    }
    public Class appliesClass() {
		return m_binding.appliesClass();
    }
	protected boolean getCountPreservingFilterValue( DataSource source, Object to_transform)
	{
		return to_transform.toString().indexOf( m_to_match)!= -1;
	}

	private BindImpl m_binding;
	private String m_to_match;
}