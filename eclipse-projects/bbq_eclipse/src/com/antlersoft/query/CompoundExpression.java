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

public class CompoundExpression extends ResolveCompoundBinding
implements ValueExpression, ScalarValueContext
{
	public CompoundExpression( ValueExpression primary, ValueExpression secondary)
	throws BindException
	{
		super( primary, secondary);
		m_primary_expr=primary;
		m_secondary_expr=secondary;
		if ( m_secondary_expr.getContext().getContextType()!=COUNT_PRESERVING)
			throw new BindException( "Value expression contexts incompatible");
		m_context_type=m_primary_expr.getContext().getContextType();
		if (m_context_type == SCALAR)
		{
			m_context_type=COUNT_PRESERVING;
		}
	}

	public void inputObject( ValueObject obj, DataSource source, Object to_transform)
	{
		((CountPreservingValueContext)m_primary_expr.getContext()).inputObject(
				  m_primary_expr, source, to_transform);
		((CountPreservingValueContext)m_secondary_expr.getContext()).inputObject(
				  m_secondary_expr, source, m_primary_expr.getValue());
	}

	public void startGroup( ValueObject obj, DataSource source)
	{
		((GroupValueContext)m_primary_expr.getContext()).startGroup( m_primary_expr, source);
	}

	public boolean addObject( ValueObject obj, DataSource source, Object to_add)
	{
		return ((GroupValueContext)m_primary_expr.getContext()).addObject( m_primary_expr,
			source, to_add);
	}

	public void finishGroup( ValueObject obj, DataSource source)
	{
		((GroupValueContext)m_primary_expr.getContext()).finishGroup(
				  m_primary_expr, source);
		((CountPreservingValueContext)m_secondary_expr.getContext()).inputObject(
				  m_secondary_expr, source, m_primary_expr.getValue());
	}

	public List getValueCollection()
	{
		return m_primary_expr.getValueCollection();
	}

	public Object getValue()
	{
		return m_secondary_expr.getValue();
	}

	public ValueContext getContext()
	{
		return this;
	}

	public int getContextType()
	{
		return m_context_type;
	}

	private int m_context_type;
	private ValueExpression m_primary_expr, m_secondary_expr;
}