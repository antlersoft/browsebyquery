
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

import java.util.Enumeration;
import java.util.Vector;

import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public class DBStringConstant implements Persistent, Cloneable
{
    String constant;
    Vector referencedBy;

    private transient PersistentImpl _persistentImpl;

    public DBStringConstant( String key, AnalyzerDB db)
    {
        constant=key;
        referencedBy=new Vector();
        _persistentImpl=new PersistentImpl();
        ObjectDB.makePersistent( this);
    }

    public Enumeration getReferencedBy()
    {
    	if ( referencedBy!=null)
    	    return referencedBy.elements();
    	return DBMethod.emptyVector.elements();
    }

    public String toString()
    {
        return constant;
    }

    public void addReferencedBy( DBMethod caller, Vector callsFromCaller)
    {
    	ObjectDB.makeDirty( this);
    	if ( referencedBy==null)
        {
            if ( ! callsFromCaller.isEmpty())
	        referencedBy=callsFromCaller;
        }
    	else
	    /* Remove references from same method and append refs to list */
    	{
    	    int i;
    	    for ( i=0; i<referencedBy.size(); i++)
    	    {
        		if ( ((DBReference)referencedBy.elementAt( i)).getSource()==caller)
        		{
        		    referencedBy.removeElementAt( i);
        		    i--;
        		}
            }
    	    for ( i=0; i<callsFromCaller.size(); i++)
    	    {
    		    referencedBy.addElement( callsFromCaller.elementAt( i));
    	    }
    	}
    }

    public PersistentImpl _getPersistentImpl()
    {
    	if ( _persistentImpl==null)
                _persistentImpl=new PersistentImpl();
    	return _persistentImpl;
    }
}