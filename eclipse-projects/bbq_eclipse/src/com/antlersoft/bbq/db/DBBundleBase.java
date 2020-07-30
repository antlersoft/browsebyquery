/**
 * Copyright (c) 2008 Michael A. MacDonald 
 */
package com.antlersoft.bbq.db;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import com.antlersoft.odb.FromRefIteratorEnumeration;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectKeyHashMap;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * Base class for a resource set, a collection of DBStringResource name-value pairs
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBBundleBase implements Persistent {

	static public final String BUNDLE_KEY_INDEX = "BUNDLE_KEY_INDEX";
	protected String _name;
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
	
	protected DBBundleBase( String name)
	{
		_name=name;
		_resources=new ObjectKeyHashMap<DBString,ObjectRef<DBStringResource>>();
	}
	
	public String getName()
	{
		return _name;
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
		return "bundle "+_name;
	}

	/**
	 * Populates a new resource bundle, or populates and updates an existing resource bundle.
	 * @author Michael A. MacDonald
	 *
	 */
	public static class BundleUpdater
	{
		private DBBundleBase _bundle;
		private HashMap<ObjectRef<DBString>,ObjectRef<DBStringResource>> _setValues;

		public BundleUpdater( DBBundleBase bundle)
		{
			_bundle=bundle;
			_setValues=new HashMap<ObjectRef<DBString>,ObjectRef<DBStringResource>>();
		}
		
		public void addNameValuePair( IndexObjectDB db, String name, String value)
		{
			DBString dbname=DBString.get(db, name);
			ObjectRef<DBString> refdbname=new ObjectRef<DBString>(dbname);
			ObjectRef<DBStringResource> refresource=_bundle._resources.get( refdbname);
			DBString dbvalue=DBString.get(db,value);
			if ( refresource==null)
			{
				refresource=new ObjectRef<DBStringResource>( new DBStringResource(db, _bundle,dbname,dbvalue));
				db.makeDirty(_bundle);
			}
			else
				refresource.getReferenced().setValue(db, DBString.get(db, value));
			_setValues.put( refdbname, refresource);			
		}
		
		public void finishBundle( IndexObjectDB db)
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
				db.makeDirty(_bundle);
		}
	}
}

