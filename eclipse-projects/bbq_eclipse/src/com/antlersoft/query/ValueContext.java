package com.antlersoft.query;

public interface ValueContext {
	public static final int SCALAR=1;
	public static final int GROUP=2;
	public static final int COUNT_PRESERVING=3;

	public int getContextType();
}