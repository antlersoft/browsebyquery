/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.logging.Logger;

/**
 * @author Michael A. MacDonald
 *
 */
public class LoggingDBDriver implements DBDriver {
	
	static public Logger logger=Logger.getLogger(LoggingDBDriver.class.getName());
	
	private DBDriver m_nested;
	
	public LoggingDBDriver()
	{
		m_nested=null;
	}
	
	public LoggingDBDriver( DBDriver nested)
	{
		m_nested=nested;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addFieldReference(com.antlersoft.ilanalyze.ReadType, com.antlersoft.ilanalyze.ReadType, java.lang.String, boolean)
	 */
	public void addFieldReference(ReadType containing_type,
			ReadType field_type, String name, boolean is_write) {
		StringBuilder sb=new StringBuilder();
		if ( is_write)
			sb.append( "Write");
		else
			sb.append( "Read");
		sb.append( " reference to ");
		if ( containing_type==null)
			sb.append( "(containing type)");
		else
			sb.append( containing_type.toString());
		sb.append('.');
		sb.append( name);
		sb.append(" with type ");
		sb.append( field_type.toString());
		logger.fine( sb.toString());
		if ( m_nested!=null)
			m_nested.addFieldReference(containing_type, field_type, name, is_write);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addMethodCall(com.antlersoft.ilanalyze.ReadType, java.lang.String, com.antlersoft.ilanalyze.Signature)
	 */
	public void addMethodCall(ReadType containing_type, String method_name, List genericArgs,
			Signature sig) {
		StringBuilder sb=new StringBuilder("Call to ");
		sb.append( containing_type==null ? "(containing type)" : containing_type.toString());
		sb.append( '.');
		sb.append( method_name);
		formatGenericParams( sb, genericArgs);
		sb.append( sig.toString());
		logger.fine( sb.toString());
		if ( m_nested!=null)
			m_nested.addMethodCall(containing_type, method_name, genericArgs, sig);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addStringReference(java.lang.String)
	 */
	public void addStringReference(String referenced) {
		logger.fine( "Reference to \""+referenced+"\"");
		if ( m_nested!=null)
			m_nested.addStringReference(referenced);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endClass()
	 */
	public void endClass() {
		logger.fine( "End class");
		if ( m_nested!=null)
			m_nested.endClass();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#setFileAndLine(java.lang.String, int)
	 */
	public void setFileAndLine(String name, int line) {
		logger.fine( "File: "+name+" line: "+line);
		if ( m_nested!=null)
			m_nested.setFileAndLine(name, line);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startAssembly(java.lang.String)
	 */
	public void startAssembly(String name) {
		logger.fine( "Start assembly: "+name);
		if ( m_nested!=null)
			m_nested.startAssembly(name);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startClass(java.lang.String, java.util.List, int, com.antlersoft.ilanalyze.ReadType, java.util.List)
	 */
	public void startClass(String className, List genericParams,
			int properties, ReadType extendsType, List implementsList) {
		StringBuilder sb=new StringBuilder();
		sb.append( "Start class: ");
		sb.append( className);
		sb.append(' ');
		sb.append( properties);
		sb.append(' ');
		formatGenericParams( sb, genericParams);
		sb.append( " extends ");
		sb.append( extendsType.toString());
		if ( implementsList.size()>0)
		{
			sb.append( " implements ");
			formatList( sb, implementsList);
		}
		logger.fine( sb.toString());
		if ( m_nested!=null)
			m_nested.startClass(className, genericParams, properties, extendsType, implementsList);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startMethod(java.lang.String, java.util.List, com.antlersoft.ilanalyze.Signature, int)
	 */
	public void startMethod(String name, List genericParams,
			Signature signature, int properties) {
		StringBuilder sb=new StringBuilder();
		sb.append( "Start method: ");
		sb.append( name);
		sb.append(' ');
		sb.append( properties);
		sb.append(' ');
		formatGenericParams( sb, genericParams);
		sb.append( signature.toString());
		logger.fine( sb.toString());
		if ( m_nested!=null)
			m_nested.startMethod(name, genericParams, signature, properties);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startModule(java.lang.String)
	 */
	public void startModule(String name) {
		logger.fine( "Start module: "+name);
		if ( m_nested!=null)
			m_nested.startModule(name);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startNamespace(java.lang.String)
	 */
	public void startNamespace(String name) {
		logger.fine( "Start namespace: "+name);
		if ( m_nested!=null)
			m_nested.startNamespace(name);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endMethod()
	 */
	public void endMethod() {
		logger.fine( "End method");
		if ( m_nested!=null)
			m_nested.endMethod();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endNamespace()
	 */
	public void endNamespace() {
		logger.fine( "End namespace");
		if ( m_nested!=null)
		{
			m_nested.endNamespace();
		}
	}


	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endAnalyzedFile()
	 */
	public void endAnalyzedFile() {
		logger.fine( "End analyzed file");
		if ( m_nested!=null)
			m_nested.endAnalyzedFile();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startAnalyzedFile(java.lang.String)
	 */
	public void startAnalyzedFile(String file) {
		logger.fine( "Start analyzed file "+file);
		if ( m_nested!=null)
			m_nested.startAnalyzedFile(file);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addField(java.lang.String, com.antlersoft.ilanalyze.ReadType, int)
	 */
	public void addField(String name, ReadType type, int properties) {
		logger.fine( "Add field "+type.toString()+" "+name+" "+properties);
		if ( m_nested!=null)
		{
			m_nested.addField(name, type, properties);
		}
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addCustomAttribute(com.antlersoft.ilanalyze.ReadType, com.antlersoft.ilanalyze.Signature, byte[])
	 */
	public void addCustomAttribute(ReadType containing_type, Signature sig, byte[] data, String string_data) {
		logger.fine( (containing_type==null ? "" : containing_type.toString()+"::")+".ctor"+(sig.toString()+(string_data==null ? "" : string_data)));
		if ( m_nested!=null)
			m_nested.addCustomAttribute(containing_type, sig, data, string_data);
	}

	static private void formatGenericParams( StringBuilder sb, List genericParams)
	{
		if ( genericParams!=null && genericParams.size()>0)
		{
			sb.append('<');
			formatList( sb, genericParams);
			sb.append('>');
		}
	}
	
	static public void formatList( StringBuilder sb, Collection l)
	{
		for ( Iterator i=l.iterator(); i.hasNext();)
		{
			sb.append( i.next().toString());
			if ( i.hasNext())
				sb.append(',');
		}
		
	}
}
