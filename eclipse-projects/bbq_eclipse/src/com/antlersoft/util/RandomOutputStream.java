
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RandomOutputStream extends ByteArrayOutputStream
{
	private static final int SIZE_INCREMENT=1024;

	public RandomOutputStream()
	{
		super( SIZE_INCREMENT);
	}

	public byte[] getWrittenBytes()
		throws IOException
	{
		byte[] retVal=toByteArray();
		reset();
		return retVal;
	}
}