/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.bbq.db;

import com.antlersoft.odb.Persistent;

/**
 * An object in the analyzed system that can have annotations/custom attributes
 * associated with it
 * @author Michael A. MacDonald
 *
 */
public interface DBAnnotatable extends Persistent {
	/**
	 * Retrieves the collection of annotations/custom attributes
	 * associated with this object
	 * @return Collection of annotation objects
	 */
	public AnnotationCollection getAnnotationCollection();
}
