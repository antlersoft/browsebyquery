package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public abstract class SetOperator implements Enumeration {

	public static final int UNION=1;
	public static final int INTERSECTION=2;
	public static final int WITHOUT=3;
	public static final int DEINTERSECTION=4;

	private static final String[] m_type_names=new String[] {
		null,
		"union",
		"intersection",
		"without",
		"deintersection"
	};

	public SetOperator( Comparator comp, Enumeration a, Enumeration b)
	{
		m_comp=comp;
		if ( m_comp==null)
			m_comp=DefaultComparator.m_default;
		m_a=a;
		m_b=b;
		m_next=determineNext();
	}

	public boolean hasMoreElements()
	{
		return m_next!=null;
	}

	public Object nextElement()
	{
		if ( m_next==null)
			throw new NoSuchElementException();
		Object result=m_next;
		do
		{
			m_next=determineNext();
		}
		while( m_next!=null && m_comp.compare( result, m_next)==0);
		return result;
	}

	public static SetOperator SetOperatorFactory( int type, Comparator comp, Enumeration a, Enumeration b)
	{
		SetOperator result=null;

		switch ( type)
		{
		case UNION :
			result=new SetUnion( comp, a, b);
			break;
		case INTERSECTION :
			result=new SetIntersection( comp, a, b);
			break;
		case WITHOUT :
			result=new SetWithout( comp, a, b);
			break;
		case DEINTERSECTION :
			result=new SetDeintersection( comp, a, b);
			break;
		default :
			throw new NoSuchElementException();
		}
		return result;
	}

	public static int getType( String type_name)
	{
		int i;
		for ( i=1; i<m_type_names.length &&
			  ! type_name.equals( m_type_names[i]); ++i);
		return i;
	}

	protected abstract Object determineNext();

	protected Object m_next;
	protected Comparator m_comp;
	protected Enumeration m_a;
	protected Enumeration m_b;
	protected Object m_current_a;
	protected Object m_current_b;
	protected boolean m_next_advance_a;

	public static class DefaultComparator implements Comparator
	{
		public int compare( Object a, Object b)
		{
			return ((Comparable)a).compareTo( b);
		}

		public boolean equals( Object o)
		{
			return o==m_default;
		}

		public static final DefaultComparator m_default=new DefaultComparator();
	}

	protected boolean nextPairInOrder()
	{
		boolean result=true;
		// Initial condition
		if ( m_next==null)
		{
			if ( m_a.hasMoreElements())
			{
				m_current_a=m_a.nextElement();
				if ( m_b.hasMoreElements())
				{
					m_current_b=m_a.nextElement();
					if ( m_comp.compare( m_current_a, m_current_b)>0)
						m_next_advance_a=false;
				}
				else
					m_next_advance_a=true;
			}
			else
			{
				if ( m_b.hasMoreElements())
				{
					m_next_advance_a=false;
					m_current_b=m_b.nextElement();
				}
				else
					result=false;
			}
		}
		else
		{
			if ( m_next_advance_a)
			{
				if ( m_a.hasMoreElements())
				{
					m_current_a=m_a.nextElement();
					if ( m_current_b!=null && m_comp.compare( m_current_a, m_current_b)>0)
						m_next_advance_a=false;
				}
				else
				{
					m_current_a=null;
					m_next_advance_a=false;
					result=m_current_b!=null;
				}
			}
			else
			{
				if ( m_b.hasMoreElements())
				{
					m_current_b=m_b.nextElement();
					if ( m_current_a!=null && m_comp.compare( m_current_a, m_current_b)<0)
						m_next_advance_a=true;
				}
				else
				{
					m_current_b=null;
					m_next_advance_a=true;
					result=m_current_a!=null;
				}
			}
		}
		return result;
	}
}