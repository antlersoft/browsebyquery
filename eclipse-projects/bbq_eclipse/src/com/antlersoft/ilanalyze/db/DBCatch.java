/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.ObjectDB;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBCatch extends DBReference<DBClass> {
	final static String CATCH_TARGET="CATCH_TARGET";
	DBCatch( ObjectDB db, DBMethod source, DBClass target)
	{
		super(source,target);
		db.makePersistent(this);
	}
	
	public DBClass getCaught()
	{
		return m_target.getReferenced();
	}
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append( "Catch of ");
		sb.append( getCaught().toString());
		addFromInfo( sb );
		
		return sb.toString();
	}
}
