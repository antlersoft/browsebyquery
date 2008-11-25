/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.bbq.db;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * A string resource in a resource set/bundle
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBStringResource implements Persistent {
	
	public final static String STRING_RESOURCE_VALUE_INDEX="STRING_RESOURCE_VALUE_INDEX";
	public final static String STRING_RESOURCE_NAME_INDEX="STRING_RESOURCE_NAME_INDEX";

	ObjectRef<DBString> _name;
	ObjectRef<DBString> _value;
	ObjectRef<DBBundleBase> _bundle;
	
	private transient PersistentImpl _persistentImpl;

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		return _persistentImpl;
	}
	
	public DBStringResource( DBBundleBase bundle, DBString name, DBString value){
		_bundle=new ObjectRef<DBBundleBase>( bundle);
		_name=new ObjectRef<DBString>( name);
		_value=new ObjectRef<DBString>( value);
		ObjectDB.makePersistent(this);
	}
	
	void setValue( DBString v)
	{
		if ( ! _value.getReferenced().equals(v))
		{
			_value=new ObjectRef<DBString>(v);
			ObjectDB.makeDirty(this);
		}
	}
	
	public DBString getName()
	{
		return _name.getReferenced();
	}
	
	public DBString getValue()
	{
		return _value.getReferenced();
	}
	
	public DBBundleBase getBundle()
	{
		return _bundle.getReferenced();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append('"');
		sb.append( getName().toString());
		sb.append( "\" = \"");
		sb.append( getValue().toString());
		sb.append( "\" in bundle ");
		sb.append( getBundle().toString());
		return sb.toString();
	}

	public static class ValueKeyGenerator implements KeyGenerator
	{

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return new ObjectRefKey(((DBStringResource)o1)._value);
		}
	}
	public static class NameKeyGenerator implements KeyGenerator
	{

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return new ObjectRefKey(((DBStringResource)o1)._name);
		}
	}
}
