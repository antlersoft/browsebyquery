/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import java.util.Enumeration;

import com.antlersoft.analyzer.query.EmptyEnumeration;

import com.antlersoft.classwriter.TypeParse;

import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
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
									typeString.substring( 1, typeString.length()-1))));
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
		Enumeration result;
		if ( db.captureOptional( AnalyzerDB.OPTIONAL_TYPE_INFO))
		{
			result=((IndexAnalyzeDB)db).retrieveByIndex( DBMethod.RETURN_TYPE_INDEX, new ObjectRefKey( this));
		}
		else
			result=EmptyEnumeration.emptyEnumeration;
		
		return result;
	}
	
	public Enumeration getFields( AnalyzerDB db)
	{
		Enumeration result;
		if ( db.captureOptional( AnalyzerDB.OPTIONAL_TYPE_INFO))
		{
			result=((IndexAnalyzeDB)db).retrieveByIndex( DBField.FIELD_TYPE_INDEX, new ObjectRefKey( this));
		}
		else
			result=EmptyEnumeration.emptyEnumeration;
		
		return result;
	}
	
	public Enumeration getArguments( AnalyzerDB db)
	{
		Enumeration result;
		if ( db.captureOptional( AnalyzerDB.OPTIONAL_TYPE_INFO))
		{
			result=((IndexAnalyzeDB)db).retrieveByIndex( DBArgument.ARGUMENT_TYPE_INDEX, new ObjectRefKey( this));
		}
		else
			result=EmptyEnumeration.emptyEnumeration;
		
		return result;
	}
	
	public String toString()
	{
		String result;
		if ( isArrayRef())
		{
			result=_arrayReferencedType.getReferenced().toString()+"[]";
		}
		else if ( isClassRef())
		{
			result=_class.getReferenced().toString();
		}
		else
		{
			result=TypeStringMap.internalToUser( _builtInType);
		}
		
		return result;
	}
	
	static class TypeStringMap
	{
		private String internal;
		private String user;
		private static TypeStringMap[] typeMap= {
			new TypeStringMap( TypeParse.ARG_BOOLEAN, "boolean"),
			new TypeStringMap( TypeParse.ARG_BYTE, "byte"),
			new TypeStringMap( TypeParse.ARG_CHAR, "char"),
			new TypeStringMap( TypeParse.ARG_DOUBLE, "double"),
			new TypeStringMap( TypeParse.ARG_FLOAT, "float"),
			new TypeStringMap( TypeParse.ARG_INT, "int"),
			new TypeStringMap( TypeParse.ARG_LONG, "long"),
			new TypeStringMap( TypeParse.ARG_SHORT, "short"),
			new TypeStringMap( TypeParse.ARG_VOID, "void")
		};
		
		private TypeStringMap( String i, String u)
		{
			internal=i;
			user=u;
		}
		
		static String internalToUser( String s)
		{
			String result=null;
			
			for ( int i=0; i<typeMap.length; i++)
				if ( typeMap[i].internal.equals(s))
				{
					result=typeMap[i].user;
					break;
				}
			
			return result;
		}
		static String userToInternal( String s)
		{
			String result=null;
			
			for ( int i=0; i<typeMap.length; i++)
				if ( typeMap[i].user.equals(s))
				{
					result=typeMap[i].internal;
					break;
				}
			
			return result;
		}
	}
}
