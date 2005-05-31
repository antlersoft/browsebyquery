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
package com.antlersoft.query;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class SingleEnum implements Enumeration {
	public SingleEnum( Object obj)
	{
		m_obj=obj;
		m_used=false;
	}
    public boolean hasMoreElements() {
		return ! m_used;
    }
    public Object nextElement() {
		if ( m_used)
			throw new NoSuchElementException();
		m_used=true;
		return m_obj;
    }
	private Object m_obj;
	private boolean m_used;
}