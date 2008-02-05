
/*
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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.antlersoft.odb.DiskAllocator;
import com.antlersoft.odb.DiskAllocatorException;
import com.antlersoft.odb.IndexObjectStore;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectKey;
import com.antlersoft.odb.ObjectStoreException;
import com.antlersoft.odb.ObjectStreamCustomizer;

import com.antlersoft.util.NetByte;
import com.antlersoft.util.Semaphore;

/**
 * This IndexObjectStore saves objects in a set of files in a
 * directory.  Each file is a DiskAllocator.
 *
 * The directory will always have one file called "overhead".  There
 * will be an additional file for each class type that is stored.
 * These files are named 1, 2, 3 etc. up to N for each class type.
 * If a class type has any indices defined, an additional
 * DiskAllocator is defined that stores the index information
 * for that class.  The name of this file is the class file name
 * with an i appended.  So if the class stored in file 5 has one or more
 * indices, that information is stored in 5i.
 *
 * The point of an object database is to handle more data than
 * will fit in memory comfortably.  The DiskAllocatorStore was a step
 * in this direction, since the actual class data was kept on the disk
 * (except for the ObjectDB cache), but all the metadata for
 * finding objects on demand,
 * as well as the root object,  was always kept in memory.  This became
 * impractical with large databases; not just because you
 * ran out of memory, but because the time required to sync
 * the in-memory state with the disk became too large.
 * In the DirectoryAllocator,
 * only a small sub-set of this data needs to be cached in memory;
 * the rest is kept on the disk in the overhead allocator.
 *
 * The largest portion of the metadata
 */
public class DirectoryAllocator implements IndexObjectStore
{
    private static Logger logger=Logger.getLogger( DirectoryAllocator.class.getName());
    
    public static final int FAVORED_CHUNK_SIZE=10240;
    public static final String ENTRIES_PER_PAGE="ENTRIES_PER_PAGE";

