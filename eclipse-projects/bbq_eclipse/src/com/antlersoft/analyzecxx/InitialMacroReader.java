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

import java.util.ArrayList;

import com.antlersoft.parser.RuleActionException;

public class InitialMacroReader implements LexReader {
	private ArrayList m_list=new ArrayList();
    public void processToken(LexToken next_token) throws com.antlersoft.parser.RuleActionException {
		m_list.add( next_token);
    }

	public void noMoreTokens() {}

	ArrayList getTokens()
	{
		return m_list;
	}
}