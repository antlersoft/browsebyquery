package com.antlersoft.parser;

public class Token implements Cloneable
{
	public Symbol symbol;
	public String value;

	public Token( Symbol sym, String val)
	{
		symbol=sym;
		value=val;
	}

	public String toString()
	{
		return value;
	}

	public Object clone()
	throws CloneNotSupportedException
	{
		return super.clone();
	}
}
