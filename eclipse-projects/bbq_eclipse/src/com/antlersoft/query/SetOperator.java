/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.NoSuchElementException;

public abstract class SetOperator {

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
	
	protected class SetOperatorSortedEnum implements Enumeration
	{
		protected Object m_next;
		protected Comparator m_comp;
		protected Enumeration m_a;
		protected Enumeration m_b;
		protected Object m_current_a;
		protected Object m_current_b;
		protected boolean m_next_advance_a;

		SetOperatorSortedEnum( Comparator comp, Enumeration a, Enumeration b)
		{
			m_comp=comp;
			if ( m_comp==null)
				m_comp=DefaultComparator.m_default;
			m_a=a;
			m_b=b;
			m_next=determineNext( this);
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
				m_next=determineNext( this);
			}
			while( m_next!=null && m_comp.compare( result, m_next)==0);
			return result;
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
	
	public static SetOperator SetOperatorFactory( int type)
	{
		SetOperator result=null;

		switch ( type)
		{
		case UNION :
			result=new SetUnion();
			break;
		case INTERSECTION :
			result=new SetIntersection();
			break;
		case WITHOUT :
			result=new SetWithout();
			break;
		case DEINTERSECTION :
			result=new SetDeintersection();
			break;
		default :
			throw new NoSuchElementException();
		}
		return result;
	}
	
	public Enumeration getSortedEnum( Comparator comp, Enumeration a, Enumeration b)
	{
		return new SetOperatorSortedEnum( comp, a, b);
	}

	public static int getType( String type_name)
	{
		int i;
		for ( i=1; i<m_type_names.length &&
			  ! type_name.equals( m_type_names[i]); ++i);
		return i;
	}

	protected abstract Object determineNext( SetOperatorSortedEnum e);

	protected HashSet m_set_a;
	protected HashSet m_set_b;
	protected HashSet m_set_both;

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
	
	private void initializeSets()
	{
		if ( m_set_a==null)
		{
			m_set_a=new HashSet();
			m_set_b=new HashSet();
			m_set_both=new HashSet();
		}
	}

	public Enumeration getUnsortedEnumeration()
	{
		initializeSets();
		Enumeration result=getEnumerationFromSets();
		
		// Clear references so they can be collected when enumeration is gone
		m_set_a=m_set_b=m_set_both=null;
		
		return result;
	}
	
	public void processEnumerationFromA( Enumeration e)
	{
		initializeSets();
		processEnumeration( e, m_set_a, m_set_b);
	}
	
	public void processEnumerationFromB( Enumeration e)
	{
		initializeSets();
		processEnumeration( e, m_set_b, m_set_a);
	}
	
	protected abstract Enumeration getEnumerationFromSets();
	
	private void processEnumeration( Enumeration e, HashSet same, HashSet other)
	{
		while (  e.hasMoreElements())
		{
			Object o=e.nextElement();

			if ( ! m_set_both.contains( o))
			{
				if ( other.contains( o))
				{
					other.remove( o);
					m_set_both.add( o);
				}
				else
					same.add( o);
			}
		}
	}
}