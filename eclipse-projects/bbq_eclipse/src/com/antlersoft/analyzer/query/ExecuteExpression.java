
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package analyzer.query;

import java.util.Enumeration;
import java.util.Vector;

import analyzer.AnalyzerDB;

public class ExecuteExpression extends SetExpression
{
	private Vector storedResult;
    private SetExpression setExpression;

    public ExecuteExpression( SetExpression toExecute)
    {
    	super( toExecute.getSetClass());
    	storedResult=null;
        setExpression=toExecute;
    }

    public Enumeration execute(AnalyzerDB adb)
    	throws java.lang.Exception
    {
        if ( storedResult==null)
        {
        	storedResult=new Vector();
	        for ( Enumeration e=setExpression.execute( adb);
         		e.hasMoreElements();
	            storedResult.addElement( e.nextElement()));
	        setExpression=null;
        }
        return storedResult.elements();
    }
}