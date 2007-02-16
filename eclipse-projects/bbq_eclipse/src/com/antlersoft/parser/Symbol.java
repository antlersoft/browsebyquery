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

import java.util.HashMap;

public class Symbol
{
	private String m_symbol_name;

    protected Symbol( String symbol_name)
	throws DuplicateSymbolException
    {
		synchronized ( _scope)
		{
			Symbol named=(Symbol)_scope.get( symbol_name);
			if ( named!=null)
				throw new DuplicateSymbolException( named);
			_scope.put( symbol_name, this);
			m_symbol_name=symbol_name;
		}
    }

    private static HashMap _scope=new HashMap();

    public static Symbol get( String symbol_name)
    {
    	Symbol s=(Symbol)_scope.get( symbol_name);
    	if ( s==null)
    	{
			try
			{
				s=new Symbol( symbol_name);
			}
			catch ( DuplicateSymbolException dse)
			{
				s=dse.duplicate;
			}
    	}
    	return s;
    }

    public static class DuplicateSymbolException extends Exception
    {
		public Symbol duplicate;
		DuplicateSymbolException( Symbol dupl)
		{
			duplicate=dupl;
		}
    }

	public String toString()
	{
		return m_symbol_name;
	}
}
