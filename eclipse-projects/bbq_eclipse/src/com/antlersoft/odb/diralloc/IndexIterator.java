
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

import java.util.NoSuchElementException;

class IndexIterator implements com.antlersoft.odb.IndexIterator
{
    private Index baseIndex;
    private IndexPage currentPage;
    private int currentOffset;
    private boolean exactMatch;

    IndexIterator( Index b, IndexPage p, int o, boolean m)
    {
        baseIndex=b;
        currentPage=p;
        currentOffset=o;
        exactMatch=m;
    }

    public boolean hasNext()
    {
        return currentOffset<currentPage.size;
    }

    public boolean isExactMatch()
    {
        return exactMatch;
    }

    public Object next()
    {
        baseIndex.manager.startIndexPageNoFlush();
        baseIndex.indexModificationLock.enterProtected();
        try
        {
            if ( currentOffset>=currentPage.size)
                throw new NoSuchElementException();
            DAKey result=new DAKey( currentPage.nextOffsetArray[currentOffset],
                currentPage.reuseArray[currentOffset]);
            currentOffset++;
            if ( currentOffset>=currentPage.size)
            {
                IndexPage nextPage=currentPage;
                while ( nextPage.thisPage.parent!=null)
                {
                    IndexPage parent=baseIndex.manager.getIndexPageByKey(
                        nextPage.thisPage.parent);
                    int keyOffset=baseIndex.binarySearch( parent.keyArray, 0,
                        parent.size-1, currentPage.keyArray[0]);
                    if ( keyOffset<0)
                        keyOffset= -keyOffset-1;
                    else
                        keyOffset++;
                    if ( keyOffset>=parent.size)
                    {
                        nextPage=parent;
                    }
                    else
                    {
                        nextPage=baseIndex.getChildPage( parent, keyOffset);
                        while ( nextPage.reuseArray!=null)
                        {
                            nextPage=baseIndex.getChildPage( nextPage, 0);
                        }
                        currentPage=nextPage;
                        currentOffset=0;
                    }
                }
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