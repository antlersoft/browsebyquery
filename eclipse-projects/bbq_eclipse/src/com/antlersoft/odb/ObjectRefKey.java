package com.antlersoft.odb;

import java.io.Serializable;

/**
 * <p>Title: odb</p>
 * <p>Description: Simple Object Database</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ObjectRefKey implements Comparable, Serializable
{
	private Comparable m_object_key;
    public int compareTo(Object parm1)
	{
		ObjectRefKey  to_compare=(ObjectRefKey)parm1;
		if ( m_object_key==null)
		{
			if ( to_compare.m_object_key==null)
				return 0;
			return -1;
		}
		if ( to_compare.m_object_key==null)
			return 1;
		return m_object_key.compareTo( to_compare.m_object_key);
	}

	public ObjectRefKey( ObjectRef ref)
	{
		if ( ref.impl!=null)
			m_object_key=(Comparable)ref.impl.objectKey;
		else
			m_object_key=null;
	}

	public ObjectRefKey( Persistent p)
	{
		m_object_key=(Comparable)p._getPersistentImpl().objectKey;
	}
}