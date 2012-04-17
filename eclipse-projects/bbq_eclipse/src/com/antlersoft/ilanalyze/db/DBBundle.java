/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.bbq.db.DBBundleBase;

import com.antlersoft.odb.CompoundKey;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;

/**
 * A resource set in an assembly
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBBundle extends DBBundleBase {

	ObjectRef<DBAssembly> _assembly;

	private DBBundle( DBAssembly assembly, String name)
	{
		super(name);
		_assembly=new ObjectRef<DBAssembly>(assembly);
		ObjectDB.makePersistent( this);
	}
	
	public DBAssembly getAssembly()
	{
		return _assembly.getReferenced();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "bundle "+_name+" in "+getAssembly().toString();
	}

	static DBBundle get( ILDB db, DBAssembly assembly, String name)
	{
		DBBundle result=(DBBundle)db.findObject(BUNDLE_KEY_INDEX, makeBundleKey( assembly, name));
		
		if ( result==null)
			result=new DBBundle( assembly, name);
		
		return result;
	}
	
	static Comparable makeBundleKey( DBAssembly assembly, String name)
	{
		return new CompoundKey( new ObjectRefKey(assembly), name);
	}
	
	static class BundleKeyGenerator implements KeyGenerator
	{

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			DBBundle b=(DBBundle)o1;
			return makeBundleKey( b._assembly.getReferenced(), b._name);
		}
	}
}
