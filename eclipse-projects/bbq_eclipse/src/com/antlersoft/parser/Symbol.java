package com.antlersoft.parser;

import java.util.HashMap;

public class Symbol
{
    protected Symbol( String symbol_name)
	throws DuplicateSymbolException
    {
		synchronized ( _scope)
		{
			Symbol named=(Symbol)_scope.get( symbol_name);
			if ( named!=null)
				throw new DuplicateSymbolException( this);
			_scope.put( symbol_name, this);
		}
    }

    private static HashMap _scope=new HashMap();

    public static Symbol get( String symbol_name)
    {
		try
		{
			return new Symbol( symbol_name);
		}
		catch ( DuplicateSymbolException dse)
		{
			return dse.duplicate;
		}
    }

    public static class DuplicateSymbolException extends Exception
    {
		public Symbol duplicate;
		DuplicateSymbolException( Symbol dupl)
		{
			duplicate=dupl;
		}
    }
}
