package com.antlersoft.query;

import java.util.List;

public class CompoundExpression extends ResolveCompoundBinding
implements ValueExpression, ScalarValueContext
{
	public CompoundExpression( ValueExpression primary, ValueExpression secondary)
	throws BindException
	{
		super( primary, secondary);
		if ( m_secondary.getContext().getContextType()!=COUNT_PRESERVING)
			throw new BindException( "Value expression contexts incompatible");
		m_context_type=m_primary.getContext().getContextType();
		if (m_context_type == SCALAR)
		{
			m_context_type=COUNT_PRESERVING;
		}
	}

	public void inputObject( ValueObject obj, DataSource source, Object to_transform)
	{
		((CountPreservingValueContext)m_primary.getContext()).inputObject(
				  m_primary, source, to_transform);
		((CountPreservingValueContext)m_secondary.getContext()).inputObject(
				  m_secondary, source, m_primary.getValue());
	}

	public void startGroup( ValueObject obj, DataSource source)
	{
		((GroupValueContext)m_primary.getContext()).startGroup( m_primary, source);
	}

	public boolean addObject( ValueObject obj, DataSource source, Object to_add)
	{
		return ((GroupValueContext)m_primary.getContext()).addObject( m_primary,
			source, to_add);
	}

	public void finishGroup( ValueObject obj, DataSource source)
	{
		((GroupValueContext)m_primary.getContext()).finishGroup(
				  m_primary, source);
		((CountPreservingValueContext)m_secondary.getContext()).inputObject(
				  m_secondary, source, m_primary.getValue());
	}

	public List getValueCollection()
	{
		return m_primary.getValueCollection();
	}

	public Object getValue()
	{
		return m_secondary.getValue();
	}

	public ValueContext getContext()
	{
		return this;
	}

	public int getContextType()
	{
		return m_context_type;
	}

	private int m_context_type;
	private ValueExpression m_primary, m_secondary;
}