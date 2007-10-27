/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.ObjectDB;

/**
 * A field in a class in the analyzed system
 * @author Michael A. MacDonald
 *
 */
public class DBField extends DBMember {
	static final String FIELD_TYPE_INDEX="FIELD_TYPE_INDEX";
	
	DBField( DBClass container, String name, DBType type)
	{
		super( container, name, type);
		
		ObjectDB.makePersistent( this);
	}
	
	public String toString()
	{
		return getDBClass().toString()+"::"+getName()+" "+getDBType().toString();
	}
}
