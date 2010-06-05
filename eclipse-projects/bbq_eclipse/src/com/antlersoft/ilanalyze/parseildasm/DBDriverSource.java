/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

import com.antlersoft.ilanalyze.DBDriver;

/**
 * Provides a DBDriver when analyzing a set of files; may provide
 * different DBDrivers for different threads
 * 
 * @author Michael A. MacDonald
 *
 */
interface DBDriverSource {
	DBDriver get();
}
