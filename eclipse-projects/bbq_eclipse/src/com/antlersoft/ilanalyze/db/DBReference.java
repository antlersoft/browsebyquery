/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;

/**
 * Base class for references found in methods in the analyzed system
 * @author Michael A. MacDonald
 *
 */
public class DBReference<T extends Persistent> extends DBSourceObject {
	
	/** DBMethod that contains this reference */
	ObjectRef<DBMethod> m_source;
	ObjectRef<T> m_target;
	
	public DBMethod getMethod()
	{
		return m_source.getReferenced();
	}

	/**
	 * 
	 */
	public DBReference( DBMethod source, T target) {
		m_source=new ObjectRef<DBMethod>( source);
		m_target=new ObjectRef<T>( target);
	}
	
	public void addFromInfo( StringBuilder sb)
	{
		sb.append( " from ");
		getMethod().addMethodInfo(sb);
		addPositionString( sb);
	}
	
	static class ReferenceTargetGenerator implements KeyGenerator
	{
		static ReferenceTargetGenerator G=new ReferenceTargetGenerator();

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return new ObjectRefKey( ((DBReference)o1).m_target);
		}
		
	}

}
