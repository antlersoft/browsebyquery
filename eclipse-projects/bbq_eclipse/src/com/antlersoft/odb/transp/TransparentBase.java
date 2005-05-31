
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