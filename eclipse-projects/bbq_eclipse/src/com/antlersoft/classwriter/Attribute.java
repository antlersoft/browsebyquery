
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
import java.io.IOException;

public interface Attribute extends Cloneable
{
	public void write( DataOutputStream classStream)
 		throws IOException;
    public String getTypeString();
}