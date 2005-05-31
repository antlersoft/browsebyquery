
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
package com.antlersoft.odb.transp;

import com.antlersoft.odb.*;

public class TransparentDB extends ObjectDB
{
    public TransparentDB( ObjectStore store)
    {
        super( store);
    }

	public synchronized void setPersistent( Object toStore)
		throws ObjectStoreException
	{
        if ( toStore instanceof TransparentBase)
            toStore=((TransparentBase)toStore)._odb_ref.getReferenced();
        super.setPersistent( toStore);
    }

    public Object get( ObjectKey key)
		throws ObjectStoreException
    {
		Object result=getObjectByKey( key);
        if ( result instanceof AuxBase)
            result=((AuxBase)result)._odb_newTransparent();
        return result;
    }

	public ObjectKey getObjectKey( Object object)
	{
		if ( object instanceof TransparentBase)
            object=((TransparentBase)object)._odb_ref.getReferenced();
        return super.getObjectKey(object);
	}

    public void deleteObject( Object object)
    {
		if ( object instanceof TransparentBase)
            object=((TransparentBase)object)._odb_ref.getReferenced();
        super.deleteObject(object);
    }

	public void makeRootObject( String key, Object object)
	{
		if ( object instanceof TransparentBase)
            object=((TransparentBase)object)._odb_ref.getReferenced();
		super.makeRootObject( key, object);
	}

	public Object getRootObject( String key)
	{
		Object result=super.getRootObject( key);
        if ( result instanceof AuxBase)
            result=((AuxBase)result)._odb_newTransparent();
        return result;
	}
}