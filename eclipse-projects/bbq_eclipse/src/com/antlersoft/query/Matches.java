package com.antlersoft.query;

import java.util.List;

public class Matches extends Filter implements CountPreservingValueContext {
	public Matches( String to_match)
	{
		m_to_match=to_match;
		m_binding=new BindImpl( Boolean.class, null);
	}
    public List getValueCollection() {
		return NO_SUBS;
    }
    public void lateBindApplies(Class new_applies) throws com.antlersoft.query.BindException {
		m_binding.lateBindApplies( new_applies);
    }
    public Class resultClass() {
		return m_binding.resultClass();
    }
    public Class appliesClass() {
		return m_binding.appliesClass();
    }
    public boolean booleanValue() {
		return m_result;
    }
    public void lateBindResult(Class new_result) throws com.antlersoft.query.BindException {
		m_binding.lateBindResult( new_result);
    }
    public ValueContext getContext() {
		return this;
    }
	public int getContextType() {
		return COUNT_PRESERVING;
	}
	public void inputObject( ValueObject obj, DataSource source, Object to_transform)
	{
		m_result=to_transform.toString().indexOf( m_to_match)!= -1;
	}

	private BindImpl m_binding;
	private String m_to_match;
	private boolean m_result;
}