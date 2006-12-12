
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

public class ExecuteExpression extends SetExpression
{
	private Vector storedResult;
    private SetExpression setExpression;
    private Class resultClass;
    private Comparator ordering;

    public ExecuteExpression( SetExpression toExecute)
    {
    	storedResult=null;
        setExpression=toExecute;
        resultClass=toExecute.getResultClass();
        ordering=toExecute.getOrdering();
    }
    
    public Class getResultClass()
    {
    	return resultClass;
    }
    
    public Comparator getOrdering()
    {
    	return ordering;
    }

    public Enumeration evaluate(DataSource adb)
     {
        if ( storedResult==null)
        {
        	storedResult=new Vector();
	        for ( Enumeration e=setExpression.evaluate( adb);
         		e.hasMoreElements();
	            storedResult.addElement( e.nextElement()));
	        setExpression=null;
        }
        return storedResult.elements();
    }
}