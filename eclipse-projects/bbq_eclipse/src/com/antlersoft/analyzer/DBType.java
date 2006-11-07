/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

import java.util.Enumeration;

import com.antlersoft.query.EmptyEnum;

import com.antlersoft.classwriter.TypeParse;

import com.antlersoft.odb.KeyGenerator;
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
	public static final String TYPE_CLASS_INDEX="TypeClass";
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
	
	/**
	 * 
	 * @return The single character internal code for the type
	 */
	public String getBuiltInType()
	{
		return _builtInType;
	}
	
	/**
	 * 
	 * @return The internal representation of the type, which is also the primary key
	 */
	public String getTypeString()
	{
		return _typeString;
	}
	
	/**
	 * Return the DBType object for a class if it exists in the database
	 * @param db -- database (must be IndexAnalyzeDB to retrieve a type)
	 * @param cl -- class to find within a database
	 * @return The DBType representing the class type, or null if no such type is referenced
	 */
	public static DBType getFromClass( AnalyzerDB db, DBClass cl)
	{
		DBType result=null;
		if ( db.captureOptional( AnalyzerDB.OPTIONAL_TYPE_INFO))
		{
			Enumeration e=((IndexAnalyzeDB)db).retrieveByIndex( DBType.TYPE_CLASS_INDEX,
					new ObjectRefKey( cl));
			if ( e.hasMoreElements())
				result=(DBType)e.nextElement(); 
		}
		
		return result;
	}
	
	public Enumeration getReturningMethods( AnalyzerDB db)
	{
		Enumeration result;
		if ( db.captureOptional( AnalyzerDB.OPTIONAL_TYPE_INFO))
		{
			result=((IndexAnalyzeDB)db).retrieveByIndex( DBMethod.RETURN_TYPE_INDEX, new ObjectRefKey( this));
		}
		else
			result=EmptyEnum.empty;
		
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
			result=EmptyEnum.empty;
		
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
			result=EmptyEnum.empty;
		
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
	
	/**
	 * Maps names for Java built-in types to the characters found in the type strings in
	 * class files
	 * @author Michael MacDonald
	 *
	 */
	public static class TypeStringMap
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
		
		public static String internalToUser( String s)
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
		
		/**
		 * 
		 * @param s user representation of a built-in type
		 * @return internal java type character, or null if type name doesn't map to a built-in type
		 */
		public static String userToInternal( String s)
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

	static class TypeClassKeyGenerator implements KeyGenerator
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 173926813542534195L;

		public Comparable generateKey( Object obj)
		{
			DBType t=(DBType)obj;
			return new ObjectRefKey( t._class);
		}
	}
}