    public DirectoryAllocator( File file, CustomizerFactory factory)
        throws ObjectStoreException
    {
        try
        {
            if ( ! file.exists())
                file.mkdirs();
            baseDirectory=file;
            File allocatorFile=new File( file, "overhead");
            overhead=new DiskAllocator( allocatorFile, INITIAL_REGION_SIZE,
            		FAVORED_CHUNK_SIZE, 204800, 0);

            overheadStreams=new StreamPair( overhead,
                factory.getCustomizer( EntryPage.class));
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
                int version=NetByte.quadToInt( offsetBuffer, 0);
                if ( version!=DIR_ALLOC_VERSION)
                {
                	throw new ObjectStoreException( "Mismatched version:" + version);
                }
                entryOffset=NetByte.quadToInt( offsetBuffer, 4);
                classOffset=NetByte.quadToInt( offsetBuffer, 8);
                rootOffset=NetByte.quadToInt( offsetBuffer, 12);
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
            classList.factory=factory;
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
                ObjectStreamCustomizer customizer=factory.getCustomizer(
                    Class.forName( entry.className));
                entry.objectStreams=new StreamPair( allocator,
                    customizer);
                if ( entry.indices.size()>0)
                {
                    entry.indexStreams=new StreamPair(
                        new DiskAllocator( new File( baseDirectory,
                            entry.fileName+"i"), 4, FAVORED_CHUNK_SIZE, 102400,
                            DiskAllocator.FORCE_EXIST), customizer);
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
            emergencyCleanup( new ObjectStoreException( "Should never happen", cnfe));
        }
        catch ( IOException ioe)
        {
ioe.printStackTrace();
            emergencyCleanup( new ObjectStoreException( "Failed to initialize", ioe));
        }
        catch ( DiskAllocatorException dae)
        {
            emergencyCleanup( new ObjectStoreException( "Allocator failure", dae));
        }
        // Initialize index page cache
        indexPageMap=new HashMap( INDEX_PAGE_CACHE_SIZE+1);
        indexPageLRU=new ArrayList( INDEX_PAGE_CACHE_SIZE+1);
    }

    public DirectoryAllocator( File file)
    {
        this( file, CustomizerFactory.BASE_FACTORY);
    }

    public Iterator getAll(Class toRetrieve) throws ObjectStoreException
    {
        return classList.getClassIterator( toRetrieve);
    }

    public void defineIndex(String indexName, Class indexedClass,
        KeyGenerator keyGen, boolean descending, boolean unique, Properties indexProperties)
        throws ObjectStoreException
    {
    	int entriesPerPage=200; // A number pulled out of the air
    	if ( indexProperties!=null)
    	{
    		String entries=indexProperties.getProperty( ENTRIES_PER_PAGE);
    		if ( entries!=null)
    			entriesPerPage=Integer.parseInt( entries);
    	}
        classList.defineIndex( indexName, indexedClass,
            keyGen, descending, unique, 
            entriesPerPage, this, baseDirectory);
    }
    
    public void logAllStatistics() throws ObjectStoreException
    {
    	try
    	{
    		classList.classChangeLock.enterProtected();
    		for ( Iterator i=classList.indexMap.values().iterator(); i.hasNext();)
    		{
    			Index index=(Index)i.next();
    			logger.info( index.entry.indexName+index.getStatistics().toString());
    		}
    	}
    	finally
    	{
    		classList.classChangeLock.leaveProtected();
    	}
    }

    public Object getIndexStatistics( String indexName) throws ObjectStoreException
    {
        classList.classChangeLock.enterProtected();
        try
        {
            Index index=(Index)classList.indexMap.get( indexName);
            if ( index==null)
                throw new ObjectStoreException( "getIndexStatistics: Index "+indexName+
                " undefined");
            return index.getStatistics();
        }
        finally
        {
            classList.classChangeLock.leaveProtected();
        }
    }
    
    /**
     * Walks one (or each) index, insuring it is correct with keys in the write order, and optionally that all objects
     * are accessible.
     * @param indexName Name of index to validate, or null to validate all indices
     * @param checkReferences If true, checks all objects referenced by the index
     * @throws ObjectStoreException Throws if any problem with the index, with message describing the problem
     */
    public void validateIndex( String indexName, boolean checkReferences)
    throws ObjectStoreException
    {
    	try
    	{
    		classList.classChangeLock.enterProtected();
    		if ( indexName==null)
    		{
        		for ( Iterator i=classList.indexMap.values().iterator(); i.hasNext();)
        		{
        			((Index)i.next()).validate(checkReferences);
        		}
    		}
    		else
    		{
    			Index named=(Index)classList.indexMap.get(indexName);
                if ( named==null)
                    throw new ObjectStoreException( "validateIndex: Index "+indexName+
                    " undefined");
                named.validate(checkReferences);
    		}
    	}
    	finally
    	{
    		classList.classChangeLock.leaveProtected();
    	}
    	
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
        DAKey result=null;
        try
        {
            ClassEntry classEntry=classList.getEntry( insertObject.getClass(),
                baseDirectory);
            result=entryList.getNewKey( overheadStreams,
                classEntry.index, classEntry.reuseCount);
            int region=classEntry.objectStreams.writeObjectWithPrefix(
                insertObject, 0, result.index, result.reuseCount);
            entryList.setRegion( result, region, overheadStreams);
            for ( Iterator i=classEntry.indices.iterator(); i.hasNext();)
                ((IndexEntry)i.next()).index.insertKey( result, insertObject);
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
        return result;
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
                    flushIndexPage( (IndexPage)i.next());
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
            	NetByte.intToQuad( DIR_ALLOC_VERSION, offsets, 0);
                NetByte.intToQuad( entryOffset, offsets, 4);
                NetByte.intToQuad( classOffset, offsets, 8);
                NetByte.intToQuad( rootOffset, offsets, 12);
                overhead.write( overhead.getInitialRegion(), offsets);
            }
            overhead.sync();
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
            	ObjectStoreException storedException=null;
            	try
            	{
            		sync();
            	}
            	catch ( ObjectStoreException ose)
            	{
            		storedException=ose;
            	}
                try
                {
                    classList.close();
                }
                catch ( IOException ioe)
                {
                	if ( storedException==null)
                		storedException=new ObjectStoreException( "IO error closing classlist", ioe);
                	else
                		storedException=new ObjectStoreException( "Exception closing classlist after sync exception: "+ioe.getMessage(), storedException);
                }
                try
                {
                    overheadStreams.close();
                }
                catch ( IOException ioe)
                {
                	if ( storedException==null)
                		storedException=new ObjectStoreException( "IO error closing overhead streams", ioe);
                	else
                		storedException=new ObjectStoreException( "Exception closing overhead streams after class list exception: "+ioe.getMessage(), storedException);
                }
                if ( storedException!=null)
                	emergencyCleanup( storedException);
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

    private static final int INITIAL_REGION_SIZE=16;
    private static final int INDEX_PAGE_CACHE_SIZE=100;
    /** 
     * BBQ magic number; the last byte is the version number, it gets incremented with each version
     * the current version is 1.
     */
    private static final int DIR_ALLOC_VERSION=0x42425101;

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
            if ( logger.isLoggable(Level.FINE))
                logger.fine( "new index page "+toAdd.thisPage.offset+( toAdd.thisPage.parent!=null ? " parent "+toAdd.thisPage.parent.offset : " top page"));
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
                toFree.thisPage.index.streams.freeImmovableObject( toFree.thisPage.offset);
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
                if ( page.thisPage.index==newParentKey.index
                     && page.thisPage!=newParentKey)
                {
                    for ( int j=start; j<start+len; j++)
                    {
                        if ( offsets[j]==page.thisPage.offset)
                        {
                            if ( logger.isLoggable( Level.FINER))
                                logger.finer( "Change parent offset of page at "
                                              +page.thisPage.offset+" from "+
                                              page.thisPage.parent.offset+" to "
                                              +newParentKey.offset);
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
    
    private void emergencyCleanup( ObjectStoreException underlying)
    throws ObjectStoreException
    {
    	if ( classList!=null && classList.classEntries!=null)
    	{
	    	for ( Iterator i=classList.classEntries.iterator(); i.hasNext();)
	    	{
		    	try
		    	{
		    		ClassEntry entry=(ClassEntry)i.next();
		    		if ( entry.objectStreams!=null)
		    		{
		    			entry.objectStreams.close();
		    		}
		    		if ( entry.indexStreams!=null)
		    		{
		    			entry.indexStreams.close();
		    		}
		    	}
		    	catch ( Exception e)
		    	{
		    		underlying=new ObjectStoreException( "Error closing object streams: "+e.getMessage(), underlying);
		    	}
	    	}
    	}
    	if ( overhead!=null)
    	{
	    	try
	    	{
	    		overhead.close();
	    	}
	    	catch ( Exception e)
	    	{
	    		underlying=new ObjectStoreException( "Error closing object streams: "+e.getMessage(), underlying);
	    	}   		
    	}
    	throw underlying;
    }

    private IndexPage getIndexPageByKeyInternal( IndexPageKey key)
        throws ClassNotFoundException, IOException, DiskAllocatorException
    {
        synchronized ( indexPageMap)
        {
            IndexPage result=(IndexPage)indexPageMap.get( key);
            if ( result==null)
            {
                if ( logger.isLoggable( Level.FINE))
                    logger.fine( "Read index page from store at "+key.offset+( key.parent!=null ? " parent "+key.parent.offset : " top page"));
                result=(IndexPage)key.index.streams.readImmovableObject( key.offset);
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
    private void flushIndexPage( IndexPage toDump)
        throws IOException, DiskAllocatorException
    {
        if ( toDump.modified)
        {
            toDump.modified=false;
            toDump.thisPage.index.streams.writeImmovableObject( toDump,
                toDump.thisPage.offset);
        }
    }
}
