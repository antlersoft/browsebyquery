
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.antlersoft.odb.DiskAllocator;
import com.antlersoft.odb.DiskAllocatorException;
import com.antlersoft.odb.IndexExistsException;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectStoreException;

import com.antlersoft.util.Semaphore;

class ClassList implements Serializable
{
    ArrayList classEntries;
    private ArrayList freeList;
    transient boolean listModified;
    transient HashMap classMap;
    transient Semaphore classChangeLock;
    /**
     * Map of index names to indices
     */
    transient HashMap indexMap; // String, Index


    /**
     * Called only when a new DirectoryAllocator is set up
     */
    ClassList()
    {
        classEntries=new ArrayList();
        freeList=new ArrayList();
        classMap=new HashMap();
        listModified=true;
        classChangeLock=new Semaphore();
    }

    /**
     * Makes sure all cached data is flushed to disk
     */
    int sync( StreamPair streams, int classOffset)
        throws DiskAllocatorException, IOException
    {
        classChangeLock.enterProtected();
        try
        {
            for ( Iterator i=classEntries.iterator(); i.hasNext();)
            {
                ClassEntry entry=(ClassEntry)i.next();
                synchronized ( entry)
                {
                    entry.objectStreams.sync();
                    if ( entry.indexStreams!=null)
                        entry.indexStreams.sync();
                }
            }
            if ( listModified)
            {
                classOffset=streams.writeObject( this, classOffset);
            }
        }
        finally
        {
            classChangeLock.leaveProtected();
        }
        return classOffset;
    }

    public void defineIndex(String indexName, Class indexedClass,
        KeyGenerator keyGen, boolean descending, boolean unique,
        DirectoryAllocator manager, File baseDirectory)
        throws ObjectStoreException
    {
        try
        {
            // Make sure class exists to us
            getEntry( indexedClass, baseDirectory);
        }
        finally
        {
            classChangeLock.leaveProtected();
        }
        classChangeLock.enterCritical();
        try
        {
            ClassEntry entry=(ClassEntry)classMap.get( indexedClass);
            if ( entry==null)
                // Very rare circumstance; other thread snatched class from us
                throw new ObjectStoreException( "Class "+indexedClass.getName()
                    +" was removed.");
            if ( indexMap.get( indexName)!=null)
                throw new IndexExistsException( indexName);
            listModified=true;
            if ( entry.indexStreams==null)
            {
                entry.indexStreams=new StreamPair(
                    new DiskAllocator( new File( baseDirectory,
                        entry.fileName+"i"), 4, 10240, 102400, 0));
            }
            IndexEntry indexEntry=Index.createIndexEntry( indexName,
                keyGen, descending, unique, manager, entry.indexStreams);
            entry.indices.add( indexEntry);
            indexMap.put( indexName, indexEntry.index);

            // Add existing objects to index
            for ( Iterator i=entry.objectStreams.allocator.iterator();
                i.hasNext(); )
            {
                Object nextObject=entry.objectStreams.readObjectWithPrefix(
                    ((Integer)i.next()).intValue());
                indexEntry.index.insertKey( new DAKey(
                    entry.objectStreams.first, entry.objectStreams.second),
                    nextObject);
            }
        }
        catch ( ClassNotFoundException cnfe)
        {
            throw new ObjectStoreException(
                "Class not found while creating index", cnfe);
        }
        catch ( DiskAllocatorException dae)
        {
            throw new ObjectStoreException( "Error adding index: ", dae);
        }
        catch ( IOException ioe)
        {
            throw new ObjectStoreException( "I/O Error adding index: ", ioe);
        }
        finally
        {
            classChangeLock.leaveCritical();
        }
    }

    /**
     * Close all streams for the class allocators
     */
    void close()
        throws IOException
    {
        classChangeLock.enterCritical();
        try
        {
            for ( Iterator i=classEntries.iterator(); i.hasNext();)
            {
                ClassEntry entry=(ClassEntry)i.next();
                synchronized ( entry)
                {
                    entry.objectStreams.close();
                    if ( entry.indexStreams!=null)
                        entry.indexStreams.close();
                }
            }
        }
        finally
        {
            classChangeLock.leaveCritical();
        }
    }

    Iterator getClassIterator( Class toRetrieve)
        throws ObjectStoreException
    {
        classChangeLock.enterProtected();
        try
        {
            ClassEntry entry=(ClassEntry)classMap.get( toRetrieve);
            if ( entry==null)
                throw new NoSuchClassException( toRetrieve);
            try
            {
                return new ClassIterator( entry.objectStreams.allocator);
            }
            catch ( IOException ioe)
            {
                throw new ObjectStoreException( "Creating iterator:", ioe);
            }
        }
        finally
        {
            classChangeLock.leaveProtected();
        }
    }

    /**
     * Returns the entry corresponding to a class, or if no such entry
     * exists, adds that entry to the list.
     */
    ClassEntry getEntry( Class toFind, File baseDirectory)
        throws ObjectStoreException
    {
        classChangeLock.enterProtected();
        ClassEntry result=(ClassEntry)classMap.get( toFind);
        if ( result==null)
        {
            try
            {
                classChangeLock.leaveProtected();
                classChangeLock.enterCritical();
                // Check if added by another thread in the short interval
                // it was unprotected
                result=(ClassEntry)classMap.get( toFind);
                if ( result!=null)
                    return result;
                listModified=true;
                int freeSize=freeList.size();
                int classIndex;
                if ( freeSize>0)
                {
                    freeSize--;
                    classIndex=((Integer)freeList.get( freeSize)).intValue();
                    freeList.remove( freeSize);
                    result=(ClassEntry)classEntries.get( classIndex);
                    result.reuseCount= -result.reuseCount+1;
                    result.className=toFind.getName();
                    result.fileName=Integer.toString( classIndex);
                    result.indices=new ArrayList();
                    try
                    {
                        DiskAllocator allocator=new DiskAllocator(
                            new File( baseDirectory,
                            result.fileName), 4, 128, 102400,
                            DiskAllocator.FORCE_CREATE);
                        result.objectStreams=new StreamPair( allocator);
                    }
                    catch ( Exception dae)
                    {
                        throw new ObjectStoreException( "Failed creating allocator "+
                            result.fileName+" for class "+result.className, dae);
                    }
                }
                else
                {
                    classIndex=classEntries.size();
                    result=new ClassEntry();
                    result.reuseCount=0;
                    result.index=classEntries.size();
                    result.className=toFind.getName();
                    result.fileName=Integer.toString( classIndex);
                    result.indices=new ArrayList();
                    try
                    {
                        DiskAllocator allocator=new DiskAllocator(
                            new File( baseDirectory,
                            result.fileName), 4, 128, 102400,
                            DiskAllocator.FORCE_CREATE);
                        result.objectStreams=new StreamPair( allocator);
                    }
                    catch ( Exception dae)
                    {
                        throw new ObjectStoreException( "Failed creating allocator "+
                            result.fileName+" for class "+result.className, dae);
                    }
                    classEntries.add( result);
                }
                classMap.put( toFind, result);
            }
            finally
            {
                classChangeLock.criticalToProtected();
            }
        }
        return result;
    }
}