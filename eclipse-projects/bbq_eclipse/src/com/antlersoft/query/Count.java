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

public class Count extends GroupExpression {
	public Count()
	{
		super( Integer.class, null);
	}
    public Object getValue() {
		return new Integer( m_count);
    }
    public void startGroup(ValueObject vobj, DataSource source) {
		m_count=0;
    }
    public void finishGroup(ValueObject vobj, DataSource source) {
    }
    public boolean addObject(ValueObject vobj, DataSource source, Object to_add) {
		++m_count;
		return true;
    }

	private int m_count;
}