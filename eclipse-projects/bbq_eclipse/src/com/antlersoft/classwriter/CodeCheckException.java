
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package classwriter;

public class CodeCheckException extends Exception
{
    public CodeCheckException()
    {
        super();
    }

    public CodeCheckException( String message)
    {
        super( message);
    }
}