/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.odb;

import java.io.Serializable;

public class DualRef extends ObjectRef
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6769695183831894619L;
	private Serializable serializableRef;
	private transient Object nonSerializableRef;

	public DualRef()
	{
		super();
		serializableRef=null;
		nonSerializableRef=null;
	}

	public DualRef( Object toStore)
	{
		this();
		setReferenced( toStore);
	}

	public Object getReferenced()
	{
		if ( serializableRef!=null)
			return serializableRef;
		if ( nonSerializableRef!=null)
			return nonSerializableRef;
		return super.getReferenced();
	}

	public void setReferenced( Object toStore)
	{
		if ( toStore==null || toStore instanceof Persistent)
		{
			serializableRef=null;
			nonSerializableRef=null;
			super.setReferenced( toStore);
		}
		else if ( toStore instanceof Serializable)
		{
			serializableRef=(Serializable)toStore;
			nonSerializableRef=null;
			super.setReferenced( null);
		}
		else
		{
			serializableRef=null;
			nonSerializableRef=toStore;
			super.setReferenced( null);
		}
	}
}