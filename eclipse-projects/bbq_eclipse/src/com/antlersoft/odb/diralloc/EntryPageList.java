
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

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;

import com.antlersoft.odb.DiskAllocatorException;
import com.antlersoft.odb.InvalidObjectKeyException;
import com.antlersoft.odb.ObjectStoreException;

import com.antlersoft.util.Semaphore;

class EntryPageList implements Serializable
{
    private static final int ENTRY_PAGE_CACHE_SIZE=100;
    static final int ENTRY_PAGE_SIZE=8000;

    private int freePageOffset;
    private ArrayList pages;

    private transient boolean modified;
    private transient FreeEntryPage freePage;
    private transient int[] pageLRU;
    private transient int lruSize;
    private transient Semaphore pageFlushLock;
    private transient Semaphore deleteLock;

    EntryPageList()
    {
        modified=true;
        pages=new ArrayList();
        pages.add( new EntryPageHeader());
        freePageOffset=0;
        freePage=null;
        pageLRU=new int[ENTRY_PAGE_CACHE_SIZE];
        pageLRU[0]=0;
        lruSize=1;
        pageFlushLock=new Semaphore();
        deleteLock=new Semaphore();
    }

    void initialize( StreamPair streams)
        throws ClassNotFoundException, DiskAllocatorException, IOException
    {
        modified=false;
        pageLRU=new int[ENTRY_PAGE_CACHE_SIZE];
        lruSize=0;
        if ( freePageOffset==0)
            freePage=null;
        else
            freePage=(FreeEntryPage)streams.readObject( freePageOffset);
        pageFlushLock=new Semaphore();
        deleteLock=new Semaphore();
    }

    synchronized void deleteObject( DAKey key, StreamPair streams,
        ClassList classList)
        throws ObjectStoreException
    {
        try
        {
            int localOffset=key.index%ENTRY_PAGE_SIZE;
            deleteLock.enterCritical();
            EntryPage page=makePageCurrent( streams, key.index/ENTRY_PAGE_SIZE);
            if ( key.reuseCount!=page.reuseCount[localOffset])
                throw new InvalidObjectKeyException();
            int classIndex=page.classIndex[localOffset];
            try
            {
                classList.classChangeLock.enterProtected();
                if ( classIndex>classList.classEntries.size() || classIndex<0)
                    throw new InvalidObjectKeyException();
                ClassEntry classEntry=
                    (ClassEntry)classList.classEntries.get( classIndex);
                if ( classEntry.reuseCount!=page.classReuse[localOffset])
                    throw new InvalidObjectKeyException();
                if ( classEntry.indices.size()!=0)
                {
                    Object toDelete=streams.readObjectWithPrefix(
                        page.offset[localOffset]);
                    for ( Iterator i=classEntry.indices.iterator();
                        i.hasNext();)
                    {
                        ((IndexEntry)i.next()).index.removeKey( key, toDelete);
                    }
                }
                classEntry.objectStreams.free( page.offset[localOffset]);
            }
            finally
            {
                classList.classChangeLock.leaveProtected();
            }
            page.offset[localOffset]=0;
            page.classIndex[localOffset]= -1;
            page.modified=true;
            if ( freePage==null || freePage.size==FreeEntryPage.FREE_PAGE_SIZE)
            {
                modified=true;
                if ( freePage!=null)
                    freePageOffset=freePage.sync( streams, freePageOffset);
                freePage=new FreeEntryPage( freePageOffset);
                freePageOffset=streams.writeObject( freePage, 0);
            }
            freePage.modified=true;
            freePage.freeArray[freePage.size++]=key.index;
        }
        catch ( ClassNotFoundException cnfe)
        {
            throw new ObjectStoreException(
                "Class of object not found when deleting", cnfe);
        }
        catch ( IOException ioe)
        {
            throw new ObjectStoreException( "Deleting: ", ioe);
        }
        catch ( DiskAllocatorException dae)
        {
            throw new ObjectStoreException( "Deleting: ", dae);
        }
        finally
        {
            pageFlushLock.leaveProtected();
            deleteLock.leaveCritical();
        }
    }

