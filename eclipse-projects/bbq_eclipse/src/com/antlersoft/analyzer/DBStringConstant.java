
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

import com.antlersoft.odb.ExactMatchIndexEnumeration;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public class DBStringConstant implements Persistent, Cloneable
{
    String constant;

    private transient PersistentImpl _persistentImpl;

    public DBStringConstant( String key)
    {
        constant=key;
        _persistentImpl=new PersistentImpl();
        ObjectDB.makePersistent( this);
    }

    /**
     * 
     * @return Enumeration over DBStringReference objects referencing this constant
     */
    public Enumeration getReferencedBy( IndexObjectDB db)
    {
    	return new ExactMatchIndexEnumeration( db.greaterThanOrEqualEntries(DBStringReference.SRTARGET, new ObjectRefKey(this)));
    }

    public String toString()
    {
        return constant;
    }

    public PersistentImpl _getPersistentImpl()
    {
    	if ( _persistentImpl==null)
                _persistentImpl=new PersistentImpl();
    	return _persistentImpl;
    }
}