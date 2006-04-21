/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import java.util.Enumeration;

import com.antlersoft.classwriter.TypeParse;

import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

/**
 * @author Michael MacDonald
 *
 */
public class DBType implements Persistent, Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2116580043409773373L;
	private ObjectRef _class;
	private ObjectRef _arrayReferencedType;
	private String _builtInType;
	private String _typeString;
	
	private transient PersistentImpl _persistentImpl;

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.Persistent#_getPersistentImpl()
	 */
	public PersistentImpl _getPersistentImpl() {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
		
		return _persistentImpl;
	}

	public DBType( String typeString, AnalyzerDB db)
	throws Exception
	{
		_typeString=typeString;
		String t=TypeParse.parseFieldType( typeString);
		if ( t==TypeParse.ARG_ARRAYREF)
		{
			_arrayReferencedType=new ObjectRef( db.getWithKey( "com.antlersoft.analyzer.DBType", typeString.substring( 1)));
		}
		else if ( t==TypeParse.ARG_OBJREF)
		{
			_class=new ObjectRef(
					db.getWithKey( "com.antlersoft.analyzer.DBClass",
							TypeParse.convertFromInternalClassName(
									typeString.substring( 1, typeString.length()-2))));
		}
		_builtInType=t;
		_persistentImpl=new PersistentImpl();
		ObjectDB.makePersistent( this);
	}
	
	public boolean isArrayRef()
	{
		return _arrayReferencedType!=null;
	}
	
	public boolean isClassRef()
	{
		return _class!=null;
	}
	
	public boolean isBuiltIn()
	{
		return ! ( _builtInType==TypeParse.ARG_OBJREF || _builtInType==TypeParse.ARG_ARRAYREF);
	}
	
	public DBClass getReferencedClass()
	{
		return isClassRef() ? (DBClass)_class.getReferenced() : null;
	}
	
	public DBType getArrayReferencedType()
	{
		return isArrayRef() ? (DBType)_class.getReferenced() : null;
	}
	
	public DBType getArrayType( AnalyzerDB db)
	throws Exception
	{
		return (DBType)db.getWithKey( "com.antlersoft.analyzer.DBType", "["+_typeString);
	}
	
	public String getBuiltInType()
	{
		return _builtInType;
	}
	
	public Enumeration getReturningMethods( AnalyzerDB db)
	{
		// TODO: implement this method
		return null;
	}
	
	public Enumeration getFields( AnalyzerDB db)
	{
		// TODO: implement this method
		return null;
	}
	
	public Enumeration getArguments( AnalyzerDB db)
	{
		// TODO: implement this method
		return null;
	}
}
