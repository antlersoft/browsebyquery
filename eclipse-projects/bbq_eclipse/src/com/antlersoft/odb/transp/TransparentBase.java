
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

import java.io.Serializable;

public abstract class TransparentBase implements Serializable
{
    public int hashCode()
    {
        return _odb_ref.hashCode();
    }

    public boolean equals( Object toCompare)
    {
        if ( toCompare instanceof TransparentBase)
        {
            toCompare=((TransparentBase)toCompare)._odb_ref;
        }
        return _odb_ref.equals( toCompare);
    }

    protected ObjectRef _odb_ref;

    protected TransparentBase()
    {
        super();
        _odb_ref=new ObjectRef( _odb_aux());
    }

    protected TransparentBase( AuxBase aux)
    {
        super();
        _odb_ref=new ObjectRef( aux);
    }

    protected abstract AuxBase _odb_aux();
}