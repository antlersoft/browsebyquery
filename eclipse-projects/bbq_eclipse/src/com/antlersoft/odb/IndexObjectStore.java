
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.odb;

import java.util.Iterator;

/**
 * An ObjectStore that organizes stored objects by their class
 * and supports indexed retrieval.
 */
public interface IndexObjectStore extends ObjectStore
{
    public static final boolean ASCENDING=false;
    public static final boolean DESCENDING=true;
    public static final boolean UNIQUE=true;
    public static final boolean DUPLICATES=false;

    /**
     * Returns an iterator of ObjectKey values for all stored objects of the
     * class.
     */
    public Iterator getAll( Class toRetrieve)
        throws ObjectStoreException;

    /**
     * Defines an index with the given name and KeyGenerator on the class.
     * If the descending flag is true, the natural ordering of the values
     * of the KeyGenerator will be reversed.  If the unique flag is true,
     * it is an error to try and store two different objects that generate
     * key values that compare equal (a UniqueValueException is thrown
     * when the object is stored).
     * <P>
     * If an index is defined for a class that already has data, the existing
     * values for the class are added to the index.
     */
    public void defineIndex( String indexName, Class indexedClass,
        KeyGenerator keyGen, boolean descending, boolean unique)
        throws ObjectStoreException;
    /**
     * Remove the named index from this database.
     */
    public void removeIndex( String indexName)
        throws ObjectStoreException;

    /**
     * Remove all objects with the given class (sub-classes don't count)
     */
    public void removeClass( Class toRemove)
        throws ObjectStoreException;

    /**
     * Returns the ObjectKey of an object that exactly matches the search
     * key, or null if no such object exists.
     */
    public ObjectKey findObject( String indexName, Comparable toFind)
        throws ObjectStoreException;

    /**
     * Returns an iterator over ObjectKey objects for the index
     * where the key>=toFind.  The value is the ObjectKey of the
     * object that produced the key from the index KeyGenerator.
     * If the index is defined in descending order, this iterator
     * with give less than or equal keys in descending order.
     */
    public IndexIterator greaterThanOrEqualEntries( String indexName,
        Comparable toFind)
        throws ObjectStoreException;
}