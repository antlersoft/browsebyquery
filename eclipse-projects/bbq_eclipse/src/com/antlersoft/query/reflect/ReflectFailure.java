package com.antlersoft.query.reflect;

public class ReflectFailure extends RuntimeException {
	public ReflectFailure( Exception caused_by)
	{
		super( caused_by);
	}
}