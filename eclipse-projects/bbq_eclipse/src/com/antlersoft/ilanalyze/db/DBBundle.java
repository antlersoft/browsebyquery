/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import com.antlersoft.odb.CompoundKey;
import com.antlersoft.odb.FromRefIteratorEnumeration;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.ObjectKeyHashMap;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * A resource set in an assembly
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBBundle implements Persistent {

	static final String BUNDLE_KEY_INDEX = "BUNDLE_KEY_INDEX";
	ObjectRef<DBAssembly> _assembly;
	String _name;
	ObjectKeyHashMap<DBString,ObjectRef<DBStringResource>> _resources;

	private transient PersistentImpl _persistentImpl;

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}
	
	private DBBundle( DBAssembly assembly, String name)
	{
		_assembly=new ObjectRef<DBAssembly>(assembly);
		_name=name;
		_resources=new ObjectKeyHashMap<DBString,ObjectRef<DBStringResource>>();
		ObjectDB.makePersistent( this);
	}
	
	public String getName()
	{
		return _name;
	}
	
	public DBAssembly getAssembly()
	{
		return _assembly.getReferenced();
	}
	
	public Enumeration<DBStringResource> getResources()
	{
		return new FromRefIteratorEnumeration<DBStringResource>(_resources.values().iterator());
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
	
	static class BundleUpdater
	{
		DBBundle _bundle;
		HashMap<ObjectRef<DBString>,ObjectRef<DBStringResource>> _setValues;

		BundleUpdater( DBBundle bundle)
		{
			_bundle=bundle;
			_setValues=new HashMap<ObjectRef<DBString>,ObjectRef<DBStringResource>>();
		}
		
		void addNameValuePair( ILDB db, String name, String value)
		{
			DBString dbname=DBString.get(db, name);
			ObjectRef<DBString> refdbname=new ObjectRef<DBString>(dbname);
			ObjectRef<DBStringResource> refresource=_bundle._resources.get( refdbname);
			DBString dbvalue=DBString.get(db,value);
			if ( refresource==null)
			{
				refresource=new ObjectRef<DBStringResource>( new DBStringResource(_bundle,dbname,dbvalue));
				ObjectDB.makeDirty(_bundle);
			}
			else
				refresource.getReferenced().setValue(DBString.get(db, value));
			_setValues.put( refdbname, refresource);			
		}
		
		void finishBundle( ILDB db)
		{
			ArrayList<DBStringResource> todelete=new ArrayList<DBStringResource>();
			for ( ObjectRef<DBStringResource> i :_bundle._resources.values())
			{
				DBStringResource tocheck=i.getReferenced();
				if ( ! _setValues.containsKey(tocheck._name))
				{
					todelete.add(tocheck);
				}
			}
			boolean makedirty=false;
			for ( DBStringResource r : todelete)
			{
				makedirty=true;
				_bundle._resources.remove(r._name);
				db.deleteObject(r);
			}
			if ( makedirty)
				ObjectDB.makeDirty(_bundle);
		}
	}
}
