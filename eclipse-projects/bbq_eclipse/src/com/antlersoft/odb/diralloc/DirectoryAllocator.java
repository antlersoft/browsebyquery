
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

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import com.antlersoft.odb.DiskAllocator;
import com.antlersoft.odb.DiskAllocatorException;
import com.antlersoft.odb.IndexObjectStore;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectKey;
import com.antlersoft.odb.ObjectStoreException;
import com.antlersoft.util.RandomInputStream;
import com.antlersoft.util.RandomOutputStream;

import com.antlersoft.util.NetByte;
import com.antlersoft.util.Semaphore;

public class DirectoryAllocator implements IndexObjectStore
{
    public DirectoryAllocator( File file)
        throws ObjectStoreException
    {
        try
        {
            if ( ! file.exists())
                file.mkdirs();
            baseDirectory=file;
            File allocatorFile=new File( file, "overhead");
            overhead=new DiskAllocator( allocatorFile, INITIAL_REGION_SIZE,
                10240, 204800, 0);

            overheadStreams=new StreamPair( overhead);
            rootModified=false;
            offsets=new byte[INITIAL_REGION_SIZE];

            if ( overhead.isNewFile())
            {
                classList=new ClassList();
                entryList=new EntryPageList();
                rootObject=null;
                entryOffset=overheadStreams.writeObject( entryList, 0);
                classOffset=overheadStreams.writeObject( classList, 0);
                rootOffset=0;
            }
            else
            {
                int offset=overhead.getInitialRegion();
                byte[] offsetBuffer=overhead.read( offset, INITIAL_REGION_SIZE);
                entryOffset=NetByte.quadToInt( offsetBuffer, 0);
                classOffset=NetByte.quadToInt( offsetBuffer, 4);
                rootOffset=NetByte.quadToInt( offsetBuffer, 8);
                entryList=(EntryPageList)overheadStreams.readObject(
                    entryOffset);
                entryList.initialize( overheadStreams);
                classList=(ClassList)overheadStreams.readObject( classOffset);
                if ( rootOffset!=0)
                    rootObject=(Serializable)overheadStreams.
                        readObject( rootOffset);
            }
            // Traverse class list; create class and index maps and stream
            // pairs
            classList.classMap=new HashMap();
            classList.classChangeLock=new Semaphore();
            classList.indexMap=new HashMap();
            indexPageFlushLock=new Semaphore();
            for ( Iterator i=classList.classEntries.iterator(); i.hasNext();)
            {
                ClassEntry entry=(ClassEntry)i.next();
                classList.classMap.put( Class.forName( entry.className),
                    entry);
                DiskAllocator allocator=new DiskAllocator(
                    new File( baseDirectory,
                    entry.fileName), 4, 128, 102400,
                    DiskAllocator.FORCE_EXIST);
                entry.objectStreams=new StreamPair( allocator);
                if ( entry.indices.size()>0)
                {
                    entry.indexStreams=new StreamPair(
                        new DiskAllocator( new File( baseDirectory,
                            entry.fileName+"i"), 4, 10240, 102400,
                            DiskAllocator.FORCE_EXIST));
                    for ( Iterator j=entry.indices.iterator(); j.hasNext();)
                    {
                        IndexEntry indexEntry=(IndexEntry)j.next();
                        indexEntry.index=new Index( this,
                            indexEntry, entry.indexStreams);
                        classList.indexMap.put( indexEntry.indexName,
                            indexEntry.index);
                    }
                }
            }
        }
        catch ( ClassNotFoundException cnfe)
        {
            throw new ObjectStoreException( "Should never happen", cnfe);
        }
        catch ( IOException ioe)
        {
ioe.printStackTrace();
            throw new ObjectStoreException( "Failed to initialize", ioe);
        }
        catch ( DiskAllocatorException dae)
        {
            throw new ObjectStoreException( "Allocator failure", dae);
        }
        // Initialize index page cache
        indexPageMap=new HashMap( INDEX_PAGE_CACHE_SIZE+1);
        indexPageLRU=new ArrayList( INDEX_PAGE_CACHE_SIZE+1);
    }

    public Iterator getAll(Class toRetrieve) throws ObjectStoreException
    {
        return classList.getClassIterator( toRetrieve);
    }

