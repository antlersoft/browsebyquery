
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
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