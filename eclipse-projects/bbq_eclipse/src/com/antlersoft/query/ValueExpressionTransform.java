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

public class ValueExpressionTransform extends Transform
{
	public ValueExpressionTransform(ValueExpression expr) {
		m_expr = expr;
		m_able_to_use_more = true;
	}

	public void lateBindApplies(Class new_applies) throws BindException {
		m_expr.lateBindApplies(new_applies);
	}

	public void lateBindResult(Class new_result) throws BindException {
		m_expr.lateBindResult(new_result);
	}

	public Class appliesClass() {
		return m_expr.appliesClass();
	}

	public Class resultClass() {
		return m_expr.resultClass();
	}

	public void startEvaluation(DataSource source) {
		switch (m_expr.getContext().getContextType()) {
			case ValueContext.GROUP:
				( (GroupValueContext) (m_expr.getContext())).startGroup(m_expr,
					source);
				break;
			case ValueContext.SCALAR:
				m_able_to_use_more=false;
				break;
		}
	}

	public boolean isAbleToUseMore()
	{
		return m_able_to_use_more;
	}

	public Enumeration transformObject(DataSource source, Object to_transform) {
		Enumeration result=EmptyEnum.empty;
		switch (m_expr.getContext().getContextType()) {
			case ValueContext.GROUP:
				m_able_to_use_more=((GroupValueContext)m_expr.getContext()).addObject( m_expr, source, to_transform);
				break;
			case ValueContext.COUNT_PRESERVING:
				((CountPreservingValueContext)m_expr.getContext()).inputObject( m_expr, source, to_transform);
			case ValueContext.SCALAR:
				result=new SingleEnum( m_expr.getValue());
		}

		return result;
	}
	public Enumeration finishEvaluation(DataSource source) {
		Enumeration result=EmptyEnum.empty;
		switch (m_expr.getContext().getContextType()) {
			case ValueContext.GROUP:
				((GroupValueContext)m_expr.getContext()).finishGroup( m_expr, source);
				result=new SingleEnum( m_expr.getValue());
				break;
		}

		return result;
	}
	private ValueExpression m_expr;
	private boolean m_able_to_use_more;
}