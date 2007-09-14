/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.query;

/**
 * Converts type of anything to string
 * @author Michael A. MacDonald
 *
 */
public class ToString extends CountPreservingValueExpression {
	
	public ToString()
	{
		super( String.class, null);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.CountPreservingValueExpression#transformSingleObject(com.antlersoft.query.DataSource, java.lang.Object)
	 */
	protected Object transformSingleObject(DataSource source,
			Object to_transform) {
		return to_transform.toString();
	}

}
