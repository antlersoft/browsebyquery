/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.List;

/**
 * Methods for collecting data while processing IL information
 * @author Michael A. MacDonald
 *
 */
public interface DBDriver {
	public final static int IS_PRIVATE=1;
	public final static int IS_PUBLIC=2;
	public final static int IS_ASSEMBLY=4;
	public final static int IS_FAMILY=8;
	public final static int IS_ENUM=16;
	public final static int IS_INTERFACE=32;
	public final static int IS_ABSTRACT=64;
	public final static int IS_SERIALIZABLE=128;
	public final static int IS_FAMANDASSEM=256;
	public final static int IS_FAMORASSEM=512;
	
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
	 * @param className This may be a slashed name to indicate an inner class (components
	 * will be separated by slashes, rather than periods)
	 * @param genericParams List of ReadGenericParam objects.  If the list is null or empty, create
	 * a regular (not a parameterized) class
	 * @param properties Visibility/etc bits or'd together
	 * @param extendsType Type that this class extends (must exists; object if nothing else)
	 * @param implementsList Interfaces that this class implements (may be empty list)
	 */
	public void startClass( String className, List genericParams, int properties, ReadType extendsType, List implementsList);
	
	/**
	 * Pop the implicit stack of nested classes
	 *
	 */
	public void endClass();
	
	/**
	 * Add a method to the current class.  The method must be being read,
	 * not referenced.  A method outside any class is in the default (namespace level)
	 * class.
	 * @param name Name
	 * @param genericParams List of ReadGenericParam objects.  If the list is null or empty, create
	 * a regular (not a parameterized) method
	 * @param signature
	 * @param properties Visibility/etc bits or'd together
	 */
	public void startMethod( String name, List genericParams, Signature signature, int properties);
	
	/**
	 * Set file and line number.  If the file name is null or empty,
	 * the existing file name is not changed.
	 * @param name Name of file
	 * @param line Line number
	 */
	public void setFileAndLine( String name, int line);
	
	/**
	 * Add a reference to a string; the reference occurs in the current method
	 * @param referenced String being referenced
	 */
	public void addStringReference( String referenced);
	
	/**
	 * Add a method call to the current method
	 * @param containing_type Type that contains the called method
	 * @param method_name
	 * @param sig Signature of methd
	 */
	public void addMethodCall( ReadType containing_type, String method_name, Signature sig);
	
	/**
	 * Add a field reference to the current method
	 * @param containing_type Type that contains the referenced field
	 * @param field_type
	 * @param name
	 * @param is_write True if the reference is a write reference
	 */
	public void addFieldReference( ReadType containing_type, ReadType field_type, String name, boolean is_write);
}
