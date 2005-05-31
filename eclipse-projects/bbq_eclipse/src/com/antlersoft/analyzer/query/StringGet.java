
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
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
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.analyzer.query;

import java.util.Enumeration;
import com.antlersoft.analyzer.AnalyzerDB;
import com.antlersoft.analyzer.DBStringConstant;

class StringGet extends SetExpression
{
    private String toGet;

    StringGet( String _toGet)
    {
        super( DBStringConstant.class);
        toGet=_toGet;
    }

    public Enumeration execute(AnalyzerDB parm1) throws java.lang.Exception
    {
        Enumeration result;
        Object found=parm1.findWithKey( DBStringConstant.class.getName(),
            toGet);
        if ( found!=null)
            return QueryParser.enumFromObject( found);
        else
            return EmptyEnumeration.emptyEnumeration;
    }
}