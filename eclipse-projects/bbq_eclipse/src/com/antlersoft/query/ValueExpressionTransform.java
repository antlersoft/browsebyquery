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