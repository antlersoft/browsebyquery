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

import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public abstract class DBReference implements java.io.Serializable, Cloneable,
    SourceObject, Persistent
{
    private transient PersistentImpl _impl;
    private ObjectRef<DBMethod> source;
    protected ObjectRef target;
    protected int lineNumber;
    
    DBReference( DBMethod s, Persistent t, int l)
    {
		source=new ObjectRef<DBMethod>( s);
		target=new ObjectRef(t);
		lineNumber=l;
		_impl=new PersistentImpl();
    }

    public DBMethod getSource()
    {
		return source.getReferenced();
    }

    public DBClass getDBClass()
    {
        return getSource().getDBClass();
    }

    public int getLineNumber()
    {
        return lineNumber;
    }
    
    public void setLineNumber( int ln)
    {
    	if ( lineNumber!=ln)
    	{
    		ObjectDB.makeDirty(this);
    		lineNumber=ln;
    	}
    }
    
    public PersistentImpl _getPersistentImpl()
    {
		if ( _impl==null)
			_impl=new PersistentImpl();
		return _impl;    	
    }

 	static class ReferenceTargetGenerator implements KeyGenerator
	{
		static ReferenceTargetGenerator G=new ReferenceTargetGenerator();

		/* (non-Javadoc)
		 * @see com.antlersoft.odb.KeyGenerator#generateKey(java.lang.Object)
		 */
		public Comparable generateKey(Object o1) {
			return new ObjectRefKey( ((DBReference)o1).target);
		}
		
	}
}