    public void defineIndex(String indexName, Class indexedClass,
        KeyGenerator keyGen, boolean descending, boolean unique)
        throws ObjectStoreException
    {
        classList.defineIndex( indexName, indexedClass,
            keyGen, descending, unique, this, baseDirectory);
    }

    public void removeIndex(String indexName) throws ObjectStoreException
    {
        throw new ObjectStoreException( "Removing indexes not yet supported");
    }

    public void removeClass( Class toRemove)
        throws ObjectStoreException
    {
        throw new ObjectStoreException( "Removing classes not yet supported");
    }

    /**
     * Returns the ObjectKey of an object that exactly matches the search
     * key, or null if no such object exists.
     */
    public ObjectKey findObject( String indexName, Comparable toFind)
        throws ObjectStoreException
    {
        classList.classChangeLock.enterProtected();
        try
        {
            Index index=(Index)classList.indexMap.get( indexName);
            if ( index==null)
                throw new ObjectStoreException( "Index "+indexName+
                " undefined");
            return index.findObject( toFind);
        }
        finally
        {
            classList.classChangeLock.leaveProtected();
        }
    }

    public com.antlersoft.odb.IndexIterator
        greaterThanOrEqualEntries(String indexName, Comparable toFind)
        throws ObjectStoreException
    {
        classList.classChangeLock.enterProtected();
        try
        {
            Index index=(Index)classList.indexMap.get( indexName);
            if ( index==null)
                throw new ObjectStoreException( "Index "+indexName+
                " undefined");
            return index.getIterator( toFind);
        }
        finally
        {
            classList.classChangeLock.leaveProtected();
        }
    }

    public ObjectKey insert(Serializable insertObject)
        throws ObjectStoreException
    {
        try
        {
            ClassEntry classEntry=classList.getEntry( insertObject.getClass(),
                baseDirectory);
            DAKey result=entryList.getNewKey( overheadStreams,
                classEntry.index, classEntry.reuseCount);
            int region=classEntry.objectStreams.writeObjectWithPrefix(
                insertObject, 0, result.index, result.reuseCount);
            entryList.setRegion( result, region, overheadStreams);
            for ( Iterator i=classEntry.indices.iterator(); i.hasNext();)
                ((IndexEntry)i.next()).index.insertKey( result, insertObject);
            return result;
        }
        catch ( ClassNotFoundException cnfe)
        {
            throw new ObjectStoreException( "insert: class not found",
                cnfe);
        }
        catch ( IOException ioe)
        {
            throw new ObjectStoreException( "insert: IO Error",
                ioe);
        }
        catch ( DiskAllocatorException dae)
        {
            throw new ObjectStoreException(
                "insert: DiskAllocatorError", dae);
        }
        finally
        {
            classList.classChangeLock.leaveProtected();
        }
    }

    public Serializable retrieve(ObjectKey retrieveKey) throws ObjectStoreException
    {
        return (Serializable)entryList.getObject( (DAKey)retrieveKey,
            overheadStreams,
            classList);
    }

    public void update(ObjectKey replaceKey, Serializable toReplace) throws ObjectStoreException
    {
        entryList.updateObject( (DAKey)replaceKey, toReplace, overheadStreams,
            classList);
    }

    public void delete(ObjectKey deleteKey) throws ObjectStoreException
    {
        entryList.deleteObject( (DAKey)deleteKey, overheadStreams, classList);
    }

    public Serializable getRootObject() throws ObjectStoreException
    {
        return rootObject;
    }

    public synchronized void updateRootObject(Serializable newRoot)
        throws ObjectStoreException
    {
        rootObject=newRoot;
        rootModified=true;
    }

