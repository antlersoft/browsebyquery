package com.antlersoft.analyzer.query;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class EmptyEnumeration implements Enumeration
{
	static EmptyEnumeration emptyEnumeration=new EmptyEnumeration();

	public boolean hasMoreElements()
	{
		return false;
	}

	public Object nextElement()
	{
		throw new NoSuchElementException();
	}
}
