package com.antlersoft.analyzer.query;

import java.util.Enumeration;

class SameTransform extends TransformImpl
{
	SameTransform()
	{
		super( null, null);
	}

	public Enumeration transform( Object toTransform)
	{
		return QueryParser.enumFromObject( toTransform);
	}

	public void lateBindApplies( Class c)
	{
		_applies=c;
		_result=c;
	}

	public void lateBindResult( Class c)
	{
		_applies=c;
		_result=c;
	}
}