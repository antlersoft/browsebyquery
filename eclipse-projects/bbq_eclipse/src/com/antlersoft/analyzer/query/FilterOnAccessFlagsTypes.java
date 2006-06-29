/**
 * Copyright (c) 2006 Michael A. MacDonald
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.AccessFlags;
import com.antlersoft.parser.RuleActionException;

/**
 * @author mike
 *
 */
abstract class FilterOnAccessFlagsTypes extends Filter {
	FilterOnAccessFlagsTypes()
	{
		super(null);
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
}
