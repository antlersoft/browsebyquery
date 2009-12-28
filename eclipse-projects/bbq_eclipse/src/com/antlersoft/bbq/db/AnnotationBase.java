/**
 * Copyright (c) 2009 Michael A. MacDonald
 */
package com.antlersoft.bbq.db;

import com.antlersoft.odb.ObjectRef;

/**
 * @author Michael A. MacDonald
 *
 */
public interface AnnotationBase {
	public DBClassBase getAnnotationClass();
	
	public DBAnnotatable getAnnotatedObject();

	public ObjectRef<DBClassBase> getAnnotationClassRef();
}