    synchronized DAKey getNewKey( StreamPair streams, int classIndex,
        int classReuse)
        throws ClassNotFoundException, DiskAllocatorException, IOException
    {
        int offset;
        int reuseCount;
        EntryPage page;
        int localOffset;

        if ( freePage!=null)
        {
            freePage.size--;
            offset=freePage.freeArray[freePage.size];
            if ( freePage.size==0)
            {
                modified=true;
                streams.free( freePageOffset);
                freePageOffset=freePage.nextPageOffset;
                if ( freePageOffset!=0)
                {
                    freePage=(FreeEntryPage)streams.readObject( freePageOffset);
                }
                else
                    freePage=null;
            }
            try
            {
                page=makePageCurrent( streams, offset/ENTRY_PAGE_SIZE);
                localOffset=offset%ENTRY_PAGE_SIZE;
                reuseCount=page.reuseCount[localOffset]+1;
                page.setInitialValues( localOffset,
                    reuseCount,
                    classIndex, classReuse);
            }
            finally
            {
                pageFlushLock.leaveProtected();
            }
        }
        else
        {
            try
            {
                page=makePageCurrent( streams, pages.size()-1);
                offset=(pages.size()-1)*ENTRY_PAGE_SIZE+page.size;
                page.size++;
                reuseCount=0;
                page.setInitialValues( offset%ENTRY_PAGE_CACHE_SIZE,
                    0, classIndex, classReuse);
                if ( page.size==ENTRY_PAGE_SIZE)
                {
                    pageFlushLock.leaveProtected();
                    pageFlushLock.enterCritical();
                    try
                    {
                        makeRoomInCache( streams);
                        modified=true;
                        pageLRU[lruSize]=pages.size();
                        lruSize++;
                        pages.add( new EntryPageHeader());
                    }
                    finally
                    {
                        pageFlushLock.criticalToProtected();
                    }
                }
            }
            finally
            {
                pageFlushLock.leaveProtected();
            }
        }

        return new DAKey( offset, reuseCount);
    }

    void setRegion( DAKey key, int region, StreamPair streams)
        throws ClassNotFoundException, DiskAllocatorException, IOException
    {
        try
        {
            EntryPage page;
            page=makePageCurrent( streams, key.index/ENTRY_PAGE_SIZE);
            int localOffset=key.index%ENTRY_PAGE_SIZE;
            if ( page.reuseCount[localOffset]!=key.reuseCount ||
                page.classIndex[localOffset]<0)
                throw new InvalidObjectKeyException();
            page.offset[localOffset]=region;
            page.modified=true;
        }
        finally
        {
            pageFlushLock.leaveProtected();
        }
    }

    Object getObject( DAKey key, StreamPair streams, ClassList
        classList)
        throws ObjectStoreException
    {
        try
        {
            deleteLock.enterProtected();
            EntryPage page=makePageCurrent( streams, key.index/ENTRY_PAGE_SIZE);
            int localOffset=key.index%ENTRY_PAGE_SIZE;
            if ( key.reuseCount!=page.reuseCount[localOffset])
                throw new InvalidObjectKeyException();
            int classIndex=page.classIndex[localOffset];
            try
            {
                classList.classChangeLock.enterProtected();
                if ( classIndex>classList.classEntries.size() ||
                    classIndex<0)
                    throw new InvalidObjectKeyException();
                ClassEntry classEntry=
                    (ClassEntry)classList.classEntries.get( classIndex);
                if ( classEntry.reuseCount!=page.classReuse[localOffset])
                    throw new InvalidObjectKeyException();
                return classEntry.objectStreams.readObjectWithPrefix(
                    page.offset[localOffset]);
            }
            finally
            {
                classList.classChangeLock.leaveProtected();
            }
        }
        catch ( ClassNotFoundException cnfe)
        {
            throw new ObjectStoreException(
                "Retrieving: Class not found", cnfe);
        }
        catch ( IOException ioe)
        {
            throw new ObjectStoreException( "Retrieving: ", ioe);
        }
        catch ( DiskAllocatorException dae)
        {
            throw new ObjectStoreException( "Retrieving: ", dae);
        }
        finally
        {
            pageFlushLock.leaveProtected();
            deleteLock.leaveProtected();
        }
    }

