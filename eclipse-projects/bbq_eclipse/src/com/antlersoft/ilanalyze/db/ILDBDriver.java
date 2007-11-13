/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.antlersoft.ilanalyze.DBDriver;
import com.antlersoft.ilanalyze.LoggingDBDriver;
import com.antlersoft.ilanalyze.ReadType;
import com.antlersoft.ilanalyze.Signature;
import com.antlersoft.ilanalyze.ReadArg;

import com.antlersoft.odb.ObjectKeyHashSet;
import com.antlersoft.odb.ObjectRef;

/**
 * Adds data read from assembly files to the database
 * @author Michael A. MacDonald
 *
 */
public class ILDBDriver implements DBDriver {
	
	private ILDB m_db;
	private DBAssembly m_current_assembly;
	private DBModule m_current_module;
	private DBSourceFile m_current_source_file;
	private int m_current_line;
	/** List representing current stack of DBNamespace objects */
	private ArrayList m_namespace_stack;
	/** List representing current stack of DBClass objects */
	private ArrayList m_class_stack;
	/** Class that manages updating the contents of a method */
	private DBMethod.MethodUpdater m_method_updater;
	/** How many classes processed since last commit */
	private int m_class_count;
	
	public ILDBDriver( ILDB db)
	{
		m_db=db;
		m_namespace_stack=new ArrayList();
		m_class_stack=new ArrayList();
		m_class_count=0;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addFieldReference(com.antlersoft.ilanalyze.ReadType, com.antlersoft.ilanalyze.ReadType, java.lang.String, boolean)
	 */
	public void addFieldReference(ReadType containing_type,
			ReadType field_type, String name, boolean is_write) {
		if ( m_method_updater!=null)
		{
			try
			{
				m_method_updater.addFieldReference(getCurrentClass(containing_type).getField(name, DBType.get(m_db, field_type)), is_write, m_current_source_file, m_current_line);
			}
			catch ( ITypeInterpreter.TIException ti)
			{
				LoggingDBDriver.logger.info(ti.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addMethodCall(com.antlersoft.ilanalyze.ReadType, java.lang.String, java.util.List, com.antlersoft.ilanalyze.Signature)
	 */
	public void addMethodCall(ReadType containing_type, String method_name,
			List genericArgs, Signature sig) {
		if ( m_method_updater!=null)
		{
			try
			{
				m_method_updater.addCall(getCurrentClass(containing_type).getMethod(method_name, DBType.get(m_db, sig.getReturnType()), getSignatureKey( sig)), m_current_source_file, m_current_line);
			}
			catch ( ITypeInterpreter.TIException ti)
			{
				LoggingDBDriver.logger.info(ti.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addStringReference(java.lang.String)
	 */
	public void addStringReference(String referenced) {
		if ( m_method_updater!=null)
		{
			m_method_updater.addStringReference(DBString.get(m_db, referenced), m_current_source_file, m_current_line);
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endClass()
	 */
	public void endClass() {
		m_class_stack.remove( m_class_stack.size()-1);
		if ( ++m_class_count>1000)
		{
			m_db.commitAndRetain();
			m_class_count=0;
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endMethod()
	 */
	public void endMethod() {
		if ( m_method_updater!=null)
		{
			m_method_updater.methodDone();
			m_method_updater=null;
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endNamespace()
	 */
	public void endNamespace() {
		m_namespace_stack.remove( m_namespace_stack.size()-1);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#setFileAndLine(java.lang.String, int)
	 */
	public void setFileAndLine(String name, int line) {
		if ( name!=null && name.length()>0)
		{
			m_current_source_file=DBSourceFile.get(m_db, name);
		}
		m_current_line=line;
		if ( m_class_stack.size()>0)
		{
			((DBClass)m_class_stack.get(m_class_stack.size()-1)).setFileAndLine(m_current_source_file, line);
		}
		if ( m_method_updater!=null)
			m_method_updater.m_method.setFileAndLine(m_current_source_file, line);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startAssembly(java.lang.String)
	 */
	public void startAssembly(String name) {
		m_current_assembly=DBAssembly.get( m_db, name);
		m_current_source_file=null;
		m_current_line=0;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startClass(java.lang.String, java.util.List, int, com.antlersoft.ilanalyze.ReadType, java.util.List)
	 */
	public void startClass(String className, List genericParams,
			int properties, ReadType extendsType, List implementsList) {
		DBClass read_class=DBClass.get(m_db, className);
		read_class.setProperties(properties);
		read_class.setVisited( true);
		ObjectKeyHashSet base=new ObjectKeyHashSet();
		try
		{
			base.add( new ObjectRef( DBClass.get( m_db, ITypeInterpreter.Factory.getClassKey(extendsType))));
			for ( Iterator i=implementsList.iterator(); i.hasNext();)
			{
				base.add( new ObjectRef( DBClass.get( m_db, ITypeInterpreter.Factory.getClassKey((ReadType)i.next()))));
			}
		}
		catch ( ITypeInterpreter.TIException ti)
		{
			LoggingDBDriver.logger.info( ti.getMessage());
		}
		read_class.updateBaseClasses(base);
		read_class.setAssembly(m_current_assembly);
		read_class.setModule(m_current_module);
		m_class_stack.add( read_class);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startMethod(java.lang.String, java.util.List, com.antlersoft.ilanalyze.Signature, int)
	 */
	public void startMethod(String name, List genericParams,
			Signature signature, int properties) {
		assert( m_method_updater==null);
		try
		{
			m_method_updater=new DBMethod.MethodUpdater( m_db, getCurrentClass( null).getMethod( name, DBType.get( m_db, signature.getReturnType()), getSignatureKey( signature)));
			m_method_updater.updateArguments(signature);
			m_method_updater.m_method.setProperties(properties);
		}
		catch ( ITypeInterpreter.TIException ti)
		{
			LoggingDBDriver.logger.info( ti.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startModule(java.lang.String)
	 */
	public void startModule(String name) {
		m_current_module=DBModule.get(m_db, name);
		m_current_module.setAssembly( m_current_assembly);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startNamespace(java.lang.String)
	 */
	public void startNamespace(String name) {
		DBNamespace namespace=DBNamespace.get( m_db, name);
		m_namespace_stack.add( namespace);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addField(java.lang.String, com.antlersoft.ilanalyze.ReadType, int)
	 */
	public void addField(String name, ReadType type, int properties) {
		try
		{
			DBField field=getCurrentClass(null).getField( name, DBType.get( m_db, type));
			field.setProperties( properties);
			field.setFileAndLine(m_current_source_file, m_current_line);
		}
		catch ( ITypeInterpreter.TIException ti)
		{
			LoggingDBDriver.logger.info(ti.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endAnalyzedFile()
	 */
	public void endAnalyzedFile() {
		m_db.commitAndRetain();
		m_class_count=0;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startAnalyzedFile(java.lang.String)
	 */
	public void startAnalyzedFile(String file) {
		// TODO Auto-generated method stub
		
	}
	
	static private String NAMELESS_CLASS="`NAMELESS_CLASS`";
	
	/**
	 * Interpret a type and a name in the context of the current class
	 * and namespace stack to get a DBClass and the name within it.
	 * @author Michael A. MacDonald
	 *
	 */
	private DBClass getCurrentClass( ReadType type ) throws ITypeInterpreter.TIException
	{
		if ( type!=null)
		{
			return DBClass.get( m_db, ITypeInterpreter.Factory.getClassKey(type));
		}
		if ( m_class_stack.size()>0)
		{
			return (DBClass)m_class_stack.get( m_class_stack.size()-1);
		}
		String namespace="";
		if ( m_namespace_stack.size()>0)
		{
			namespace=((DBNamespace)m_namespace_stack.get( m_namespace_stack.size()-1)).toString();
		}
		return DBClass.get( m_db, namespace+"."+NAMELESS_CLASS);
	}
	
	private static String getSignatureKey( Signature sig) throws ITypeInterpreter.TIException
	{
		StringBuilder sb=new StringBuilder();
		for ( Iterator i=sig.getArguments().iterator(); i.hasNext();)
		{
			sb.append(',');
			ReadType t=((ReadArg)i.next()).getType();
			if ( t==null)
				sb.append("...");
			else
				sb.append( ITypeInterpreter.Factory.getTypeKey(t));
		}
		
		return sb.toString();
	}
}
