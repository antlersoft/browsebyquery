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

import java.util.Enumeration;

public abstract class TransformImpl extends Transform {
	private BindImpl m_bind_impl;

	public TransformImpl( Class result, Class applies)
	{
		m_bind_impl=new BindImpl( result, applies);
	}
    public Class resultClass() {
		return m_bind_impl.resultClass();
    }
    public Class appliesClass() {
		return m_bind_impl.appliesClass();
    }
    public void lateBindResult(Class new_result) throws BindException {
		m_bind_impl.lateBindResult( new_result);
    }
    public void lateBindApplies(Class new_applies) throws BindException {
		m_bind_impl.lateBindApplies( new_applies);
    }

	public void startEvaluation( DataSource source) {}
	public Enumeration finishEvaluation( DataSource source) { return EmptyEnum.empty; }
}