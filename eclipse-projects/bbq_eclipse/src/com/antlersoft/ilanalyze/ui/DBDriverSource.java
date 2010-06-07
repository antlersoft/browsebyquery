/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.ui;

import com.antlersoft.ilanalyze.DBDriver;
import com.antlersoft.ilanalyze.parseildasm.IldasmReader;

/**
 * Provides a DBDriver and IldasmReader when analyzing a set of files; may provide
 * different instances for different threads
 * 
 * @author Michael A. MacDonald
 *
 */
interface DBDriverSource {
	DBDriver getDBDriver();
	IldasmReader getReader();
}
