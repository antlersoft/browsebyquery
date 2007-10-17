/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.List;

import java.util.logging.Logger;

/**
 * @author Michael A. MacDonald
 *
 */
public class LoggingDBDriver implements DBDriver {
	
	Logger logger=Logger.getLogger(LoggingDBDriver.class.getName());

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addFieldReference(com.antlersoft.ilanalyze.ReadType, com.antlersoft.ilanalyze.ReadType, java.lang.String, boolean)
	 */
	public void addFieldReference(ReadType containing_type,
			ReadType field_type, String name, boolean is_write) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addMethodCall(com.antlersoft.ilanalyze.ReadType, java.lang.String, com.antlersoft.ilanalyze.Signature)
	 */
	public void addMethodCall(ReadType containing_type, String method_name,
			Signature sig) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#addStringReference(java.lang.String)
	 */
	public void addStringReference(String referenced) {
		logger.fine( "Reference to \""+referenced+"\"");
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#endClass()
	 */
	public void endClass() {
		logger.fine( "End class");
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#setFileAndLine(java.lang.String, int)
	 */
	public void setFileAndLine(String name, int line) {
		logger.fine( "File: "+name+" line: "+line);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startAssembly(java.lang.String)
	 */
	public void startAssembly(String name) {
		logger.fine( "Start assembly: "+name);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startClass(java.lang.String, java.util.List, int, com.antlersoft.ilanalyze.ReadType, java.util.List)
	 */
	public void startClass(String className, List genericParams,
			int properties, ReadType extendsType, List implementsList) {
		logger.fine( "Start class: "+className);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startMethod(java.lang.String, java.util.List, com.antlersoft.ilanalyze.Signature, int)
	 */
	public void startMethod(String name, List genericParams,
			Signature signature, int properties) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startModule(java.lang.String)
	 */
	public void startModule(String name) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.DBDriver#startNamespace(java.lang.String)
	 */
	public void startNamespace(String name) {
		// TODO Auto-generated method stub

	}

}
