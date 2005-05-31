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
package com.antlersoft.analyzecxx;

import java.util.HashSet;

import com.antlersoft.parser.Symbol;
import com.antlersoft.parser.Token;

/**
 * Base class of tokens used in the analyzer; really just a marker class.
 * The values used by the parser with be the tokens themselves; objects of
 * this class (or derived classes).
 */
class LexToken extends Token
{
	private HashSet m_no_expand;

	HashSet m_empty_set=new HashSet();

	LexToken( Symbol symbol, String value)
	{
		super( symbol, value);
		m_no_expand=m_empty_set;
	}

	/**
	 * Compares symbol and value for equality; used to match up
	 * replacement lists in duplicate macro definitions.
	 */
	public boolean equals( Object o)
	{
		boolean result=( o instanceof LexToken);
		if ( result)
		{
			LexToken other=(LexToken)o;
			if ( other.symbol==symbol)
			{
				result=( other.value==null) ? ( value==null) :
					( other.value.equals( value));
			}
			else
				result=false;
		}
		return result;
	}

	boolean canExpand( String name)
	{
		return ! m_no_expand.contains( name);
	}

	HashSet setWithNewMember( String added)
	{
		//try
		//{
			HashSet result = (HashSet) m_no_expand.clone();
			result.add(added);
			return result;
		//}
		//catch ( CloneNotSupportedException cse)
		//{
		//}
		//return null;
	}

	LexToken cloneWithNewSet( HashSet set)
	{
		try
		{
			LexToken result = (LexToken) clone();
			result.m_no_expand = set;
			return result;
		}
		catch ( CloneNotSupportedException cse)
		{

		}
		return null;
	}
	LexToken cloneWithMergedSet( HashSet set)
	{
		try
		{
			LexToken result = (LexToken) clone();
			result.m_no_expand = (HashSet)result.m_no_expand.clone();
			result.m_no_expand.addAll( set);
			return result;
		}
		catch ( CloneNotSupportedException cse)
		{

		}
		return null;
	}
}