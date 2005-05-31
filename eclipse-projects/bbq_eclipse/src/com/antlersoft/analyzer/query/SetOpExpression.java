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

import java.util.HashSet;
import java.util.Collections;
import java.util.Enumeration;

import com.antlersoft.parser.ReservedWord;
import com.antlersoft.parser.RuleActionException;

import com.antlersoft.analyzer.AnalyzerDB;

class SetOpExpression extends SetExpression
{
	SetExpression a;
	SetExpression b;
	HashSet a_only;
	HashSet b_only;
	HashSet both;
	QueryParserBase.ReservedWord op;

	SetOpExpression( QueryParserBase.ReservedWord setOp, SetExpression s1, SetExpression s2)
		throws RuleActionException
	{
		super( TransformSet.commonSubType( s1.getSetClass(), s2.getSetClass(), true));
		a=s1;
		b=s2;
		op=setOp;
	}

	public Enumeration execute( AnalyzerDB db)
		throws Exception
	{
		a_only=new HashSet();
		b_only=new HashSet();
		both=new HashSet();
		processEnumeration( a.execute( db), a_only, b_only);
		processEnumeration( b.execute( db), b_only, a_only);
		Enumeration result;
		if ( op==QueryParser.union)
		{
			result=new QueryParser.ComboEnumeration( Collections.enumeration( a_only),
				new QueryParser.ComboEnumeration( Collections.enumeration( both),
				Collections.enumeration( b_only)));
		}
		else if ( op==QueryParser.intersection)
		{
			result=Collections.enumeration( both);
		}
		else if ( op==QueryParser.deintersection)
		{
			result=new QueryParser.ComboEnumeration( Collections.enumeration( a_only),
				Collections.enumeration( b_only));
		}
		// Final case is without
		else result=Collections.enumeration( a_only);

		// Release references to sets so they can be discarded when
		// we are done with the enumerations
		a_only=null;
		b_only=null;
		both=null;

		return result;
	}
	private void processEnumeration( Enumeration e, HashSet same, HashSet other)
	{
		while (  e.hasMoreElements())
		{
			Object o=e.nextElement();

			if ( ! both.contains( o))
			{
				if ( other.contains( o))
				{
					other.remove( o);
					both.add( o);
				}
				else
					same.add( o);
			}
		}
	}
}
