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

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Enumeration;

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public class DBField extends DBMember
{
    ObjectRef<DBClass> dbclass;
    ObjectRef<DBType> dbtype;
    String name;
    int accessFlags;
    private boolean deprecated;

    private transient PersistentImpl _persistentImpl;

    DBField( DBClass cl, String name, DBType ty)
    {
    	super( name, cl, ty);
		_persistentImpl=new PersistentImpl();
		dbtype=new ObjectRef<DBType>();
		ObjectDB.makePersistent( this);
    }

    public PersistentImpl _getPersistentImpl()
    {
		if ( _persistentImpl==null)
			_persistentImpl=new PersistentImpl();
	    return _persistentImpl;
    }

    public String toString()
    {
    	return ((DBClass)dbclass.getReferenced()).name+":"+name+" "+getDBType(null).toString();
    }

    public int fieldStatus()
    {
		if ( ! ((DBClass)dbclass.getReferenced()).isResolved())
		    return DBMethod.UNRESOLVED;
		else
		    return DBMethod.REAL;
    }

    public String getName()
    {
    	return name;
    }

    public int getLineNumber()
    {
        return getDBClass().getLineNumber();
    }

    public Enumeration<DBFieldReference> getReferencedBy()
    {
    }
}
