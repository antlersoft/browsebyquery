/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.db;

import com.antlersoft.odb.KeyGenerator;

/**
 * Key generator for classes where toString generates an appropriate key
 * @author Michael A. MacDonald
 *
 */
final class StringKeyGenerator implements KeyGenerator {

		public Comparable generateKey(Object o1) {
			return o1.toString();
		}
}
