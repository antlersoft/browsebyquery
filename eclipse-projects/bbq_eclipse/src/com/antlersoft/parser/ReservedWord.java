package com.antlersoft.parser;

import java.util.Hashtable;

public class ReservedWord extends Symbol
{
	protected ReservedWord( String w)
			throws Symbol.DuplicateSymbolException
	{
		super( w);
	}

	public static ReservedWord getReserved( String w)
	{
		try
		{
			return new ReservedWord( w);
		}
		catch ( Symbol.DuplicateSymbolException dse)
		{
			return (ReservedWord)dse.duplicate;
		}
	}
}
