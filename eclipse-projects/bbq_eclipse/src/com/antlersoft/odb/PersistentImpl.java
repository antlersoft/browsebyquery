package com.antlersoft.odb;

/**
 * Publicly opaque class that is included in persistent objects to provide
 * support for persistence.
 */
public class PersistentImpl
{
	public PersistentImpl()
	{
		dirty=false;
		objectKey=null;
	}

	boolean dirty;
	ObjectKey objectKey;
}