    public synchronized void sync() throws ObjectStoreException
    {
        try
        {
            boolean initialModified=false;

            // Sync index pages
            indexPageFlushLock.enterCritical();
            try
            {
                for ( Iterator i=indexPageLRU.iterator(); i.hasNext();)
                {
                    if ( flushIndexPage( (IndexPage)i.next()))
                        i=indexPageLRU.iterator();
                }
            }
            finally
            {
                indexPageFlushLock.leaveCritical();
            }
            int newEntryOffset=entryList.sync( overheadStreams, entryOffset);
            int newClassOffset=classList.sync( overheadStreams, classOffset);
            int newRootOffset=rootOffset;

            if ( rootModified)
            {
                if ( rootObject==null)
                    newRootOffset=0;
                else
                    newRootOffset=overheadStreams.writeObject( rootObject,
                        rootOffset);
                rootModified=false;
            }
            if ( newEntryOffset!=entryOffset)
            {
                entryOffset=newEntryOffset;
                initialModified=true;
            }
            if ( newClassOffset!=classOffset)
            {
                classOffset=newClassOffset;
                initialModified=true;
            }
            if ( newRootOffset!=rootOffset)
            {
                rootOffset=newRootOffset;
                initialModified=true;
            }
            if ( initialModified)
            {
                NetByte.intToQuad( entryOffset, offsets, 0);
                NetByte.intToQuad( classOffset, offsets, 4);
                NetByte.intToQuad( rootOffset, offsets, 8);
                overhead.write( overhead.getInitialRegion(), offsets);
            }
            overhead.sync();
        }
        catch ( ClassNotFoundException cnfe)
        {
            throw new ObjectStoreException( "sync: class not found",
                cnfe);
        }
        catch ( IOException ioe)
        {
            throw new ObjectStoreException( "IO Error syncing",
                ioe);
        }
        catch ( DiskAllocatorException dae)
        {
            throw new ObjectStoreException(
                "DiskAllocator error syncing", dae);
        }
    }

    public void rollback() throws ObjectStoreException
    {
        throw new ObjectStoreException(
            "DirectoryAllocatorStore does not support rollback");
    }

    public synchronized void close() throws ObjectStoreException
    {
        synchronized ( classList)
        {
            synchronized ( entryList)
            {
                sync();
                try
                {
                    classList.close();
                    overheadStreams.close();
                }
                catch ( IOException ioe)
                {
                    throw new ObjectStoreException( "IO Error closing",
                        ioe);
                }
            }
        }
    }

    /**
     * List of classes this database knows about; class entry free list
     */
    private ClassList classList;

    /**
     * List of entry pages for this database; entry page free list
     */
    private EntryPageList entryList;

    /**
     * DiskAllocator that stores ClassList, EntryPageList, and EntryPages
     */
    private DiskAllocator overhead;

    /**
     * The directory that contains everything
     */
    private File baseDirectory;

    private Serializable rootObject;
    private int classOffset;
    private int entryOffset;
    private int rootOffset;
    private boolean rootModified;
    private byte[] offsets;
    private StreamPair overheadStreams;

    /** Fields for index page cache management */
    private HashMap indexPageMap; // IndexPageKey, IndexPage
    private ArrayList indexPageLRU;
    private Semaphore indexPageFlushLock;

    private static final int INITIAL_REGION_SIZE=12;
    private static final int INDEX_PAGE_CACHE_SIZE=100;

    void startIndexPageNoFlush()
    {
        indexPageFlushLock.enterProtected();
    }

    void endIndexPageNoFlush()
        throws ObjectStoreException
    {
        indexPageFlushLock.leaveProtected();
        if ( indexPageLRU.size()>INDEX_PAGE_CACHE_SIZE)
        {
            indexPageFlushLock.enterCritical();
            try
            {
                while ( indexPageLRU.size()>INDEX_PAGE_CACHE_SIZE)
                {
                    IndexPage toFlush=(IndexPage)indexPageLRU.get( 0);
                    indexPageLRU.remove( 0);
                    indexPageMap.remove( toFlush.thisPage);
                    flushIndexPage( toFlush);
                }
            }
            catch ( ClassNotFoundException cnfe)
            {
                throw new ObjectStoreException( "Index page class not found",
                    cnfe);
            }
            catch ( IOException ioe)
            {
                throw new ObjectStoreException( "IO Error paging index",
                    ioe);
            }
            catch ( DiskAllocatorException dae)
            {
                throw new ObjectStoreException(
                    "DiskAllocator error paging index", dae);
            }
            finally
            {
                indexPageFlushLock.leaveCritical();
            }
        }
    }

