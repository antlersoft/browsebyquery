package com.antlersoft.query;

import java.util.List;

public class Exists extends Filter implements CountPreservingValueContext {
	public Exists( Transform transform)
	{
		m_transform=transform;
		m_result=false;
		m_result_object=new Boolean( false);
	}

    public List getValueCollection() {
		return NO_SUBS;
    }
    public Object getValue() {
		return m_result_object;
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
		boolean new_result=m_transform.transformObject( source, to_transform).hasMoreElements();
		new_result=m_transform.finishEvaluation( source).hasMoreElements() || new_result;
		if ( new_result!=m_result)
		{
			m_result=new_result;
			m_result_object=new Boolean( m_result);
		}
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
	private Boolean m_result_object;
	private Transform m_transform;
}