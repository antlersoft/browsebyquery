/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.query;

import com.antlersoft.ilanalyze.db.HasProperties;

import com.antlersoft.query.BindException;
import com.antlersoft.query.CountPreservingFilter;
import com.antlersoft.query.DataSource;

/**
 * Filter that compares the properties associated with an object with a bitmap of tested properties
 * @author Michael A. MacDonald
 *
 */
class AccessFilter extends CountPreservingFilter {

	private int _mask;
	private int _compareValue;
	    
	/**
	 * @param mask Bits of properties to test for
	 * @param compareValue Properties & mask = compareValue for successful test; or if compare value is -1,
	 * that successful if Properties& mask != 0
	 */
	public AccessFilter( int mask, int compareValue) {
		_mask=mask;
		_compareValue=compareValue;
	}
	
	/**
	 * @param mask Bits of properties to test for; test is successful if properties contains all these ints
	 */
	public AccessFilter( int mask)
	{
		this( mask, mask);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.CountPreservingFilter#getCountPreservingFilterValue(com.antlersoft.query.DataSource, java.lang.Object)
	 */
	protected boolean getCountPreservingFilterValue(DataSource source,
			Object inputObject) {
	       if ( _compareValue!= -1)
	            return (((HasProperties)inputObject).getProperties() & _mask)==_compareValue;
	        else
	            return (((HasProperties)inputObject).getProperties() & _mask)!=0;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#appliesClass()
	 */
    public Class appliesClass()
    {
    	return _appliesClass;
    }

	/* (non-Javadoc)
	 * @see com.antlersoft.query.Bindable#lateBindApplies(java.lang.Class)
	 */
    public void lateBindApplies( Class new_applies)
    throws BindException
    {
        if ( ! HasProperties.class.isAssignableFrom( new_applies))
            throw new BindException( "Failed to bind: "+new_applies.getName()+".  Does not implement HasProperties");
		if ( _appliesClass==null)
		    _appliesClass=new_applies;
		else
		if ( _appliesClass.isAssignableFrom( new_applies))
		    _appliesClass=new_applies;
		else
		    throw new BindException( "Failed to bind: "+new_applies.getName()+".  Not compatible with "+_appliesClass.getName());
	}
	
	private Class _appliesClass;

}
