/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import com.antlersoft.bbq.db.DBBundleBase;

import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBBundle extends DBBundleBase {

	/**
	 * @author Michael A. MacDonald
	 *
	 */
	static class NameKeyGenerator implements KeyGenerator {

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return ((DBBundle)o1)._name;
		}
	}
	
	/**
	 * Create or return a DBBundle with the given name
	 * @param db
	 * @param name
	 * @return Existing bundle with the name, or a newly created bundle
	 */
	public static DBBundle get( IndexObjectDB db, String name)
	{
		DBBundle result=(DBBundle)db.findObject(DBBundleBase.BUNDLE_KEY_INDEX, name);
		
		if ( result==null)
			result=new DBBundle(db, name);
		
		return result;
	}

	DBBundle( ObjectDB db, String name)
	{
		super(name);
		db.makePersistent(this);
	}
}
