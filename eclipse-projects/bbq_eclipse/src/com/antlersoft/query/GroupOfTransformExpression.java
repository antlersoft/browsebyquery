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
import java.util.List;

public class GroupOfTransformExpression extends ResolveCompoundBinding
implements ValueExpression, CountPreservingValueContext
{
	public GroupOfTransformExpression( ValueExpression expr, Transform transform)
	throws BindException
	{
		super( transform, expr);
		m_transform=transform;
		m_expr=expr;
	}

	public List getValueCollection()
	{
		return NO_SUBS;
	}

	public Object getValue()
	{
		return m_expr.getValue();
	}

	public ValueContext getContext()
	{
		return this;
	}

	public void inputObject( ValueObject vobj, DataSource source, Object next)
	{
		m_transform.startEvaluation( source);
		GroupValueContext context=(GroupValueContext)m_expr.getContext();
		Enumeration e=m_transform.transformObject( source, next);
		boolean need_more=true;
		while ( need_more && e.hasMoreElements())
		{
			need_more=context.addObject(
						 m_expr, source, e.nextElement());
		}
		e=m_transform.finishEvaluation( source);
		while ( need_more && e.hasMoreElements())
		{
			need_more=context.addObject(
						 m_expr, source, e.nextElement());
		}
		context.finishGroup( m_expr, source);
	}

	public int getContextType()
	{
		return COUNT_PRESERVING;
	}

	private ValueExpression m_expr;
	private Transform m_transform;
}

