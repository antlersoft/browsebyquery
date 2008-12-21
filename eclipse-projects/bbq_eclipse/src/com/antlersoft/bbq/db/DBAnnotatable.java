/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.bbq.db;

import com.antlersoft.odb.Persistent;

/**
 * @author Michael A. MacDonald
 *
 */
public interface DBAnnotatable extends Persistent {
	public AnnotationCollection getAnnotationCollection();
}
