/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * All information relating to a custom attribute declaration found in an assembly
 * @author Michael A. MacDonald
 *
 */
public class CustomAttributeSetting {
	
	private ReadType containing;
	private Signature signature;
	private List<String> stringArguments;
	private List<NamedArgument> namedArguments;
	
	/**
	 * Create when there is a single string associated with the declaration
	 * @param cont Containing type or null
	 * @param sig Constructor signature (which should consist of a single string argument)
	 * @param data Data string
	 */
	public CustomAttributeSetting( ReadType cont, Signature sig, String data)
	{
		containing=cont;
		signature=sig;
		stringArguments=new ArrayList<String>(1);
		stringArguments.add( data);
		namedArguments=new ArrayList<NamedArgument>(0);
	}
	
	/**
	 * Create when there is no data, or byte data, associated with the declaration
	 * @param cont Containing type or null
	 * @param sig Constructor signature
	 */
	public CustomAttributeSetting( ReadType cont, Signature sig)
	{
		containing=cont;
		signature=sig;
		stringArguments=new ArrayList<String>();
		namedArguments=new ArrayList<NamedArgument>();
	}
	
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
	public List<String> getStringArguments()
	{
		return stringArguments;
	}
	
	/**
	 * @return A collection of NamedArgument objects giving the properties/fields being set and the values set in them
	 */
	public List<NamedArgument> getNamedArguments()
	{
		return namedArguments;
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		if ( containing!=null)
			sb.append(containing.toString());
		sb.append( "::.ctor(");
		sb.append( signature.toString());
		sb.append( ")");
		for ( Iterator<String> i=stringArguments.iterator(); i.hasNext();)
		{
			sb.append( i.next());
			if ( i.hasNext())
				sb.append( ',');
		}
		for ( Iterator<NamedArgument> i=namedArguments.iterator(); i.hasNext();)
		{
			sb.append("\r\n");
			i.next().addNamedArgument(sb);
		}
		return sb.toString();
	}

	/**
	 * A named argument appearing in the custom class declaration
	 * @author Michael A. MacDonald
	 *
	 */
	public static class NamedArgument
	{
		private String name;
		private boolean isProp;
		private List<String> stringArguments;
		private ReadType type;
		
		NamedArgument( String n, boolean is_prop, ReadType t)
		{
			name=n;
			isProp=is_prop;
			stringArguments=new ArrayList<String>();
			type=t;
		}
		
		/**
		 * 
		 * @return The type of the property or field set by the named argument
		 */
		public ReadType getType()
		{
			return type;
		}
		
		/**
		 * 
		 * @return All string arguments for the constructor (including all the members of string arrays)
		 */
		public List<String> getStringArguments()
		{
			return stringArguments;
		}
		
		/**
		 * 
		 * @return true if the named argument sets the value of a property; false if it is setting the value of a field
		 */
		public boolean isProperty()
		{
			return isProp;
		}
		
		/**
		 * 
		 * @return The name of the argument, corresponding to the name of the property or field in the attribute being set
		 */
		public String getName()
		{
			return name;
		}
		
		void addNamedArgument( StringBuilder sb)
		{
			sb.append( name);
			sb.append( '(');
			sb.append( isProperty() ? "property" : "field");
			sb.append(")=");
			for ( Iterator<String> i=stringArguments.iterator(); i.hasNext();)
			{
				sb.append( i.next());
				if ( i.hasNext())
					sb.append( ',');
			}
		}
	}
}
