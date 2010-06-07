/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.ui;

import com.antlersoft.ilanalyze.DBDriver;
import com.antlersoft.ilanalyze.parseildasm.IldasmReader;

/**
 * Implementation of DBDriverSource that always returns the same driver
 * 
 * @author Michael A. MacDonald
 *
 */
class SimpleDBDriverSource implements DBDriverSource {
	
	private DBDriver driver;
	
	private IldasmReader reader;
	
	public SimpleDBDriverSource(DBDriver driver, IldasmReader reader)
	{
		this.driver = driver;
		this.reader = reader;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.parseildasm.DBDriverSource#get()
	 */
	@Override
	public DBDriver getDBDriver() {
		return driver;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.ui.DBDriverSource#getReader()
	 */
	@Override
	public IldasmReader getReader() {
		return reader;
	}

}
