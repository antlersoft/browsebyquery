
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
package com.antlersoft.odb.diralloc;

import java.util.NoSuchElementException;

class IndexIterator implements com.antlersoft.odb.IndexIterator
{
    private Index baseIndex;
    Index.FindResult findResult;
    boolean exactMatch;

    IndexIterator( Index b, Index.FindResult fr, boolean m)
    {
        baseIndex=b;
        findResult=fr;
        exactMatch=m;
    }

    public boolean hasNext()
    {
        return findResult.offset<findResult.page.size;
    }

    /**
     * Returns true if and only if the object that would be returned by
     * a call to next() exactly matches the key that was supplied for
     * the index lookup to get this iterator.
     */
    public boolean isExactMatch()
    {
        return exactMatch;
    }

    public Object next()
    {
		exactMatch=false;
        baseIndex.manager.startIndexPageNoFlush();
        baseIndex.indexModificationLock.enterProtected();
        try
        {
            if ( findResult.offset>=findResult.page.size)
                throw new NoSuchElementException();
            DAKey result=new DAKey( findResult.page.nextOffsetArray[findResult.offset],
                findResult.page.reuseArray[findResult.offset]);
            findResult.offset++;
            if ( findResult.offset>=findResult.page.size)
            {
            	baseIndex.moveToFirstKeyOfNextPage(findResult);
            }
            return result;
        }
        finally
        {
            baseIndex.indexModificationLock.leaveProtected();
            baseIndex.manager.endIndexPageNoFlush();
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}