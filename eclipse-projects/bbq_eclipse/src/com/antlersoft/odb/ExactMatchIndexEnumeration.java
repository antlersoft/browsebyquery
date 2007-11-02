/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.odb;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Enumeration over results of an IndexIterator-- only those that exactly match the searched key
 * @author Michael A. MacDonald
 *
 */
public class ExactMatchIndexEnumeration implements Enumeration
{
	private IndexIterator _ii;
	private Object _nextObject;
	
	public ExactMatchIndexEnumeration( IndexIterator ii)
	{
		_ii=ii;
		determineNextObject();
	}
	
	public boolean hasMoreElements()
	{
		return _nextObject!=null;
	}
	
	public Object nextElement()
	throws NoSuchElementException
	{
		if ( _nextObject==null)
			throw new NoSuchElementException();
		Object result=_nextObject;
		determineNextObject();
		return result;
	}
	
	private void determineNextObject()
	{
		Object result=null;
		if ( _ii.hasNext() && _ii.isExactMatch())
		{
			result=_ii.next();
		}
		_nextObject=result;
	}
}