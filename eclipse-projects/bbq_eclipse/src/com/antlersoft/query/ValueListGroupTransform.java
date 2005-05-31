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

public class ValueListGroupTransform extends Transform {
	public ValueListGroupTransform( ValueList list)
	throws BindException
	{
		int type=list.getContextType();
		if ( type==ValueContext.GROUP)
			throw new BindException( "Group context value list can't be used in group transform");
		m_value_list=list;
		m_set_expression=new ValueListSetExpression( list);
	}
    public Enumeration finishEvaluation(DataSource source) {
		return EmptyEnum.empty;
    }
    public void lateBindApplies(Class new_applies) throws com.antlersoft.query.BindException {
		m_value_list.lateBindApplies( new_applies);
    }
    public void startEvaluation(DataSource source) {
    }
    public Class resultClass() {
		return m_set_expression.getClass();
    }
    public Class appliesClass() {
		return m_value_list.appliesClass();
    }
    public void lateBindResult(Class new_result) throws com.antlersoft.query.BindException {
		if ( ! new_result.isAssignableFrom( m_set_expression.getClass()))
			throw new BindException( "Trying to bind inappropriate type "+new_result.getName()
		+" to ValueListGroupTransform result.");
    }
    public Enumeration transformObject(DataSource source, Object to_transform) {
		m_value_list.inputObject( m_value_list, source, to_transform);
		return new SingleEnum( m_set_expression);
    }

	private ValueList m_value_list;
	private ValueListSetExpression m_set_expression;
}