/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

/**
 * A type that is normally a built-in value type
 * @author Michael A. MacDonald
 *
 */
public class BuiltinType extends BasicType {

	/**
	 * @param name
	 */
	public BuiltinType(String name) {
		super(name);
	}


	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.ReadType#isClassType()
	 */
	public boolean isClassType() {
		return false;
	}
}
