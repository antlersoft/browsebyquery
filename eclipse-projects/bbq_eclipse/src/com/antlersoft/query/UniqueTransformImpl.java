package com.antlersoft.query;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.NoSuchElementException;

import com.antlersoft.util.IteratorEnumeration;

/**
 * Framework for implementing other-wise stateless transforms that insure that the output enumerations
 * are unique.  Null objects are discarded.
 * @author Michael A. MacDonald
 *
 */
public abstract class UniqueTransformImpl extends TransformImpl {

	public UniqueTransformImpl( Class result, Class applies)
	{
		super( result, applies);
	}
	
	public void startEvaluation( DataSource source)
	{
		if ( getOrdering()==null)
			m_set=new HashSet();
		else
		{
			m_set=null;
			m_previous_object=null;
		}
	}
	
	public Enumeration transformObject(DataSource source, Object to_transform) {
		Enumeration result=EmptyEnum.empty;
		
		Object transformed=uniqueTransform( source, to_transform);
		
		if ( m_set!=null)
		{
			// No ordering case-- add objects to set
			if ( transformed instanceof Enumeration)
			{
				for ( Enumeration e=(Enumeration)transformed; e.hasMoreElements();)
					m_set.add( e.nextElement());
			}
			else
				m_set.add( transformed);
		}
		else
		{
			// Ordering case-- assuming identical objects sort together, don't
			// return an object that is the same as the previous
			if ( transformed instanceof Enumeration)
			{
				result=new UniqueEnumeration( (Enumeration)transformed);
			}
			else
			{
				if ( transformed!=null && ! transformed.equals( m_previous_object))
				{
					m_previous_object=transformed;
					result=new SingleEnum( transformed);
				}
			}
		}
		
		return result;
	}
	
	public Enumeration finishEvaluation( DataSource source)
	{
		Enumeration result=EmptyEnum.empty;
		if ( m_set!=null)
		{
			result=new IteratorEnumeration( m_set.iterator());
			m_set=null;
		}
		return result;
	}
	
	/**
	 * 
	 * @param source
	 * @param to_transform
	 * @return Single object or enumeration of objects to be added to this transforms output
	 */
	public abstract Object uniqueTransform( DataSource source, Object to_transform);
	
	/**
	 * Keeps track of unique objects when there is no ordering
	 */
	protected HashSet m_set;
	
	/**
	 * Keeps track of previous object to detect duplicates when there is ordering
	 */
	Object m_previous_object;

	/**
	 * Enumeration over an underlying enumeration that throws out adjacent duplicate objects;
	 * becomes invalid the next object that is fed to UniqueTransformImpl
	 * @author Michael A. MacDonald
	 *
	 */
	private class UniqueEnumeration implements Enumeration
	{
		UniqueEnumeration( Enumeration underlying)
		{
			m_underlying=underlying;
			getNextElement();
		}
		public boolean hasMoreElements()
		{
			return m_next!=null;
		}
		public Object nextElement()
		{
			if ( m_next==null)
				return new NoSuchElementException();
			Object result=m_next;
			getNextElement();
			return result;
		}
		private void getNextElement()
		{
			m_next=null;
			while ( m_underlying.hasMoreElements())
			{
				Object o=m_underlying.nextElement();
				if ( o!=null && ! o.equals( m_previous_object))
				{
					m_next=m_previous_object=o;
					break;
				}
			}
		}
		private Enumeration m_underlying;
		private Object m_next;
	}
	
}
