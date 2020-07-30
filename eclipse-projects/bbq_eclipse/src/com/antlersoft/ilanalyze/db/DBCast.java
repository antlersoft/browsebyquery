/**
 * Copyright (c) 2018 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.ObjectDB;

/**
 * An attempt within a method to cast an object to another class type; covers actual casts and also
 * things like the instanceof operator. 
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBCast extends DBReference<DBClass> {
	static final String CAST_TARGET = "CAST_TARGET";
	private boolean m_isOptional;

	public DBCast(ObjectDB db, DBMethod source, DBClass target, boolean isOptional) {
		super(source, target);
		db.makePersistent(this);
	}

	/**
	 * Optional casts check if the object can be cast to the target class;
	 * non-optional casts throw an exception if the object is not null and can't be cast
	 * @return True if the cast is optional
	 */
	public boolean isOptional()
	{
		return m_isOptional;
	}
	
	public void setOptional(boolean isOptional)
	{
		m_isOptional = isOptional;
	}
	
	/**
	 * The class to which the method containing this reference is trying to cast an object
	 * @return DBClass object representing the target class
	 */
	public DBClass getTarget()
	{
		return m_target.getReferenced();
	}

	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		
		if (isOptional())
		{
			sb.append("optional ");
		}
		sb.append( "cast to ");
		sb.append( getTarget().toString());
		addFromInfo( sb );
		
		return sb.toString();
	}
}
