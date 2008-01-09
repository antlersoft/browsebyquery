/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.List;

/**
 * All information relating to a custom attribute declaration found in an assembly
 * @author Michael A. MacDonald
 *
 */
public class CustomAttributeSetting {
	
	private ReadType containing;
	private Signature signature;
	private List stringArguments;
	
	/**
	 * 
	 * @return Type that contains the attribute, or null if it is implicit (can that happen?)
	 */
	public ReadType getContainingType()
	{
		return containing;
	}
	
	/**
	 * 
	 * @return Signature of constructor for the attribute type being invoked
	 */
	public Signature getSignature()
	{
		return signature;
	}
	
	/**
	 * All string arguments for the constructor (including all the members of string arrays)
	 * @return
	 */
	public List getStringArguments()
	{
		return stringArguments;
	}

	public static class NamedArgument
	{
		private String name;
		private boolean isProp;
		private List stringArguments;
		
		/**
		 * All string arguments for the constructor (including all the members of string arrays)
		 * @return
		 */
		public List getStringArguments()
		{
			return stringArguments;
		}
	}
}
