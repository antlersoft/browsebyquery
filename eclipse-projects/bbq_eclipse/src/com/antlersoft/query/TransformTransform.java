package com.antlersoft.query;

import java.util.Enumeration;

public class TransformTransform extends Transform {

	public TransformTransform( Transform primary, Transform secondary)
	throws BindException
	{
		m_binding=new ResolveCompoundBinding(primary, secondary);
		m_primary=primary;
		m_secondary=secondary;
	}
    public Enumeration finishEvaluation(DataSource source) {
		TransformSet.BaseAdapter base=new TransformSet.BaseAdapter(
				  source, m_secondary, m_primary.finishEvaluation( source));

		CombineEnum result=new CombineEnum(
				  new MultiEnum( base), null);
		base.m_combined=result;
		return result;
    }
    public void lateBindApplies(Class new_applies) throws BindException {
		m_binding.lateBindApplies( new_applies);
    }
    public void startEvaluation(DataSource source) {
		m_primary.startEvaluation( source);
		m_secondary.startEvaluation( source);
    }
    public Class resultClass() {
		return m_binding.resultClass();
    }
    public Class appliesClass() {
		return m_binding.appliesClass();
    }
    public void lateBindResult(Class new_result) throws BindException {
		m_binding.lateBindResult(new_result);
    }
    public Enumeration transformObject(DataSource source, Object to_transform) {
		return new MultiEnum(
				  new TransformSet.BaseAdapter(
						source, m_secondary, m_primary.finishEvaluation( source)
						));
    }

	private ResolveCompoundBinding m_binding;
	private Transform m_primary, m_secondary;
}