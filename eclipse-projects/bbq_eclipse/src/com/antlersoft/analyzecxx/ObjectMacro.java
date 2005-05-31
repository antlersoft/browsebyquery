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

/**
 * An object-style preprocessor macro
 */
class ObjectMacro extends Macro
{
	private static final ArrayList m_empty_list=new ArrayList();

	ArrayList m_token_list;

	ObjectMacro( String identifier, ArrayList token_list)
	{
		super( identifier);
		m_token_list=token_list;
	}

	ObjectMacro( String identifier, LexToken token)
	{
		super( identifier);
		m_token_list=new ArrayList(1);
		m_token_list.add( token);
	}

	ObjectMacro( String identifier)
	{
		this( identifier, m_empty_list);
	}

	ArrayList getTokens()
	{
		return m_token_list;
	}
}