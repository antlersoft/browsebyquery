package com.antlersoft.query;

import java.util.List;

public class Exists extends Filter implements CountPreservingValueContext {
	public Exists( Transform transform)
	{
		m_transform=transform;
	}

    public List getValueCollection() {
		return NO_SUBS;
    }
	public boolean booleanValue() { return m_result; }
    public ValueContext getContext() {
		return this;
    }

	public int getContextType() {
		return COUNT_PRESERVING;
	}

	public void inputObject( ValueObject obj, DataSource source, Object to_transform)
	{
		m_transform.startEvaluation( source);
		m_result=m_transform.transformObject( source, to_transform).hasMoreElements();
		m_result=m_transform.finishEvaluation( source).hasMoreElements() || m_result;
	}

	// Bindable implementation
	public Class resultClass() { return Boolean.class; }
	public Class appliesClass() { return m_transform.appliesClass(); }
	public void lateBindResult( Class new_class) throws BindException
	{
		if ( ! new_class.isAssignableFrom( Boolean.class))
			throw new BindException( "Can't bind "+new_class.getName()+" to boolean.");
	}
	public void lateBindApplies( Class new_applies) throws BindException
	{
		m_transform.lateBindApplies( new_applies);
	}

	private boolean m_result;
	private Transform m_transform;
}