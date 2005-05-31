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

public abstract class OperatorExpression extends ResolvePairBinding implements ValueExpression {
	protected OperatorExpression( ValueExpression a, ValueExpression b)
	throws BindException
	{
		super( a, b);
		m_context=new ResolvePairContext( a, b);
		m_a=a;
		m_b=b;
	}
    public ValueContext getContext() {
		return m_context;
    }
    public List getValueCollection() {
		return m_context.getValueCollection();
    }

	private ResolvePairContext m_context;
	protected ValueExpression m_a;
	protected ValueExpression m_b;
}