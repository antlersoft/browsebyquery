/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.bbq.db.DBString;
import com.antlersoft.odb.ObjectDB;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBStringReference extends DBReference {

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
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append( "Reference to ");
		sb.append( getDBString().toString());
		addPositionString( sb);
		return sb.toString();
	}
}
