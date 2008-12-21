/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.query;

import java.util.Enumeration;

/**
 * Transform that acts as a filter guaranteeing only
 * input objects with a certain type make it to the output.
 * 
 * @author Michael A. MacDonald
 *
 */
public class Cast extends TransformImpl {

	/**
	 * @param result Output type
	 */
	public Cast(Class result) {
		super(result, Object.class);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Transform#transformObject(com.antlersoft.query.DataSource, java.lang.Object)
	 */
	@Override
	public Enumeration transformObject(DataSource source, Object to_transform) {
		if ( resultClass().isAssignableFrom(to_transform.getClass()))
			return new SingleEnum(to_transform);

		return EmptyEnum.empty;
	}

}
