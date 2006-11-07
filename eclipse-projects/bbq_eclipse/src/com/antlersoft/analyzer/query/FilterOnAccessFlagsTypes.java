/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.AccessFlags;
import com.antlersoft.query.CountPreservingFilter;
import com.antlersoft.query.BindException;

/**
 * @author mike
 *
 */
abstract class FilterOnAccessFlagsTypes extends CountPreservingFilter {
    public void lateBindApplies( Class new_applies)
    throws BindException
    {
        if ( ! AccessFlags.class.isAssignableFrom( new_applies))
            throw new BindException( "Failed to bind: "+new_applies.getName()+".  Does not implement AccessFlags");
        if ( m_filter_class==null)
            m_filter_class=new_applies;
        else
        if ( m_filter_class.isAssignableFrom( new_applies))
            m_filter_class=new_applies;
        else
            throw new BindException( "Failed to bind: "+new_applies.getName()+".  Not compatible with "+m_filter_class.getName());
    }
    
    public Class appliesClass()
    {
    	return m_filter_class;
    }
    
    private Class m_filter_class;
}
