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

public abstract class Filter implements ValueExpression {
	protected static Class BOOLEAN_CLASS=Boolean.class;
	public abstract boolean booleanValue();

	public final Object getValue()
	{
		return adjustedBooleanValue() ? Boolean.TRUE : Boolean.FALSE;
	}

	public final void applyNot()
	{
		m_not= ! m_not;
	}

	public final boolean adjustedBooleanValue()
	{
		return m_not ? ! booleanValue() : booleanValue();
	}
	
	public final Class resultClass()
	{
		return BOOLEAN_CLASS;
	}
	
	public final void lateBindResult( Class new_result)
	throws BindException
	{
		if ( new_result!=BOOLEAN_CLASS)
			throw new BindException( "Trying to bind"+new_result.toString()+" to filter result");
	}

	private boolean m_not;
}