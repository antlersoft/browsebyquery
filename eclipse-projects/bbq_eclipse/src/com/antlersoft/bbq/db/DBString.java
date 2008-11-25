/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.bbq.db;

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
	public static final String STRING_INDEX="STRING_INDEX";
	/** Index of string references by DBString key */
	public static final String SRTARGET="SRTARGET";
	
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
		DBString result=find( db, f);
		if ( result==null)
			result=new DBString( f);
		return result;
	}
	
	public static DBString find( IndexObjectDB db, String f)
	{
		 return (DBString)db.findObject( STRING_INDEX,
			f);
	}
	
	/**
	 * Return an enumeration over references to this string
	 * @param db ILDB for this analyzed system
	 * @return an enumeration over references to this string
	 */
	public Enumeration getReferencesTo( IndexObjectDB db)
	{
		return new ExactMatchIndexEnumeration( db.greaterThanOrEqualEntries(SRTARGET, new ObjectRefKey(this)));
	}
	
	/**
	 * Return an enumeration over DBStringResource whose value is this string
	 * @param db ILDB for this analyzed system
	 * @return an enumeration over string resources with this string as the value
	 */
	public Enumeration getResourcesContaining( IndexObjectDB db)
	{
		return new ExactMatchIndexEnumeration( db.greaterThanOrEqualEntries(DBStringResource.STRING_RESOURCE_VALUE_INDEX, new ObjectRefKey(this)));
	}
	
	/**
	 * Return an enumeration over DBStringResource whose namee is this string
	 * @param db ILDB for this analyzed system
	 * @return an enumeration over string resources with this string as the name
	 */
	public Enumeration getResourcesNamedBy( IndexObjectDB db)
	{
		return new ExactMatchIndexEnumeration( db.greaterThanOrEqualEntries(DBStringResource.STRING_RESOURCE_NAME_INDEX, new ObjectRefKey(this)));		
	}
	
	public String toString()
	{
		return m_string_name;
	}
}
