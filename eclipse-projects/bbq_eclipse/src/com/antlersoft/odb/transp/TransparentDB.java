
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
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