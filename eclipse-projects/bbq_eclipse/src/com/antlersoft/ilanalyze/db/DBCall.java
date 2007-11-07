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
	public DBCall(DBMethod source, DBMethod target) {
		super(source, target);
		ObjectDB.makePersistent( this);
	}
	
	public DBMethod getCalled()
	{
		return (DBMethod)m_target.getReferenced();
	}
}
