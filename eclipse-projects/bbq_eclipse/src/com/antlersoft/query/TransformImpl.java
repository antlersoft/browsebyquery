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