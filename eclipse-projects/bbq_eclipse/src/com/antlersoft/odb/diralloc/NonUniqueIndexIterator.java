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
package com.antlersoft.odb.diralloc;

class NonUniqueIndexIterator extends IndexIterator
{
	Comparable origKey;

	NonUniqueIndexIterator( Index b, IndexPage p, int o, boolean m, Comparable k)
	{
		super( b, p, o, m);
		origKey=k;
	}

	public Object next()
	{
		boolean oldMatch=exactMatch;

		Object result=super.next();

		if ( oldMatch && hasNext())
		{
			exactMatch=origKey.compareTo( ((UniqueKey)currentPage.keyArray[currentOffset]).base)==0;
		}
		return result;
	}
}