package com.antlersoft.analyzer;

import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public class DBArgument implements Persistent {
	
	ObjectRef dbtype;
	ObjectRef dbmethod;
	int ordinal;
	String name;

	private transient PersistentImpl _persistentImpl;

	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		
		return _persistentImpl;
	}
	
	public DBMethod getMethod()
	{
		return (DBMethod)dbmethod.getReferenced();
	}
	
	public DBType getType()
	{
		return (DBType)dbtype.getReferenced();
	}
	
	public int getOrdinal()
	{
		return ordinal;
	}

}
