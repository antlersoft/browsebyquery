/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer;

/**
 * Implement this method if you are a class with objects associated with a DBType,
 * a type in the analyzed system.
 * @author Michael A. MacDonald
 *
 */
public interface HasDBType {
	/**
	 * Return the type in the analyzed system associated with this object
	 * @param db
	 * @return
	 */
	public DBType getDBType( IndexAnalyzeDB db);
}
