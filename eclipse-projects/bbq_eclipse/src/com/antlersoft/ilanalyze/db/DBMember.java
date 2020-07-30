/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.bbq.db.AnnotationCollection;
import com.antlersoft.bbq.db.DBAnnotatable;

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
public abstract class DBMember extends DBSourceObject implements HasProperties, HasDBType, DBAnnotatable {
	private String m_name;
	private ObjectRef<DBClass> m_class;
	private ObjectRef<DBType> m_type;
	private int m_properties;
	private AnnotationCollection m_annotations;
	
	DBMember( DBClass container, String name, DBType type)
	{
		m_class=new ObjectRef<DBClass>( container);
		m_name=name;
		m_type=new ObjectRef<DBType>( type);
		// Public until shown otherwise
		m_properties=DBDriver.IS_PUBLIC;
		m_annotations = new AnnotationCollection();
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public DBType getDBType()
	{
		return m_type.getReferenced();
	}
	
	public DBType getDBType( ILDB db)
	{
		return getDBType();
	}
	
	public DBClass getDBClass()
	{
		return m_class.getReferenced();
	}
	
	void setDBType( ObjectDB db, DBType new_type)
	{
		if ( ! new_type.equals( getDBType()))
		{
			m_type.setReferenced(new_type);
			db.makeDirty(this);
		}
	}
	
	public int getProperties()
	{
		return m_properties;
	}
	
	void setProperties( ObjectDB db, int properties)
	{
		if ( m_properties!=properties)
		{
			m_properties=properties;
			db.makeDirty(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.antlersoft.bbq.db.DBAnnotatable#getAnnotationCollection()
	 */
	public AnnotationCollection getAnnotationCollection() {
		return m_annotations;
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
