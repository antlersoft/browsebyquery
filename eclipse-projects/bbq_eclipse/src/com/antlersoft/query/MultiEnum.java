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

/**
 * Starting with an enumeration that returns enumerations,
 * create a single enumeration over all those enumerations.
 * @author Michael A. MacDonald
 *
 */
public class MultiEnum implements Enumeration {
	private Enumeration m_base;
	private Enumeration m_current;
	public MultiEnum( Enumeration base)
	{
		m_base=base;
	}
    public boolean hasMoreElements() {
		while ( m_current==null || ! m_current.hasMoreElements())
		{
			if ( m_base.hasMoreElements())
			{
				m_current=(Enumeration)m_base.nextElement();
			}
			else
				return false;
		}
		return true;
    }
    public Object nextElement() {
		if ( ! hasMoreElements())
			throw new java.util.NoSuchElementException();
		return m_current.nextElement();
    }
}