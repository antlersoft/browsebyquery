/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.parser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Specialization of a list with convenience methods for working with the value
 * stack in rule actions.
 * @author Michael A. MacDonald
 *
 */
public class ValueStack extends ArrayList {

	/**
	 * Default capacity
	 */
	public ValueStack() {
	}

	/**
	 * Construct with a specific capacity
	 * @param initialCapacity
	 */
	public ValueStack(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Construct copying an existing collection
	 * @param arg0
	 */
	public ValueStack(Collection arg0) {
		super(arg0);
	}
	
	/**
	 * 
	 * @return Object at the top of the stack
	 */
	public final Object o_0()
	{
		return get(size()-1);
	}
	
	/**
	 * 
	 * @return Object 1 deep in the stack
	 */
	public final Object o_1()
	{
		return get(size()-2);
	}

	/**
	 * 
	 * @return Object 2 deep in the stack
	 */
	public final Object o_2()
	{
		return get(size()-3);
	}
	
	/**
	 * Get object at arbitrary stack depth
	 * @param n How deep in the stack (top is 0)
	 * @return Object at nth depth
	 */
	public final Object o_n( int n)
	{
		return get(size()-(n+1));
	}

	/**
	 * Returns the string at the top of the stack. 
	 */
	public final String s_0()
	{
		return (String)get( size()-1);
	}

	/**
	 * 
	 * @return String 1 deep in stack
	 */
	public final String s_1()
	{
		return (String)get( size()-2);
	}
	
	/**
	 * 
	 * @return String 2 deep in stack
	 */
	public final String s_2()
	{
		return (String)get( size()-3);
	}

	/**
	 * Get String at arbitrary stack depth
	 * @param n How deep in the stack (top is 0)
	 * @return String at nth depth
	 */
	public final String s_n( int n)
	{
		return (String)get(size()-(n+1));
	}

	/**
	 * Returns the int at the top of the stack. 
	 */
	public final int i_0()
	{
		return ((Integer)get( size()-1)).intValue();
	}

	/**
	 * 
	 * @return int 1 deep in stack
	 */
	public final int i_1()
	{
		return ((Integer)get( size()-2)).intValue();
	}
	
	/**
	 * 
	 * @return int 2 deep in stack
	 */
	public final int i_2()
	{
		return ((Integer)get( size()-3)).intValue();
	}

	/**
	 * Get int at arbitrary stack depth
	 * @param n How deep in the stack (top is 0)
	 * @return int at nth depth
	 */
	public final int i_n( int n)
	{
		return ((Integer)get( size()-(n+1))).intValue();
	}

}
