/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * An assembly that has been analyzed
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBAssembly implements Persistent {
	private transient PersistentImpl _persistentImpl;

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}
}
