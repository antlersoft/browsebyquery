/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.DBArgument;
import com.antlersoft.analyzer.DBClass;
import com.antlersoft.analyzer.DBField;
import com.antlersoft.analyzer.DBMethod;

/**
 * Returns a Class for an object name that might appear in a query, to support cast
 * functionality
 * @author Michael A. MacDonald
 *
 */
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
