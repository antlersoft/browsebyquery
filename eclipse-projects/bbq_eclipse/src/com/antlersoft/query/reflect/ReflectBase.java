package com.antlersoft.query.reflect;

import com.antlersoft.parser.ParseState;
import com.antlersoft.parser.Symbol;

import com.antlersoft.query.BasicBase;

public class ReflectBase extends BasicBase {
	public static final Symbol nameSymbol=Symbol.get( "nameSymbol");
	public ReflectBase( ParseState[] states)
	{
		super( states);
	}
}