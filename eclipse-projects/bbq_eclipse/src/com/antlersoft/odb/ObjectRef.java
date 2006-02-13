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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectRef implements Serializable
{
    transient PersistentImpl impl;

    public ObjectRef()
    {
        impl=null;
    }

	public ObjectRef( Object toReference)
	{
		this();
		setReferenced( toReference);
	}

    public Object getReferenced()
    {
        PersistentImpl currentImpl=impl;
		if ( currentImpl==null)
			return null;
        return currentImpl.getCanonical( this);
    }

    public synchronized void setReferenced( Object newValue)
    {
        if ( newValue!=null)
			impl=((Persistent)newValue)._getPersistentImpl();
		else
			impl=null;
    }

	public int hashCode()
	{
		Object referenced=getReferenced();
		if ( referenced==null)
		    return 0;
		return referenced.hashCode();
	}

	public boolean equals( Object toCompare)
	{
		if ( toCompare instanceof ObjectRef)
		{
			toCompare=((ObjectRef)toCompare).getReferenced();
		}
		Object ours=getReferenced();
		if ( ours==null)
		{
			return toCompare==null;
		}
		return ours.equals( toCompare);
	}

    private void writeObject( ObjectOutputStream out)
        throws IOException
    {
        PersistentImpl currentImpl=impl;

        if ( currentImpl==null)
            out.writeObject( null);
        else
            out.writeObject( currentImpl.objectKey);
    }

    private void readObject( ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        ObjectKey key=(ObjectKey)in.readObject();
        if ( key==null)
            impl=null;
        else
        {
            impl=new PersistentImpl();
            impl.objectKey=key;
        }
    }

}
