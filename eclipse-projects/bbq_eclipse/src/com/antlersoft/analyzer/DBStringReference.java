
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.analyzer;

import com.antlersoft.odb.ObjectRef;

public class DBStringReference extends DBReference
{
    private ObjectRef target;

    DBStringReference( DBMethod method, DBStringConstant constant, int l)
    {
        super( method, l);
        target=new ObjectRef( constant);
    }

    public DBStringConstant getTarget()
    {
        return (DBStringConstant)target.getReferenced();
    }

    public String toString()
    {
		return "Reference to "+getTarget().toString()+" from "+getSource().toString()+" at line "+String.valueOf( lineNumber);
    }

	public int hashCode()
	{
		return getSource().hashCode()^lineNumber^target.hashCode();
	}

	public boolean equals( Object toCompare)
	{
		if ( toCompare instanceof DBStringReference)
		{
			DBStringReference f=(DBStringReference)toCompare;

			return f.getSource().equals( getSource()) && f.lineNumber==lineNumber &&
				f.getTarget().equals( getTarget());
		}
		return false;
	}
}