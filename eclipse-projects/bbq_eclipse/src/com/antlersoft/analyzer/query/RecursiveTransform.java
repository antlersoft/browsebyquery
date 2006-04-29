/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.AnalyzerDB;
import com.antlersoft.parser.RuleActionException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author mike
 *
 */
public class RecursiveTransform implements Transform {

	private Transform _primary;
	private Transform _secondary;
	private AnalyzerDB _db;
	private ArrayList _pendingSet;
	
	/**
	 * 
	 */
	public RecursiveTransform( Transform primary, Transform secondary)
	throws RuleActionException
	{
		_primary=primary;
		_secondary=secondary;
		resolveBinding();
	}
	
	public void startEvaluation( AnalyzerDB db)
	{
		_primary.startEvaluation( db);
		_pendingSet=new ArrayList();
		_db=db;
	}
	
	public Enumeration transform( Object o)
		throws Exception
	{
		addEnumerationToPending( _primary.transform( o));
		return EmptyEnumeration.emptyEnumeration;
	}

	public Enumeration finishEvaluation()
	throws Exception
	{
		addEnumerationToPending( _primary.finishEvaluation());
		return new RecursiveEnumeration();
	}
	
	public Class resultClass()
	{
		return _primary.resultClass();
	}
	
	public Class appliesTo()
	{
		return _primary.appliesTo();
	}
	
	public void lateBindResult( Class newResultClass)
	throws RuleActionException
	{
		_primary.lateBindResult( newResultClass);
		resolveBinding();
	}
	
	public void lateBindApplies( Class newAppliesClass)
		throws RuleActionException
	{
		_primary.lateBindApplies( newAppliesClass);
		resolveBinding();
	}

	private void addEnumerationToPending( Enumeration e)
	{
		while ( e.hasMoreElements())
			_pendingSet.add( e.nextElement());
	}
	
	private void addEnumerationThroughSecondaryToPending( Enumeration e)
	throws Exception
	{
		while ( e.hasMoreElements())
		{
			addEnumerationToPending( _primary.transform( e.nextElement()));
		}
	}

	private void resolveBinding()
	throws RuleActionException
	{
		if ( _secondary!=null)
		{
			if ( _primary.appliesTo()!=null)
			{
				if ( _secondary.resultClass()==null)
					_secondary.lateBindResult( _primary.appliesTo());
				if ( ! _primary.appliesTo().isAssignableFrom( _secondary.resultClass()))
					throw new RuleActionException( "Can't convert "+_secondary.resultClass().toString()+" to "
							+_primary.appliesTo().toString());
			}
			else if ( _secondary.resultClass()!=null)
			{
				_primary.lateBindApplies( _secondary.resultClass());
			}
			if ( _primary.resultClass()!=null)
			{
				if ( _secondary.appliesTo()==null)
					_secondary.lateBindApplies( _primary.resultClass());
				if ( ! _secondary.appliesTo().isAssignableFrom( _primary.resultClass()))
					throw new RuleActionException( "Can't convert "+_primary.resultClass().toString()+" to "
							+_secondary.appliesTo().toString());
			}
			else if ( _secondary.appliesTo()!=null)
			{
				_primary.lateBindResult( _secondary.appliesTo());
			}
		}
	}
	
	private class RecursiveEnumeration implements Enumeration
	{
		private ArrayList _output;
		int _offset;
		
		public RecursiveEnumeration()
		{
			transformPendingSet();
		}
		
		public boolean hasMoreElements()
		{
			if ( _offset>=_output.size())
				transformPendingSet();
			
			return _offset<_output.size();
		}
		
		public Object nextElement()
		{
			if ( _offset>=_output.size())
				transformPendingSet();
			return _output.get( _offset++);
		}
		
		private void transformPendingSet()
		{
			_output=_pendingSet;
			_offset=0;
			try
			{
				if ( _output.size()>0)
				{
					_pendingSet=new ArrayList();
					_primary.startEvaluation( _db);
					if ( _secondary==null)
					{
						for ( Iterator i=_output.iterator(); i.hasNext();)
						{
							addEnumerationToPending( _primary.transform( i.next()));
						}
					}	
					else
					{
						_secondary.startEvaluation( _db);
						for ( Iterator i=_output.iterator(); i.hasNext();)
						{
							addEnumerationThroughSecondaryToPending( _secondary.transform( i.next()));
						}
						addEnumerationThroughSecondaryToPending( _secondary.finishEvaluation());
					}
					addEnumerationToPending( _primary.finishEvaluation());
				}
			}
			catch ( Exception e)
			{
				throw new RuntimeException( "Recursive transform processing error: "+e.toString(), e);
			}
		}
	}
}
