
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

import com.antlersoft.odb.ObjectDB;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;

public abstract class AuxBase implements Persistent
{
    private transient PersistentImpl _odb_impl;

    protected AuxBase()
    {
        super();
        _odb_impl=new PersistentImpl( this);
    }

    public PersistentImpl _getPersistentImpl()
    {
        if ( _odb_impl==null)
            _odb_impl=new PersistentImpl( this);
        return _odb_impl;
    }

    protected abstract Object _odb_newTransparent();
}