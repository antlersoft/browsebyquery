/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.query.reflect;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Enumeration;

import com.antlersoft.query.*;

import com.antlersoft.util.IteratorEnumeration;

/**
 * Returns the public methods declared by or inherited by the input class objects
 * 
 * @author Michael A. MacDonald
 *
 */
public class MethodsTransform extends TransformImpl {

	/**
	 * 
	 */
	public MethodsTransform() {
		super( String.class, Class.class);
	}

	public Enumeration transformObject( DataSource source, Object toTransform)
	{
 		Method[] methods=((Class)toTransform).getMethods();
 		ArrayList result_list=new ArrayList( methods.length);
 		for ( int i=0; i<methods.length; ++i)
 		{
 			result_list.add( methods[i].getName());
 		}
 		return new IteratorEnumeration( result_list.iterator());
	}
}
