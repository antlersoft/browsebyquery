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
package com.antlersoft.odb;

import java.io.Serializable;

public class CompoundKey implements Comparable, Serializable {
	private Comparable key1;
	private Comparable key2;

	public CompoundKey( Comparable k1, Comparable k2)
	{
		key1=k1;
		key2=k2;
	}
    public int compareTo(Object o) {
		CompoundKey ck=(CompoundKey)o;
		int result=key1.compareTo( ck.key1);
		if ( result==0)
			result=key2.compareTo( ck.key2);
		return result;
    }
}