    void updateObject( DAKey key, Serializable toUpdate,
        StreamPair streams, ClassList classList)
        throws ObjectStoreException
    {
        try
        {
            deleteLock.enterProtected();
            EntryPage page=makePageCurrent( streams, key.index/ENTRY_PAGE_SIZE);
            int localOffset=key.index%ENTRY_PAGE_SIZE;
            if ( key.reuseCount!=page.reuseCount[localOffset])
                throw new InvalidObjectKeyException();
            int classIndex=page.classIndex[localOffset];
            try
            {
                classList.classChangeLock.enterProtected();
                if ( classIndex>classList.classEntries.size() ||
                    classIndex<0)
                    throw new InvalidObjectKeyException();
                ClassEntry classEntry=
                    (ClassEntry)classList.classEntries.get( classIndex);
                if ( classEntry.reuseCount!=page.classReuse[localOffset])
                    throw new InvalidObjectKeyException();
                if ( classEntry.indices.size()!=0)
                {
                    Object oldVersion=streams.readObjectWithPrefix(
                        page.offset[localOffset]);
                    for ( Iterator i=classEntry.indices.iterator();
                        i.hasNext();)
                    {
                        ((IndexEntry)i.next()).index.updateKey( key, oldVersion,
                            toUpdate);
                    }
                }
                int newRegion=classEntry.objectStreams.
                    writeObjectWithPrefix( toUpdate,
                    page.offset[localOffset], key.index, key.reuseCount);
                if ( newRegion!=page.offset[localOffset])
                {
                    page.offset[localOffset]=newRegion;
                    page.modified=true;
                }
            }
            finally
            {
                classList.classChangeLock.leaveProtected();
            }
        }
        catch ( ClassNotFoundException cnfe)
        {
            throw new ObjectStoreException(
                "Retrieving: Class not found", cnfe);
        }
        catch ( IOException ioe)
        {
            throw new ObjectStoreException( "Retrieving: ", ioe);
        }
        catch ( DiskAllocatorException dae)
        {
            throw new ObjectStoreException( "Retrieving: ", dae);
        }
        finally
        {
            pageFlushLock.leaveProtected();
            deleteLock.leaveProtected();
        }
    }

    synchronized int sync( StreamPair streams, int entryOffset)
        throws DiskAllocatorException, IOException
    {
        if ( freePage!=null)
        {
            int newFreeOffset=freePage.sync( streams, freePageOffset);
            if ( newFreeOffset!=freePageOffset)
            {
                freePageOffset=newFreeOffset;
                modified=true;
            }
        }
        try
        {
            pageFlushLock.enterCritical();
            for ( int i=0; i<lruSize; i++)
            {
                EntryPageHeader header=(EntryPageHeader)pages.get( pageLRU[i]);
                if ( header.page.modified)
                {
                    int newPageOffset=streams.writeObject( header.page,
                        header.offset);
                    if ( newPageOffset!=header.offset)
                    {
                        modified=true;
                        header.offset=newPageOffset;
                    }
                    header.page.modified=false;
                }
            }
        }
        finally
        {
            pageFlushLock.leaveCritical();
        }
        if ( modified)
        {
            modified=false;
            entryOffset=streams.writeObject( this, entryOffset);
        }
        return entryOffset;
    }

    private void makeRoomInCache( StreamPair streams)
        throws DiskAllocatorException, IOException
    {
        if ( lruSize==ENTRY_PAGE_CACHE_SIZE)
        {
            EntryPageHeader header=(EntryPageHeader)pages.get( pageLRU[0]);
            if ( header.page.modified)
            {
                int newPageOffset=streams.writeObject( header.page,
                    header.offset);
                if ( newPageOffset!=header.offset)
                {
                    modified=true;
                    header.offset=newPageOffset;
                }
                header.page.modified=false;
            }
            header.page=null;
            lruSize--;
            System.arraycopy( pageLRU, 1, pageLRU, 0, lruSize);
        }
    }

    private void promotePage( int pageIndex)
    {
        synchronized ( pageLRU)
        {
            for ( int i=lruSize-1; i>=0; i--)
            {
                if ( pageLRU[i]==pageIndex)
                {
                    if ( i<lruSize-1)
                    {
                        System.arraycopy( pageLRU, i+1, pageLRU, i,
                            lruSize-1-i);
                        pageLRU[lruSize]=pageIndex;
                    }
                    break;
                }
            }
        }
    }

    EntryPage makePageCurrent( StreamPair streams, int pageIndex)
        throws ClassNotFoundException, DiskAllocatorException, IOException
    {
        pageFlushLock.enterProtected();
        EntryPageHeader header=(EntryPageHeader)pages.get( pageIndex);
        if ( header.page!=null)
        {
            promotePage( pageIndex);
            return header.page;
        }
        pageFlushLock.leaveProtected();
        pageFlushLock.enterCritical();
        header=(EntryPageHeader)pages.get( pageIndex);
        if ( header.page!=null)
        {
            pageFlushLock.criticalToProtected();
            promotePage( pageIndex);
        }
        else
        {
            try
            {
                makeRoomInCache( streams);
                pageLRU[lruSize++]=pageIndex;
                header.page=(EntryPage)streams.readObject( header.offset);
            }
            finally
            {
                pageFlushLock.criticalToProtected();
            }
        }
        return header.page;
    }
}