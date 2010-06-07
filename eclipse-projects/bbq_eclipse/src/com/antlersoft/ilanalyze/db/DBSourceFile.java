/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * A source file that has been referenced by a source declaration in the analyzed system
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBSourceFile implements Persistent {
	
	/** Primary key on source file name */
	static final String SOURCE_FILE_NAME_INDEX="SOURCE_FILE_NAME_INDEX";
	private transient PersistentImpl _persistentImpl;
	
	private String m_source_file_name;
	
	private DBSourceFile( String source_file_name)
	{
		m_source_file_name=source_file_name;
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
	
	static synchronized DBSourceFile get( IndexObjectDB db, String f)
	{
		DBSourceFile result=(DBSourceFile)db.findObject( SOURCE_FILE_NAME_INDEX,
			f);
		if ( result==null)
			result=new DBSourceFile( f);
		return result;
	}
	
	public String toString()
	{
		return m_source_file_name;
	}
}
