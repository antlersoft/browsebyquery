/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.ObjectDB;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBStringReference extends DBReference {

	static final String SRTARGET="SRTARGET";
	/**
	 * @param source
	 * @param target The referenced DBString
	 */
	public DBStringReference(DBMethod source, DBString target) {
		super(source, target);
		ObjectDB.makePersistent( this);
	}

	public DBString getDBString()
	{
		return (DBString)m_target.getReferenced();
	}
}
