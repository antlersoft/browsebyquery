package com.antlersoft.query;

public class ResolveCompoundBinding implements Bindable {
	public ResolveCompoundBinding( Bindable primary, Bindable secondary)
	throws BindException
	{
		m_primary=primary;
		m_secondary=secondary;
		if ( m_primary.resultClass()!=null)
		{
			m_secondary.lateBindApplies( m_primary.resultClass());
		}
		else if ( m_secondary.appliesClass()!=null)
		{
			m_primary.lateBindResult( m_secondary.appliesClass());
		}
	}
    public Class resultClass() {
		return m_secondary.resultClass();
    }
    public Class appliesClass() {
		return m_primary.appliesClass();
    }
    public void lateBindResult(Class new_result) throws BindException {
		m_secondary.lateBindResult( new_result);
		if ( m_secondary.appliesClass()!=null)
		{
			m_primary.lateBindResult(m_secondary.appliesClass());
		}
    }
    public void lateBindApplies(Class new_applies) throws BindException {
		m_primary.lateBindApplies( new_applies);
		if ( m_primary.resultClass()!=null)
		{
			m_secondary.lateBindApplies( m_primary.resultClass());
		}
    }

	private Bindable m_primary;
	private Bindable m_secondary;
}