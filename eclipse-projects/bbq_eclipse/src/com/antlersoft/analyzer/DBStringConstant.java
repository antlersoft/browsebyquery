
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
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public class DBStringConstant implements Persistent, Cloneable
{
	public final static String STRING_INDEX="STRING_INDEX";
	
    String constant;

    private transient PersistentImpl _persistentImpl;

    public DBStringConstant( String key)
    {
        constant=key;
        _persistentImpl=new PersistentImpl();
        ObjectDB.makePersistent( this);
    }
    
    /**
     * Find a string constant for a certain string in the database; if it does not exist,
     * create it.
     * @param db Analyzer database
     * @param tofind Value of string constant to fine
     * @return DBStringConstant object found in or added to the database
     */
    public static DBStringConstant get( IndexAnalyzeDB db, String tofind)
    throws Exception
    {
    	DBStringConstant c=(DBStringConstant)db.findWithIndex(STRING_INDEX, tofind);
    	if ( c==null)
    		c=new DBStringConstant( tofind);
    	
    	return c;
    }

    /**
     * 
     * @return Enumeration over DBStringReference objects referencing this constant
     */
    public Enumeration getReferencedBy( IndexAnalyzeDB db)
    {
    	return db.retrieveByIndex(DBStringReference.SRTARGET, new ObjectRefKey(this));
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

	static class StringKeyGenerator implements KeyGenerator
	{

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return ((DBStringConstant)o1).constant;
		}
	}
}