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
        super(AccessFlags.class);
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
        if ( getFilterClass().isAssignableFrom( new_applies))
            _filterClass=new_applies;
        else
            throw new RuleActionException( "Failed to bind: "+new_applies.getName()+".  Does not implement AccessFlags");
    }
    protected boolean include(Object toCheck) throws java.lang.Exception {
        return (((AccessFlags)toCheck).getAccessFlags() & _mask) == _compareValue;
    }
}