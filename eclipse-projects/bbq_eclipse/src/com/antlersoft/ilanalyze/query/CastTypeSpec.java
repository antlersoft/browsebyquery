/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.query;

import com.antlersoft.ilanalyze.db.DBArgument;
import com.antlersoft.ilanalyze.db.DBClass;
import com.antlersoft.ilanalyze.db.DBField;
import com.antlersoft.ilanalyze.db.DBMethod;

/**
 * Returns a Class for an object name that might appear in a query, to support cast
 * functionality
 * @author Michael A. MacDonald
 *
 */
@SuppressWarnings("unchecked")
class CastTypeSpec {
	private static String[] names={ "arguments","classes","fields","methods" };
	private static Class[] classes={ DBArgument.class, DBClass.class, DBField.class, DBMethod.class };
	public static Class getClassFor( String f)
	{
		for ( int i=0; i<names.length; ++i)
			if ( names[i].equals(f))
				return classes[i];
		
		throw new IllegalArgumentException( "Class name not recognized "+f);
	}
}
