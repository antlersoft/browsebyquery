/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.bbq.query;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.antlersoft.bbq.db.DBPackage;

import com.antlersoft.odb.IndexObjectDB;

import com.antlersoft.query.DataSource;
import com.antlersoft.query.TransformImpl;

/**
 * Takes a string representing a class name and returns a set of class objects
 * corresponding to classes in any namespace that have that name.
 * 
 * @author Michael A. MacDonald
 *
 */
public class AnyClass extends TransformImpl {
	/**
	 * @author Michael A. MacDonald
	 *
	 */
	private class AnyClassEnumeration implements Enumeration {

		private Iterator packages;
		private Object nextObject;
		private String className;
		private IndexObjectDB db;
		private String indexName;
		
		AnyClassEnumeration( IndexObjectDB db, String className, String indexName)
		{
			this.db=db;
			this.className=className;
			this.indexName=indexName;
			this.nextObject=null;
			packages=db.getAll( DBPackage.class);
		}
		
		public boolean hasMoreElements() {
			getNextObject();
			return nextObject != null;
		}

		/* (non-Javadoc)
		 * @see java.util.Enumeration#nextElement()
		 */
		public Object nextElement() {
			getNextObject();
			if ( nextObject==null)
			{
				throw new NoSuchElementException();
			}
			Object result=nextObject;
			nextObject=null;
			return result;
		}

		private void getNextObject()
		{
			while ( nextObject==null && packages.hasNext())
			{
				Object p=packages.next();
				StringBuilder sb=new StringBuilder(p.toString());
				sb.append('.');
				sb.append(className);
				nextObject=db.findObject( indexName, sb.toString());
			}
		}
	}

	/** Index on result class by class name */
	String _indexName;
	
	public AnyClass( Class resultClass, String indexName)
	{
		super(resultClass,String.class);
		_indexName=indexName;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Transform#transformObject(com.antlersoft.query.DataSource, java.lang.Object)
	 */
	@Override
	public Enumeration transformObject(DataSource source, Object to_transform) {
		return new AnyClassEnumeration(((IDBSource)source).getSession(), (String)to_transform, _indexName);
	}

}
