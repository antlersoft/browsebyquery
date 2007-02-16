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
package com.antlersoft.parser;

public class Token implements Cloneable
{
	public Symbol symbol;
	public String value;

	public Token( Symbol sym, String val)
	{
		symbol=sym;
		value=val;
	}

	public String toString()
	{
		return value;
	}
	
	public boolean equals( Object o)
	{
		boolean result=false;
		if ( o instanceof Token)
		{
			Token t=(Token)o;
			result=( t.symbol==symbol && t.value.equals( value));
		}
		return result;
	}

	public Object clone()
	throws CloneNotSupportedException
	{
		return super.clone();
	}
}
