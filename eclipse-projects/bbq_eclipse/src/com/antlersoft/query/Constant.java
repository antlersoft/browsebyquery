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

public class Constant extends BindImpl implements ValueExpression {
	public Constant( Object o)
	throws BindException
	{
		super( o.getClass(), Object.class);
		m_value=o;
	}

	public Object getValue()
	{
		return m_value;
	}

	public List getValueCollection()
	{
		return NO_SUBS;
	}

	public ValueContext getContext()
	{
		return m_context;
	}

	private Object m_value;
	private static ScalarValueContextImpl m_context=new ScalarValueContextImpl();
}