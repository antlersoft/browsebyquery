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
public class DBArgument implements Persistent, HasDBType {
	
	static final String ARG_TYPE_INDEX="ARG_TYPE_INDEX";
	
	private ObjectRef m_type;
	private String m_name;
	private int m_index;
	private ObjectRef m_method;
	
	private transient PersistentImpl _persistentImpl;
	
	DBArgument( DBMethod method, DBType type, String name, int index)
	{
		m_type=new ObjectRef( type);
		m_method=new ObjectRef( method);
		m_name=( name==null ? "" : name);
		m_index=index;
		
		ObjectDB.makePersistent(this);
	}

	public String getName()
	{
		return m_name;
	}
	
	void setName( String name)
	{
		if ( name!=null && name.length()>0)
		{
			if ( ! m_name.equals(name))
			{
				m_name=name;
				ObjectDB.makeDirty(this);
			}
		}
	}
	
	public DBType getDBType()
	{
		return (DBType)m_type.getReferenced();
	}
	
	public DBType getDBType( ILDB db)
	{
		return getDBType();
	}
	
	public DBMethod getMethod()
	{
		return (DBMethod)m_method.getReferenced();
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
	
	public void addArgInfo( StringBuilder sb)
	{
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
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append( getMethod().getDBClass().toString());
		sb.append( "::");
		sb.append( getMethod().getName());
		sb.append( " #");
		sb.append( m_index);
		sb.append( ":");
		addArgInfo( sb);
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
