
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
import java.io.IOException;

public class MethodInfo extends FieldInfo
{
	MethodInfo( DataInputStream dis, ClassWriter containing)
 		throws IOException, CodeCheckException
    {
    	super( dis, containing);
    }

	public CodeAttribute getCodeAttribute()
 	{
  		return (CodeAttribute)attributes.getAttributeByType(
    		CodeAttribute.typeString);
  	}
}