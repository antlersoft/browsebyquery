
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class LineNumberTableAttribute implements Attribute
{
	public final static String typeString="LineNumberTable";

	int lineNumberEntryCount;
	LineNumberEntry[] lineNumberEntries;
	LineNumberTableAttribute( DataInputStream classStream)
	    throws IOException
    {
	    lineNumberEntryCount=classStream.readUnsignedShort();
	    lineNumberEntries=new LineNumberEntry[lineNumberEntryCount];
	    for ( int i=0; i<lineNumberEntryCount; i++)
	    {
			lineNumberEntries[i]=new LineNumberEntry( classStream);
	    }
	}

	public String getTypeString() { return typeString; }
	public void write( DataOutputStream classStream)
 		throws IOException
   	{
    	classStream.writeShort( lineNumberEntryCount);
     	for ( int i=0; i<lineNumberEntryCount; i++)
      	{
       		classStream.writeShort( lineNumberEntries[i].start_pc);
         	classStream.writeShort( lineNumberEntries[i].line_number);
       	}
    }

    class LineNumberEntry
    {
		int start_pc;
		int line_number;
		LineNumberEntry( DataInputStream classStream)
		    throws IOException
		{
		    start_pc=classStream.readUnsignedShort();
		    line_number=classStream.readUnsignedShort();
		}
    }
}
