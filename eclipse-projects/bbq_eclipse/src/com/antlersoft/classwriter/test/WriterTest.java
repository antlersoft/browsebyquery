
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter.test;

import java.io.*;
import classwriter.*;

public class WriterTest
{
    public static void main(String[] args)
    	throws IOException, CodeCheckException
    {
    	ClassWriter writer=new ClassWriter();

     	writer.readClass( new FileInputStream( args[0]));
      	writer.writeClass( new FileOutputStream( args[1]));
    }
}