/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

/**
 * A class that maintains properties flags in an int
 * @author Michael A. MacDonald
 *
 */
public interface HasProperties {
	/**
	 * 
	 * @return An int with the various flag bits defined in DBDriver or'd together
	 */
	public int getProperties();
}
