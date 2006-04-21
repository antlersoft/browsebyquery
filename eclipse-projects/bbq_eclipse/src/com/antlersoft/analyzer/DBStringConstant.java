
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
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
    private static final long serialVersionUID = 8787076485629057077L;
    
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