    void addNewIndexPageToCache( IndexPage toAdd)
    {
        synchronized ( indexPageMap)
        {
            indexPageMap.put( toAdd.thisPage, toAdd);
            indexPageLRU.add( toAdd);
        }
    }

    void dirtyClassList()
    {
        classList.listModified=true;
    }

    void freeIndexPage( IndexPage toFree)
        throws ObjectStoreException
    {
        synchronized ( indexPageMap)
        {
            indexPageMap.remove( toFree.thisPage);
            indexPageLRU.remove( toFree);
            try
            {
                toFree.thisPage.index.streams.free( toFree.thisPage.offset);
            }
            catch ( IOException ioe)
            {
                throw new ObjectStoreException( "I/O Error freeing index page",
                    ioe);
            }
            catch ( DiskAllocatorException dae)
            {
                throw new ObjectStoreException( "Error freeing index page:",
                    dae);
            }
        }
    }

    void fixIndexPageParent( IndexPageKey newParentKey,
        int[] offsets, int start, int len)
    {
        synchronized ( indexPageMap)
        {
            for ( Iterator i=indexPageLRU.iterator(); i.hasNext();)
            {
                IndexPage page=(IndexPage)i.next();
                if ( page.thisPage.index==newParentKey.index)
                {
                    for ( int j=start; j<start+len; j++)
                    {
                        if ( offsets[j]==page.thisPage.offset)
                        {
                            page.thisPage.parent=newParentKey;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Assumes startIndexPageNoFlush is called
     */
    IndexPage getIndexPageByKey( IndexPageKey key)
        throws ObjectStoreException
    {
        try
        {
            IndexPage result=getIndexPageByKeyInternal( key);

            return result;
        }
        catch ( ClassNotFoundException cnfe)
        {
            throw new ObjectStoreException( "Index page class not found",
                cnfe);
        }
        catch ( IOException ioe)
        {
            throw new ObjectStoreException( "IO Error paging index",
                ioe);
        }
        catch ( DiskAllocatorException dae)
        {
            throw new ObjectStoreException(
                "DiskAllocator error paging index", dae);
        }
    }

    private IndexPage getIndexPageByKeyInternal( IndexPageKey key)
        throws ClassNotFoundException, IOException, DiskAllocatorException
    {
        synchronized ( indexPageMap)
        {
            IndexPage result=(IndexPage)indexPageMap.get( key);
            if ( result==null)
            {
                result=(IndexPage)key.index.streams.readObject( key.offset);
                result.modified=false;
                result.thisPage=key;
                indexPageMap.put( key, result);
                indexPageLRU.add( result);
            }
            else
            {
                indexPageLRU.remove( result);
                indexPageLRU.add( result);
            }

            return result;
        }
    }

    /**
     * This method is only called when indexPageFlushLock is in critical.
     * That means index pages are not stable, so no index operations
     * can be happening.  So we don't have to coordinate with index operations.
     */
    private boolean flushIndexPage( IndexPage toDump)
        throws ClassNotFoundException, IOException, DiskAllocatorException,
            ObjectStoreException
    {
        boolean modifiedPages=false;
        if ( toDump.modified)
        {
            toDump.modified=false;
            int newOffset=
                toDump.thisPage.index.streams.writeObject( toDump,
                    toDump.thisPage.offset);
            if ( newOffset!=toDump.thisPage.offset)
            {
                toDump.thisPage.offset=newOffset;
                if ( toDump.thisPage.parent==null)
                {
                    toDump.thisPage.index.entry.startPageOffset
                        =toDump.thisPage.offset;
                    classList.listModified=true;
                }
                else
                {
                    IndexPage parent=getIndexPageByKeyInternal( toDump.
                        thisPage.parent);
                    int fixOffset=
                        parent.thisPage.index.binarySearch(
                        parent.keyArray, 0, parent.size-1,
                        toDump.keyArray[0]);
                    if ( fixOffset<0)
                        throw new ObjectStoreException(
                        "Corrupt index when fixing offset in parent");
                    parent.nextOffsetArray[fixOffset]=
                        toDump.thisPage.offset;
                    parent.modified=true;
                    modifiedPages=true;
                }
            }
        }
        return modifiedPages;
    }
}