/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.bbq.db;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Enumeration;

import com.antlersoft.odb.FromRefIteratorEnumeration;
import com.antlersoft.odb.ObjectRef;

import com.antlersoft.query.EmptyEnum;

/**
 * @author Michael A. MacDonald
 *
 */
public class AnnotationCollection implements Serializable {

	ArrayList<ObjectRef<DBAnnotationBase>> annotations;
	
 	public Enumeration getAnnotations()
	{
 		Enumeration result;
 		if ( annotations==null )
 		{
 			result=EmptyEnum.empty;
 		}
 		else
 		{
 			result = new FromRefIteratorEnumeration<DBAnnotationBase>(annotations.iterator());
 		}
 		return result;
	}
}
