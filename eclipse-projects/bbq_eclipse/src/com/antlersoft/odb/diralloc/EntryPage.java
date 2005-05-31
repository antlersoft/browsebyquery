
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

import java.io.Serializable;

class EntryPage implements Serializable
{
    int[] offset;
    int[] reuseCount;
    int[] classIndex;
    int[] classReuse;
    int size;

    transient boolean modified;

    EntryPage()
    {
        modified=true;
        size=0;
        offset=new int[EntryPageList.ENTRY_PAGE_SIZE];
        reuseCount=new int[EntryPageList.ENTRY_PAGE_SIZE];
        classIndex=new int[EntryPageList.ENTRY_PAGE_SIZE];
        classReuse=new int[EntryPageList.ENTRY_PAGE_SIZE];
    }

    void setInitialValues( int localOffset,
        int rc, int ci, int cr)
    {
        modified=true;
        reuseCount[localOffset]=rc;
        classIndex[localOffset]=ci;
        classReuse[localOffset]=cr;
    }
}