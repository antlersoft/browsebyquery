
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.classwriter;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

class UnknownAttribute implements Attribute
{
	private byte[] _bytes;
 	private String _type;

	UnknownAttribute( int length, String type, DataInputStream classStream)
 		throws IOException
 	{
  		_bytes=new byte[length];
    	_type=type;
     	classStream.readFully( _bytes);
  	}

    public void write(DataOutputStream classStream) throws IOException
    {
    	classStream.write( _bytes);
    }

    public String getTypeString()
    {
    	return _type;
    }
}