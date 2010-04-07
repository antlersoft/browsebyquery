/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.query;

import java.util.Enumeration;

import com.antlersoft.ilanalyze.db.DBClass;

import com.antlersoft.query.CountPreservingBoundFilter;
import com.antlersoft.query.DataSource;

/**
 * Tests a class to see if it is an Enum by looking for "System.Enum" base class
 * 
 * @author Michael A. MacDonald
 *
 */
class EnumFilter extends CountPreservingBoundFilter {

	public EnumFilter() {
		super(DBClass.class);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.CountPreservingFilter#getCountPreservingFilterValue(com.antlersoft.query.DataSource, java.lang.Object)
	 */
	@Override
	protected boolean getCountPreservingFilterValue(DataSource source,
			Object inputObject) {
		for ( Enumeration e = ((DBClass)inputObject).getBaseClasses(); e.hasMoreElements(); )
		{
			if (((DBClass)e.nextElement()).toString().equals("System.Enum"))
				return true;
		}
		
		return false;
	}

}
