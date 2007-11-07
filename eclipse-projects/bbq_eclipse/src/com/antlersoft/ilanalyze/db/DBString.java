/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import java.util.Enumeration;

import com.antlersoft.odb.ExactMatchIndexEnumeration;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * A string that appears in the analyzed system
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBString implements Persistent {
	
	/** Primary key on string name */
	static final String STRING_INDEX="STRING_INDEX";
	private transient PersistentImpl _persistentImpl;
	
	private String m_string_name;
	
	private DBString( String string_name)
	{
		m_string_name=string_name;
		ObjectDB.makePersistent( this);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}
	
	public static DBString get( IndexObjectDB db, String f)
	{
		DBString result=(DBString)db.findObject( STRING_INDEX,
			f);
		if ( result==null)
			result=new DBString( f);
		return result;
	}
	
	/**
	 * Return an enumeration over references to this string
	 * @param db ILDB for this analyzed system
	 * @return an enumeration over references to this string
	 */
	public Enumeration getReferencesTo( ILDB db)
	{
		return new ExactMatchIndexEnumeration( db.greaterThanOrEqualEntries(DBStringReference.SRTARGET, new ObjectRefKey(this)));
	}
	
	public String toString()
	{
		return m_string_name;
	}
}
