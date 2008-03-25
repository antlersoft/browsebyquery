/**
 * Copyright (c) 2008 - Michael A. MacDonald
 */
package com.antlersoft.util.xml;

import org.xml.sax.Attributes;

import org.xml.sax.helpers.AttributesImpl;

/**
 * Get/set typed values in a org.xml.sax.Attributes collection by qname
 * @author Michael A. MacDonald
 *
 */
public class SimpleAttributes {
	private Attributes attr;
	public int defaultInt=0;
	public String defaultString="";
	public double defaultDouble=0.0;
	
	public SimpleAttributes( Attributes a)
	{
		attr=a;
	}
	
	public SimpleAttributes()
	{
		attr=new AttributesImpl();
	}
	
	public Attributes getAttributes()
	{
		return attr;
	}
	
	public <E extends Enum<E> > E enumValue( Object qName, E defaultValue)
	{
		String val=attr.getValue(qName.toString());
		
		if ( val!=null)
		{
			try
			{
				return Enum.valueOf( ((Class<E>)defaultValue.getClass()), val);
			}
			catch ( IllegalArgumentException iae)
			{
				// Can't be parsed, return default
			}
		}
		return defaultValue;
	}
	
	public void addValue( Object qName, Object o)
	{
		((AttributesImpl)attr).addAttribute("", "", qName.toString(), "", o.toString());
	}
	
	public int intValue( Object qName, int def)
	{
		String val=attr.getValue(qName.toString());
		if ( val!=null)
		{
			try
			{
				return Integer.valueOf( val);
			}
			catch ( NumberFormatException nfe)
			{
				// Can't be parsed, return default
			}
		}
		return def;
	}
	
	public int intValue( Object qName)
	{
		return intValue( qName, defaultInt);
	}
	
	public void setDefaultInt( int d)
	{
		defaultInt=d;
	}
	
	public String stringValue( Object qName, String def)
	{
		String val=attr.getValue( qName.toString());
		if ( val==null)
			val=def;
		return val;
	}
	
	public String stringValue( Object qName)
	{
		return stringValue( qName, defaultString);
	}

	public double doubleValue( Object qName, double def)
	{
		String val=attr.getValue(qName.toString());
		if ( val!=null)
		{
			try
			{
				return Integer.valueOf( val);
			}
			catch ( NumberFormatException nfe)
			{
				// Can't be parsed, return default
			}
		}
		return def;
	}
	
	public double doubleValue( Object qName)
	{
		return doubleValue( qName, defaultDouble);
	}
	
	public long longValue( Object qName, long def)
	{
		String val=attr.getValue(qName.toString());
		if ( val!=null)
		{
			try
			{
				return Long.valueOf( val);
			}
			catch ( NumberFormatException nfe)
			{
				// Can't be parsed, return default
			}
		}
		return def;
	}
	
	public long longValue( Object qName)
	{
		return longValue( qName, defaultInt);
	}
	
}
