package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.AccessFlags;

import com.antlersoft.parser.RuleActionException;

/**
 * <p>Title: BBQ Tool</p>
 * <p>Description: BBQ Integration with JBuilder</p>
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
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