package com.antlersoft.odb;

import java.io.Serializable;

public class DualRef extends ObjectRef
{
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