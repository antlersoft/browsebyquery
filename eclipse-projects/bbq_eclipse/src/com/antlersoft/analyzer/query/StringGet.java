
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

import com.antlersoft.analyzer.DBStringConstant;
import com.antlersoft.analyzer.IndexAnalyzeDB;

import com.antlersoft.query.CountPreservingValueExpression;
import com.antlersoft.query.DataSource;

class StringGet extends CountPreservingValueExpression
{
    StringGet()
    {
        super( DBStringConstant.class, String.class);
    }

    protected Object transformSingleObject(DataSource source, Object inputObject)
    {
  		return ((IndexAnalyzeDB)source).findWithIndex( DBStringConstant.STRING_INDEX, (String)inputObject);
    }
}