package com.antlersoft.analyzer;

import com.antlersoft.odb.ObjectRef;

public abstract class DBReference implements java.io.Serializable, Cloneable,
    SourceObject
{
    DBReference( DBMethod s, int l)
    {
	source=new ObjectRef( s);
	lineNumber=l;
    }

    private ObjectRef source;

    public DBMethod getSource()
    {
	return (DBMethod)source.getReferenced();
    }

    public DBClass getDBClass()
    {
        return getSource().getDBClass();
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    protected int lineNumber;
}
