package com.antlersoft.query;

import java.util.Comparator;
import java.util.List;

public class RelationalFilter extends Filter {
	public static final int LESS_THAN=1;
	public static final int LESS_THAN_OR_EQUALS=2;
	public static final int EQUALS=3;
	public static final int NOT_EQUALS=4;
	public static final int GREATER_THAN_OR_EQUALS=5;
	public static final int GREATER_THAN=6;

	private static final String[] m_operators=new String[] {
		null,
		"<",
		"<=",
		"=",
		"!=",
		">=",
		">"
	};

	public static int getType( String op)
	{
		int i=1;
		for ( ; i<m_operators.length && ! op.equals( m_operators[i]); ++i)
			;
		return i;
	}

	public RelationalFilter( String op, ValueExpression a, ValueExpression b)
	throws BindException
	{
		this( getType( op), a, b, SetOperator.DefaultComparator.m_default);
	}

	public RelationalFilter( int type, ValueExpression a, ValueExpression b, Comparator comp)
	throws BindException
	{
		m_type=type;
		m_a=a;
		m_b=b;
		m_comp=comp;
		m_binding=new ResolvePairBinding( a, b);
		m_context=new ResolvePairContext( a, b);
	}

	public List getValueCollection() {
		return m_context.getValueCollection();
    }
    public void lateBindApplies(Class new_applies) throws com.antlersoft.query.BindException {
		m_binding.lateBindApplies( new_applies);
    }
    public Class resultClass() {
		return Boolean.class;
    }
    public Class appliesClass() {
		return m_binding.appliesClass();
    }
    public boolean booleanValue() {
		boolean result=false;

		int r=m_comp.compare( m_a.getValue(), m_b.getValue());

		switch ( m_type)
		{
			case LESS_THAN : result= r<0; break;
			case LESS_THAN_OR_EQUALS : result= r<=0; break;
			case EQUALS : result= r==0; break;
			case NOT_EQUALS : result= r!=0; break;
			case GREATER_THAN_OR_EQUALS : result= r>=0;
			case GREATER_THAN : result= r>0; break;
		}

		return result;
    }
    public void lateBindResult(Class new_result) throws com.antlersoft.query.BindException {
		if ( ! new_result.isAssignableFrom( Boolean.class))
			throw new BindException( "Inappropriate type "+new_result.getName()+
		" bound as RelationalFilter result type");
    }
    public ValueContext getContext() {
		return m_context;
    }

	private ResolvePairBinding m_binding;
	private ResolvePairContext m_context;
	private ValueExpression m_a, m_b;
	private int m_type;
	private Comparator m_comp;
}