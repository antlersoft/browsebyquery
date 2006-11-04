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

import com.antlersoft.analyzer.DBClass;
import com.antlersoft.analyzer.DBMethod;
import com.antlersoft.analyzer.DBField;
import com.antlersoft.analyzer.DBStringConstant;
import com.antlersoft.analyzer.DBType;

class MatchFilter extends Filter
{
	private String match;

	MatchFilter( String toMatch)
	{
		super( null);
		match=toMatch;
	}

	public boolean include( Object toMatch)
		throws Exception
	{
		Class c=getFilterClass();

		if ( c==null)
			throw new Exception( "Unbound match filter");
		String name=null;
		if ( c==DBClass.class)
		{
			name=((DBClass)toMatch).getName();
		}
		else if ( c==DBMethod.class)
		{
			name=((DBMethod)toMatch).getName();
		}
		else if ( c==DBField.class)
		{
			name=((DBField)toMatch).getName();
		}
        else if ( c==DBStringConstant.class || c==DBType.class)
        {
            name=((DBStringConstant)toMatch).toString();
        }
		else
			throw new Exception( "Match filter bound incorrect class "+c.getName());

		return name.indexOf( match)!= -1;
	}

	public void lateBind( Class c)
	{
		_filterClass=c;
	}
}