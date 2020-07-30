/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.ObjectDB;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBCall extends DBReference {

	static final String CALL_TARGET="CALL_TARGET";
	/**
	 * @param source
	 * @param target The referenced DBString
	 */
	public DBCall(ObjectDB db, DBMethod source, DBMethod target) {
		super(source, target);
		db.makePersistent( this);
	}
	
	public DBMethod getCalled()
	{
		return (DBMethod)m_target.getReferenced();
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append( "Call to ");
		getCalled().addMethodInfo(sb);
		addFromInfo(sb);
		return sb.toString();
	}
}
