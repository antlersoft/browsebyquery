package com.antlersoft.analyzer;

import java.util.Enumeration;

public interface AnalyzerDB
{
    public abstract void openDB( String dbName)
	throws Exception;
    public abstract void closeDB()
        throws Exception;
    public abstract Object getWithKey( String type, String key)
	throws Exception;
    public abstract Object findWithKey( String type, String key)
	throws Exception;
    public abstract Enumeration getAll( String type)
	throws Exception;
}
