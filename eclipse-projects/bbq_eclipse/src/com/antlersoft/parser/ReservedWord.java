package com.antlersoft.parser;

import java.util.Hashtable;

public class ReservedWord extends Symbol
{
	public static Hashtable wordList=new Hashtable();
	protected ReservedWord( String w)
			throws Symbol.DuplicateSymbolException
	{
		super( w);
		wordList.put( w, this);
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
