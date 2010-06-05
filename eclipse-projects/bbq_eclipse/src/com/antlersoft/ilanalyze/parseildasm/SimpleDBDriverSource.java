/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import com.antlersoft.ilanalyze.DBDriver;

/**
 * Implementation of DBDriverSource that always returns the same driver
 * 
 * @author Michael A. MacDonald
 *
 */
class SimpleDBDriverSource implements DBDriverSource {
	
	private DBDriver driver;
	
	public SimpleDBDriverSource(DBDriver driver)
	{
		this.driver = driver;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.parseildasm.DBDriverSource#get()
	 */
	@Override
	public DBDriver get() {
		return driver;
	}

}
