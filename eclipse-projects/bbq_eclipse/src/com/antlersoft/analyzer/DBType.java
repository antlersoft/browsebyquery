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
	public static final String TYPE_KEY_INDEX = "TYPE_KEY_INDEX";
	public static final String TYPE_CLASS_INDEX="TypeClass";
	private ObjectRef<DBClass> _class;
	private ObjectRef<DBType> _arrayReferencedType;
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

	private DBType( String typeString, IndexAnalyzeDB db)
	throws Exception
	{
		_typeString=typeString;
		String t=TypeParse.parseFieldType( typeString);
		if ( t==TypeParse.ARG_ARRAYREF)
		{
			_arrayReferencedType=new ObjectRef<DBType>( getWithTypeKey( typeString.substring( 1), db));
		}
		else if ( t==TypeParse.ARG_OBJREF)
		{
			_class=new ObjectRef<DBClass>(
					DBClass.getByInternalName(typeString.substring( 1, typeString.length()-1), db));
		}
		_builtInType=t;
		_persistentImpl=new PersistentImpl();
		ObjectDB.makePersistent( this);
	}
	
	public static DBType getWithTypeKey( String typeKey, IndexAnalyzeDB db)
	throws Exception
	{
		DBType result=(DBType)db.findWithIndex( TYPE_KEY_INDEX, typeKey);
		if ( result==null)
		{
			result=new DBType( typeKey, db);
		}
		return result;
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
		return isClassRef() ? _class.getReferenced() : null;
	}
	
	public DBType getArrayReferencedType()
	{
		return isArrayRef() ? _arrayReferencedType.getReferenced() : null;
	}
	
	public DBType getArrayType( IndexAnalyzeDB db)
	throws Exception
	{
		return getWithTypeKey( "["+_typeString, db);
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
	public static DBType getFromClass( IndexAnalyzeDB db, DBClass cl)
	{
		try
		{
			return (DBType)db.findWithIndex(DBType.TYPE_CLASS_INDEX, new ObjectRefKey(cl));
		}
		catch ( Exception e)
		{
			
		}
		return null;
	}
	
	public Enumeration getReturningMethods( IndexAnalyzeDB db)
	{
		return db.retrieveByIndex( DBMethod.RETURN_TYPE_INDEX, new ObjectRefKey( this));
	}
	
	public Enumeration getFields( IndexAnalyzeDB db)
	{
		return db.retrieveByIndex( DBField.FIELD_TYPE_INDEX, new ObjectRefKey( this));
	}
	
	public Enumeration getArguments( IndexAnalyzeDB db)
	{
		return db.retrieveByIndex( DBArgument.ARGUMENT_TYPE_INDEX, new ObjectRefKey( this));
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
		
		public static String descriptorToUser( CharSequence descriptor)
		{
			int array_count=0;
			for ( ; descriptor.charAt(array_count)=='['; ++array_count);
			String base_type;
			if ( descriptor.charAt(array_count)==TypeParse.ARG_OBJREF.charAt(0))
			{
				base_type=TypeParse.convertFromInternalClassName(descriptor.subSequence(array_count+1, descriptor.length()-1).toString());
				if ( base_type.indexOf("java.lang")==0)
					base_type=base_type.substring(10);
			}
			else
				base_type=internalToUser(descriptor.subSequence(array_count, array_count+1).toString());
			
			if ( array_count==0)
				return base_type;
			
			StringBuilder sb=new StringBuilder( base_type.length()+2*array_count);
			sb.append(base_type);
			for ( ; array_count>0; --array_count)
				sb.append( "[]");
			return sb.toString();
		}
		
		/**
		 * Convert a built-in type code
		 * @param s
		 * @return
		 */
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
		 * Return an index key based on the class, or null if this is not a reference type
		 */
		private static final long serialVersionUID = 173926813542534195L;

		public Comparable generateKey( Object obj)
		{
			DBType t=(DBType)obj;
			return new ObjectRefKey( t._class);
		}
	}
	
	/**
	 * Return an index key based on the internal type string
	 * @author Michael A. MacDonald
	 *
	 */
	static class TypeKeyGenerator implements KeyGenerator
	{
		public Comparable generateKey( Object obj)
		{
			DBType t=(DBType)obj;
			return t._typeString;
		}
	}
}
