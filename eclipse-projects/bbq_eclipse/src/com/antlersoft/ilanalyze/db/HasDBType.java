/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

/**
 * @author Michael A. MacDonald
 *
 */
public interface HasDBType {
	/**
	 * Gets the DBType that represents the type of this object
	 * @param db ILDB holding the analyzed system
	 * @return Type associated witht his object
	 */
	public DBType getDBType( ILDB db);
}
