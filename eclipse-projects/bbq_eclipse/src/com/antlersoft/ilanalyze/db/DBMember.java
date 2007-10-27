/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.ilanalyze.DBDriver;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;

/**
 * A member in a class in the analyzed system
 * @author Michael A. MacDonald
 *
 */
public abstract class DBMember extends DBSourceObject {
	private String m_name;
	private ObjectRef m_class;
	private ObjectRef m_type;
	private int m_properties;
	
	DBMember( DBClass container, String name, DBType type)
	{
		m_class=new ObjectRef( container);
		m_name=name;
		m_type=new ObjectRef( type);
		// Public until shown otherwise
		m_properties=DBDriver.IS_PUBLIC;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public DBType getDBType()
	{
		return (DBType)m_type.getReferenced();
	}
	
	public DBClass getDBClass()
	{
		return (DBClass)m_class.getReferenced();
	}
	
	void setDBType( DBType new_type)
	{
		if ( ! new_type.equals( getDBType()))
		{
			m_type.setReferenced(new_type);
			ObjectDB.makeDirty(this);
		}
	}
	
	public int getProperties()
	{
		return m_properties;
	}
	
	void setProperties( int properties)
	{
		if ( m_properties!=properties)
		{
			m_properties=properties;
			ObjectDB.makeDirty(this);
		}
	}
	
	static class MemberTypeGenerator implements KeyGenerator
	{

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return new ObjectRefKey( ((DBMember)o1).m_type);
		}
		
	}
}
