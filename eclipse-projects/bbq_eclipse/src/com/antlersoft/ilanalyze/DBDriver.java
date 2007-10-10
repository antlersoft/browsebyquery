/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

/**
 * Methods for collecting data while processing IL information
 * @author Michael A. MacDonald
 *
 */
public interface DBDriver {
	/**
	 * Introducing an assembly that is being read (not referenced)
	 * @param name
	 */
	public void startAssembly( String name);
	/**
	 * Introduce a module contained in the current assembly.
	 * @param name
	 */
	public void startModule( String name);
	/**
	 * Entering a namespace that is being read
	 * @param name
	 */
	public void startNamespace( String name);
	/**
	 * Start a class that is being read (not referenced).  The namespace
	 * parameter implies the namespace portion of the class name--the containing
	 * namespace (if any) is ignored.
	 * If the namespace is null parameter is null, uses the containing namespace.
	 * There is always a containing namespace; it may be the "" (empty name) namespace.
	 * @param namespace
	 * @param className
	 */
	public void startClass( String namespace, String className);
	/**
	 * Add a method to the current class.  The method must be being read,
	 * not referenced.
	 */
	public void startMethod( String name, String signature);
}