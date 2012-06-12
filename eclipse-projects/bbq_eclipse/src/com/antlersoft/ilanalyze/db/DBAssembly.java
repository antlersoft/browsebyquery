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
 * An assembly that has been analyzed
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBAssembly implements Persistent {
	
	/** Primary key on assembly name */
	static public final String ASSEMBLY_NAME_INDEX="ASSEMBLY_NAME_INDEX";
	private transient PersistentImpl _persistentImpl;
	
	private String m_assembly_name;
	
	private DBAssembly( String assembly_name)
	{
		m_assembly_name=assembly_name;
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
	
	/**
	 * Return an enumeration the classes in this assembly
	 * @param db ILDB for this analyzed system
	 * @return an enumeration over classes in this assembly
	 */
	public Enumeration getContainedClasses( ILDB db)
	{
		return new ExactMatchIndexEnumeration( db.greaterThanOrEqualEntries(DBClass.CLASS_ASSEMBLY_INDEX, new ObjectRefKey(this)));
	}
	
	static synchronized DBAssembly get( IndexObjectDB db, String f)
	{
		DBAssembly result=(DBAssembly)db.findObject( ASSEMBLY_NAME_INDEX,
			f);
		if ( result==null)
			result=new DBAssembly( f);
		return result;
	}
	
	public String toString()
	{
		return m_assembly_name;
	}
}
