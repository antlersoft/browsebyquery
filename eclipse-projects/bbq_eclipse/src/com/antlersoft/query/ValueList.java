package com.antlersoft.query;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class ValueList implements ValueObject, ScalarValueContext {
	public ValueList()
	{
		m_expression_list=new ArrayList();
		m_current_binding=new BindImpl( null, Object.class);
		m_context_type=SCALAR;
		m_group_context=new GroupValueContextImpl();
		m_cp_context=new CountPreservingValueContextImpl();
	}
	public ValueList( ValueObject vobj)
	throws BindException
	{
		this();
		add( vobj);
	}
	public void add( ValueObject vobj)
	throws BindException
    {
		if ( m_context_type!=SCALAR && m_context_type!=vobj.getContext().
			getContextType())
		    throw new BindException( "Adding value expression with incompatible context to list");
	    else
		    m_context_type=vobj.getContext().getContextType();
		m_current_binding=new ResolvePairBinding( m_current_binding, vobj);
		m_expression_list.add( vobj);
	}
    public void startGroup(ValueObject vobj, DataSource source) {
		m_group_context.startGroup( vobj, source);
    }
    public boolean addObject(ValueObject vobj, DataSource source, Object to_add) {
		return m_group_context.addObject( vobj, source, to_add);
    }
    public void finishGroup(ValueObject vobj, DataSource source) {
		finishGroup( vobj, source);
    }
    public void inputObject(ValueObject value, DataSource source, Object input) {
		m_cp_context.inputObject( value, source, input);
    }
    public int getContextType() {
		return m_context_type;
    }
	public List getValueCollection()
	{
		return m_expression_list;
	}

	public ValueContext getContext()
	{
		return this;
	}

	public Class resultClass() { return m_current_binding.resultClass(); }
	public Class appliesClass() { return m_current_binding.appliesClass(); }
	public void lateBindResult( Class new_result)
	throws BindException
	{
		m_current_binding.lateBindResult( new_result);
	}
	public void lateBindApplies( Class new_applies)
	throws BindException
	{
		m_current_binding.lateBindApplies( new_applies);
	}

	public Enumeration evaluate()
	{
		return new ValueListEnumeration( m_expression_list.iterator());
	}
	private Bindable m_current_binding;
	private int m_context_type;
	private ArrayList m_expression_list;
	private GroupValueContext m_group_context;
	private CountPreservingValueContext m_cp_context;

	class ValueListEnumeration implements Enumeration
	{
		ValueListEnumeration( Iterator i)
		{
			m_i=i;
		}

		public boolean hasMoreElements()
		{
			return m_i.hasNext();
		}

		public Object nextElement()
		{
			return ((ValueExpression)m_i.next()).getValue();
		}

		Iterator m_i;
	}
}