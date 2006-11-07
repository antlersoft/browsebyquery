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
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.DBFieldReference;

import com.antlersoft.query.BindException;
import com.antlersoft.query.BindImpl;
import com.antlersoft.query.CountPreservingFilter;
import com.antlersoft.query.DataSource;

class WriteReferences extends CountPreservingFilter
{
	WriteReferences()
	{
		m_bind=new BindImpl( BOOLEAN_CLASS, DBFieldReference.class);
	}

	protected boolean getCountPreservingFilterValue( DataSource source, Object toFilter)
	{
		return ((DBFieldReference)toFilter).isWrite();
	}
	
	private BindImpl m_bind;

	/* (non-Javadoc)
	 * @see com.antlersoft.query.BindImpl#appliesClass()
	 */
	public Class appliesClass() {
		return m_bind.appliesClass();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.BindImpl#lateBindApplies(java.lang.Class)
	 */
	public void lateBindApplies(Class new_applies) throws BindException {
		m_bind.lateBindApplies(new_applies);
	}
}
