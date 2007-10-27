/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * A method argument
 * @author Michael A. MacDonald
 *
 */
public class DBArgument implements Persistent {
	
	static final String ARG_TYPE_INDEX="ARG_TYPE_INDEX";
	
	private ObjectRef m_type;
	private String m_name;
	private int m_index;
	
	private transient PersistentImpl _persistentImpl;
	
	DBArgument( DBType type, String name, int index)
	{
		m_type=new ObjectRef( m_type);
		m_name=name;
		m_index=index;
		
		ObjectDB.makePersistent(this);
	}

	public String getName()
	{
		return m_name;
	}
	
	void setName( String name)
	{
		if ( ! m_name.equals(name))
		{
			m_name=name;
			ObjectDB.makeDirty(this);
		}
	}
	
	public DBType getDBType()
	{
		return (DBType)m_type.getReferenced();
	}
	
	synchronized void setDBType( DBType t)
	{
		boolean changed;
		DBType old_type=getDBType();
		if ( old_type==null)
			changed=(t!=null);
		else
			changed= (! old_type.equals(t));
		if ( changed)
		{
			m_type.setReferenced(t);
			ObjectDB.makeDirty(this);
		}
	}
	
	public int getIndex()
	{
		return m_index;
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		DBType t=getDBType();
		if ( t==null)
			sb.append("...");
		else
		{
			if ( m_name!=null)
			{
				sb.append( m_name);
				sb.append(' ');
			}
			sb.append( t.toString());
		}
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public synchronized PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}

	static class ArgTypeGenerator implements KeyGenerator
	{

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return new ObjectRefKey(((DBArgument)o1).m_type);
		}
		
	}
}
