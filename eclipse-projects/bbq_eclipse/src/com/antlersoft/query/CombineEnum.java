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

public class CombineEnum implements Enumeration {
	private Enumeration m_first;
	Enumeration m_second;
	boolean m_use_first;
	public CombineEnum( Enumeration first, Enumeration second)
	{
		m_first=first;
		m_second=second;
	}
    public boolean hasMoreElements() {
		boolean result;
		if ( m_use_first)
		{
			result=m_first.hasMoreElements();
			if ( ! result)
			{
				m_use_first=false;
				result=m_second.hasMoreElements();
			}
		}
		else
			result=m_second.hasMoreElements();
		return result;
    }
    public Object nextElement() {
		if ( m_use_first)
		{
			if ( ! m_first.hasMoreElements())
				m_use_first=false;
			else
				return m_first.nextElement();
		}
		return m_second.nextElement();
    }
}