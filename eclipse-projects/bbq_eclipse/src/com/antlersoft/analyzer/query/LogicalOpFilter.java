/*
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
 */
package com.antlersoft.analyzer.query;

import com.antlersoft.parser.ReservedWord;
import com.antlersoft.parser.RuleActionException;

public class LogicalOpFilter extends Filter
{
    private QueryParserBase.ReservedWord _op;
    private Filter _f1;
    private Filter _f2;

    public LogicalOpFilter( QueryParserBase.ReservedWord op,
        Filter f1, Filter f2)
        throws RuleActionException
    {
		super( null);
        _op=op;
        _f1=f1;
        _f2=f2;
        setFilterClass();
    }

    public void lateBind( Class _newFilterClass)
        throws RuleActionException
    {
        _f1.lateBind( _newFilterClass);
        _f2.lateBind( _newFilterClass);
        setFilterClass();
    }

    protected boolean include( Object o1)
        throws Exception
    {
        if ( _op==QueryParser.or)
        {
            return _f1.isIncluded( o1) || _f2.isIncluded( o1);
        }
        return _f1.isIncluded( o1) && _f2.isIncluded( o1);
    }

    private void setFilterClass()
        throws RuleActionException
    {
        Class f1Class=_f1.getFilterClass();
        Class f2Class=_f2.getFilterClass();
        if ( f1Class==null)
        {
            if ( f2Class==null)
                 return;
            _f1.lateBind( f2Class);
            f1Class=_f1.getFilterClass();
        }
        if ( f2Class==null)
        {
            _f2.lateBind( f1Class);
            f2Class=_f2.getFilterClass();
        }
        _filterClass=TransformSet.commonSubType( f1Class, f2Class, false);
    }
}
