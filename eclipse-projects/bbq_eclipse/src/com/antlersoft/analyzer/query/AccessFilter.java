package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.AccessFlags;

import com.antlersoft.parser.RuleActionException;

/**
 * <p>Title: BBQ Tool</p>
 * <p>Description: BBQ Integration with JBuilder</p>
 * <p>Copyright: Copyright (c) 2000-2003</p>
 * <p>Company: antlersoft</p>
 * @author Michael MacDonald
 * @version 1.0
 */

class AccessFilter extends Filter {
    private int _mask;
    private int _compareValue;

    AccessFilter( int mask,int compare_value)
    {
        super( null);
        _mask=mask;
        _compareValue=compare_value;
    }
    AccessFilter( int flag_value)
    {
        this( flag_value, flag_value);
    }
    public void lateBind( Class new_applies)
    throws RuleActionException
    {
        if ( ! AccessFlags.class.isAssignableFrom( new_applies))
            throw new RuleActionException( "Failed to bind: "+new_applies.getName()+".  Does not implement AccessFlags");
        if ( getFilterClass()==null)
            _filterClass=new_applies;
        else
        if ( getFilterClass().isAssignableFrom( new_applies))
            _filterClass=new_applies;
        else
            throw new RuleActionException( "Failed to bind: "+new_applies.getName()+".  Not compatible with "+getFilterClass().getName());
    }
    protected boolean include(Object toCheck) throws java.lang.Exception {
        if ( _compareValue!= -1)
            return (((AccessFlags)toCheck).getAccessFlags() & _mask)==_compareValue;
        else
            return (((AccessFlags)toCheck).getAccessFlags() & _mask)!=0;
    }
}