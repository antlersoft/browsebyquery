/*
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
 */
package com.antlersoft.analyzer;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public class DBField implements Persistent, Cloneable, SourceObject, AccessFlags
{
    ObjectRef dbclass;
    ObjectRef dbtype;
    String name;
    String descriptor;
    Vector referencedBy;
    int accessFlags;
    private boolean deprecated;

    private static final long serialVersionUID = -2154296981139151800L;
    static final String FIELD_TYPE_INDEX="FieldType";
    
    private transient PersistentImpl _persistentImpl;

    public DBField( String key, AnalyzerDB db)
	throws Exception
    {
		StringTokenizer st=new StringTokenizer( key, "\t");
		dbclass=new ObjectRef( (DBClass)db.getWithKey( "com.antlersoft.analyzer.DBClass", (String)st.nextElement()));
		name=(String)st.nextElement();
		descriptor=new String();
		_persistentImpl=new PersistentImpl();
		dbtype=new ObjectRef();
		ObjectDB.makePersistent( this);
	    ((DBClass)dbclass.getReferenced()).addField( this);
    }

    public PersistentImpl _getPersistentImpl()
    {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
	    return _persistentImpl;
    }

    public String toString()
    {
    	return ((DBClass)dbclass.getReferenced()).name+":"+name+descriptor;
    }

    public int fieldStatus()
    {
		if ( ! ((DBClass)dbclass.getReferenced()).isResolved())
		    return DBMethod.UNRESOLVED;
		else
		    return DBMethod.REAL;
    }

    static String makeKey( String className, String fieldName)
    {
		StringBuffer sb=new StringBuffer();
		sb.append( className);
		sb.append( "\t");
		sb.append( fieldName);
		return sb.toString();
    }

    public String getName()
    {
    	return name;
    }

    public String getDescriptor()
    {
    	return descriptor;
    }

    public void setTypeFromDescriptor( AnalyzerDB db, String s)
    	throws Exception
    {
    	if ( ! descriptor.equals(s))
    	{
	    	descriptor=s;
	    	if ( db.captureOptional( AnalyzerDB.OPTIONAL_TYPE_INFO))
	    	{
	    		dbtype.setReferenced( db.getWithKey( "com.antlersoft.analyzer.DBType", s));
	    	}
    		ObjectDB.makeDirty( this);
    	}
    }

    public int getAccessFlags()
    {
        return accessFlags;
    }
    
    public DBType getDBType()
    {
    	return (DBType)dbtype.getReferenced();
    }

    public DBClass getDBClass()
    {
	return (DBClass)dbclass.getReferenced();
    }
   

    public int getLineNumber()
    {
        return getDBClass().getLineNumber();
    }

    public Enumeration getReferencedBy()
    {
	if ( referencedBy!=null)
	    return referencedBy.elements();
	return DBMethod.emptyVector.elements();
    }
    
    void setDeprecated( boolean dep)
    {
    	deprecated=dep;
    }
    
    public boolean isDeprecated()
    {
    	return deprecated;
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
		    /* Remove calls from same method and append calls to list */
		{
		    int i;
		    for ( i=0; i<referencedBy.size(); i++)
		    {
			if ( ((DBFieldReference)referencedBy.elementAt( i)).getSource()==caller)
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
    
	
	static class FieldTypeKeyGenerator implements KeyGenerator
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5048174276104569642L;

		public Comparable generateKey( Object obj)
		{
			DBField field=(DBField)obj;
			return new ObjectRefKey( field.dbtype);
		}
	}
}
