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
            impl.obsolete=true;
        }
    }

}
