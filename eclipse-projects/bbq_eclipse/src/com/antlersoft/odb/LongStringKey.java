/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.odb;

import java.io.Serializable;

/**
 * Use this class to generate a more compact key for a database index from a string
 * that might be long.  This can keep index pages from growing too large when an
 * indexed column might include both long and short strings.
 * <p>
 * If a key is longer than a threshold value, it is divided into beginning, middle and
 * end segments.  The resulting key is composed of the beginning section, a hash of the middle
 * section, and the end section.
 * @author Michael A. MacDonald
 *
 */
public class LongStringKey implements Serializable {
	private int initialLength;
	private int trailingLength;
	
	public static final int DEFAULT_INITIAL_LENGTH=95;
	public static final int DEFAULT_TRAILING_LENGTH=15;
	
	public LongStringKey()
	{
		this( DEFAULT_INITIAL_LENGTH,DEFAULT_TRAILING_LENGTH);
	}
	
	public LongStringKey( int initial, int trailing)
	{
		initialLength=initial;
		trailingLength=trailing;
	}
	
	public String key( String input)
	{
		int l=input.length();
		if ( l<=initialLength+trailingLength)
		{
			return input;
		}
		return input.substring( 0, initialLength)+Integer.toString(input.substring(initialLength,l-trailingLength).hashCode())+input.substring( l-trailingLength);
	}
}
