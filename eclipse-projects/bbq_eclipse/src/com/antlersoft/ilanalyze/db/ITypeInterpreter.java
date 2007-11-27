/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.ilanalyze.*;

/**
 * Object that knows how to extract useful information from ReadType objects
 * @author Michael A. MacDonald
 *
 */
interface ITypeInterpreter {
	
	static class TIException extends Exception
	{
		TIException( String msg)
		{
			super(msg);
		}
	}
	String getClassKey( ReadType type) throws TIException;
	String getTypeKey( ReadType type) throws TIException;
	DBType createType( ILDB db, String key, ReadType type) throws TIException;
	
	static class Factory
	{
		private Factory()
		{
			
		}
		
		private static Factory m_instance;
		
		private static ITypeInterpreter m_Parameterized=new ParameterizedInterpreter();
		private static ITypeInterpreter m_Builtin=new BuiltinInterpreter();
		private static ITypeInterpreter m_GenericArg=new GenericArgInterpreter();
		private static ITypeInterpreter m_Array=new ArrayInterpreter();
		private static ITypeInterpreter m_Basic=new BasicInterpreter();

		static Factory getInstance()
		{
			if ( m_instance==null)
				m_instance=new Factory();
			return m_instance;
		}
		
		ITypeInterpreter getTI( ReadType type)
		{
			ITypeInterpreter result=m_Basic;
			
			if ( type instanceof ParameterizedReadType)
				result=m_Parameterized;
			else if ( type instanceof BuiltinType)
				result=m_Builtin;
			else if ( type instanceof GenericArgRef)
				result=m_GenericArg;
			else if ( type instanceof ReadArray)
				result=m_Array;
			
			return result;
		}
		
		static String getClassKey( ReadType type) throws TIException
		{
			return getInstance().getTI(type).getClassKey(type);
		}
		
		static String getTypeKey( ReadType type) throws TIException
		{
			return getInstance().getTI(type).getTypeKey(type);
		}
		
		static class ParameterizedInterpreter implements ITypeInterpreter
		{
			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#getClassKey(com.antlersoft.ilanalyze.ReadType)
			 */
			public String getClassKey(ReadType type) throws TIException {
				return ((ParameterizedReadType)type).getSimpleName();
			}

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#getTypeKey(com.antlersoft.ilanalyze.ReadType)
			 */
			public String getTypeKey(ReadType type) throws TIException {
				return getClassKey( type);
			}

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#createType(com.antlersoft.ilanalyze.db.ILDB, java.lang.String, com.antlersoft.ilanalyze.ReadType)
			 */
			public DBType createType(ILDB db, String key, ReadType type) throws TIException {
				DBClass c=DBClass.get( db, key);
				return new DBType( key, c, null, c.toString());
			}
			
		}
		
		static class BuiltinInterpreter implements ITypeInterpreter
		{

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#getClassKey(com.antlersoft.ilanalyze.ReadType)
			 */
			public String getClassKey(ReadType type) throws TIException {
				String s=type.toString();
				if ( s.startsWith("int"))
				{
					return "System.I"+s.substring(1);
				}
				if ( s.startsWith( "uint"))
				{
					return "System.UI"+s.substring(2);
				}
				if ( s.equals( "float32"))
				{
					return "System.Single";
				}
				if ( s.equals( "float64"))
					return "System.Double";
				if ( s.equals("char"))
					return "System.Char";
				if ( s.equals("bool"))
					return "System.Boolean";
				if ( s.startsWith("native"))
					return "System.Int32";
				throw new TIException( "Can't get class key from built-in type: "+type.toString());
			}

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#getTypeKey(com.antlersoft.ilanalyze.ReadType)
			 */
			public String getTypeKey(ReadType type) throws TIException {
				return "}"+type.toString();
			}

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#createType(com.antlersoft.ilanalyze.db.ILDB, java.lang.String, com.antlersoft.ilanalyze.ReadType)
			 */
			public DBType createType(ILDB db, String key, ReadType type) throws TIException {
				return new DBType( key, null, null, type.toString());
			}
			
		}
		
		static class GenericArgInterpreter implements ITypeInterpreter {

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#getClassKey(com.antlersoft.ilanalyze.ReadType)
			 */
			public String getClassKey(ReadType type) throws TIException {
				return "?";
			}

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#getTypeKey(com.antlersoft.ilanalyze.ReadType)
			 */
			public String getTypeKey(ReadType type) throws TIException {
				return "?";
			}

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#createType(com.antlersoft.ilanalyze.db.ILDB, java.lang.String, com.antlersoft.ilanalyze.ReadType)
			 */
			public DBType createType(ILDB db, String key, ReadType type) throws TIException {
				return new DBType( key, null, null, key);
			}
			
		}
		
		static class ArrayInterpreter implements ITypeInterpreter {

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#getClassKey(com.antlersoft.ilanalyze.ReadType)
			 */
			public String getClassKey(ReadType type) throws TIException {
				return "System.Array";
			}

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#getTypeKey(com.antlersoft.ilanalyze.ReadType)
			 */
			public String getTypeKey(ReadType type) throws TIException {
				ReadType underlying=((ReadArray)type).getUnderlying();
				return Factory.getInstance().getTI( underlying).getTypeKey(underlying)+"[]";
			}

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#createType(com.antlersoft.ilanalyze.db.ILDB, java.lang.String, com.antlersoft.ilanalyze.ReadType)
			 */
			public DBType createType(ILDB db, String key, ReadType type) throws TIException {
				DBType underlying=DBType.get( db, ((ReadArray)type).getUnderlying());
				return new DBType( key, null, underlying, underlying+"[]"); 
			}
			
		}
		
		static class BasicInterpreter implements ITypeInterpreter {

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#getClassKey(com.antlersoft.ilanalyze.ReadType)
			 */
			public String getClassKey(ReadType type) throws TIException {
				return type.toString();
			}

			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#getTypeKey(com.antlersoft.ilanalyze.ReadType)
			 */
			public String getTypeKey(ReadType type) throws TIException {
				return type.toString();
			}
			
			/* (non-Javadoc)
			 * @see com.antlersoft.ilanalyze.db.ITypeInterpreter#createType(com.antlersoft.ilanalyze.db.ILDB, java.lang.String, com.antlersoft.ilanalyze.ReadType)
			 */
			public DBType createType(ILDB db, String key, ReadType type) throws TIException {
				DBClass c=DBClass.get( db, key);
				return new DBType( key, c, null, c.toString());
			}
		}
	}
}
