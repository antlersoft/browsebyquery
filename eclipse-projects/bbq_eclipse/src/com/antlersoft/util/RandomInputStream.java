
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb;

import java.io.IOException;
import java.io.InputStream;

public class RandomInputStream extends InputStream
{
	private static final int SIZE_INCREMENT=1024;

	public RandomInputStream()
	{
		position=0;
		count=0;
		buffer=new byte[SIZE_INCREMENT];
	}

	public synchronized void emptyAddBytes( byte[] toAdd, int offset,
        int length)
	{
		position=0;
		count=0;
		if ( length>buffer.length)
		{
			int newSize=( ( ( length)/SIZE_INCREMENT)+1)*SIZE_INCREMENT;
			byte[] newBuffer=new byte[newSize];
			buffer=newBuffer;
		}
		System.arraycopy( toAdd, offset, buffer, 0, length);
		count+=length;
	}

    public synchronized void emptyAddBytes( byte[] toAdd)
    {
        emptyAddBytes( toAdd, 0, toAdd.length);
    }

	private void packBuffer()
	{
		if ( position==count)
		{
			position=0;
			count=0;
		}
	}

	synchronized public int read()
		throws IOException
	{
		int retVal= -1;
		if ( position<count)
		{
			retVal=buffer[position++];
			if ( retVal<0)
				retVal+=256;
			packBuffer();
		}
		return retVal;
	}

	synchronized public int read( byte[] dest, int offset, int len)
		throws IOException
	{
		int retval= -1;
		if ( position<count)
		{
			if ( len>count-position)
				len=count-position;
			retval=len;
			System.arraycopy( buffer, position, dest, offset, len);
			position+=len;
			packBuffer();
		}
		return retval;
	}

	synchronized public long skip( long n)
		throws IOException
	{
		if ( n>count-position)
			n=count-position;
		position+=n;
		packBuffer();
		return n;
	}

	public int available()
	    throws IOException
	{
		return count-position;
	}

	private int position;
	private int count;
	private byte[] buffer;
}