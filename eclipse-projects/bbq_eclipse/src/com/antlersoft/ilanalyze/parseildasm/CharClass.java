/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.parseildasm;

/**
 * Extra character classification for things in IL asm code
 * @author Michael A. MacDonald
 *
 */
public class CharClass extends com.antlersoft.util.CharClass {
	/**
	 * II.5.3
	 * ID is a contiguous string of characters which starts with either an alphabetic character (A-Z, a-z) or one of "_",
"$", "@", "`" (grave accent), or "?", and is followed by any number of alphanumeric characters (A-Z, a-z, 0-
9) or the characters "_", "$", "@", "`" (grave accent), and "?"
	 * @param c
	 * @return
	 */
	public static final boolean isExtraIDPart( char c)
	{
		return c=='$' || c=='@' || c=='`' || c=='?';
	}
	
	public static final boolean isIDStart( char c)
	{
		return isIdentifierStart(c) || isExtraIDPart(c);
	}
	
	public static final boolean isIDPart( char c)
	{
		return isIdentifierPart(c) || isExtraIDPart(c);
	}
}
