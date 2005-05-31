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

import java.util.Enumeration;
import java.util.Hashtable;

import com.antlersoft.analyzer.AnalyzerDB;

abstract class UniqueTransform extends TransformImpl
{
    private Hashtable ht;
    UniqueTransform( Class fromClass, Class toClass)
    {
		super( fromClass, toClass);
    }

	public void startEvaluation( AnalyzerDB db)
	{
		ht=new Hashtable();
	}

    public Enumeration transform( Object toTransform)
        throws Exception
    {
		Object c=uniqueTransform( toTransform);
		if ( c instanceof Enumeration)
		{
			Enumeration enum=(Enumeration)c;
			while ( enum.hasMoreElements())
			{
				Object e=enum.nextElement();
				ht.put( e, e);
			}
		}
		else
			ht.put( c, c);
        return EmptyEnumeration.emptyEnumeration;
    }

	abstract public Object uniqueTransform( Object toTransform)
		throws Exception;

	public Enumeration finishEvaluation()
	{
		Enumeration result= ht.keys();
		ht=null;
		return result;
	}
}
