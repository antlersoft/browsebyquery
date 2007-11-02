/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import java.util.Enumeration;

import com.antlersoft.odb.ExactMatchIndexEnumeration;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRefKey;

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
	
	/**
	 * Return an enumeration over references to this field
	 * @param db ILDB for this analyzed system
	 * @return an enumeration over calls to this field
	 */
	public Enumeration getReferencesTo( ILDB db)
	{
		return new ExactMatchIndexEnumeration( db.greaterThanOrEqualEntries(DBFieldReference.FRTARGET, new ObjectRefKey(this)));
	}
	
	public String toString()
	{
		return getDBClass().toString()+"::"+getName()+" "+getDBType().toString();
	}
}
