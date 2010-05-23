
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
package com.antlersoft.odb;

import java.util.Iterator;
import java.util.Properties;

public class IndexObjectDB extends ObjectDB
{
    private IndexObjectStore store;

    public IndexObjectDB( IndexObjectStore indexStore)
    {
        super( indexStore);
        store=indexStore;
    }

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
     * @param indexName Name that identifies index; must be unique in database
     * @param indexedClass Class that index applies to; index applies only to this class, not
     * to sub-classes
     * @param keyGen Object that will convert an object of indexedClass to a Comparable key for the index
     * @param descending Set to true to maintain index in descending order of the keys
     * @param unique Set to true to indicate that the key values will be unique for all objects indexed
     * @param indexProperties Store-specific index properties.  All stores must
     * accept a null for this value, which will result in default properties
     * being used.
     * @throws ObjectStoreException
     */
    public void defineIndex( String indexName, Class indexedClass,
        KeyGenerator keyGen, boolean descending, boolean unique, Properties indexProperties)
        throws ObjectStoreException
    {
        store.defineIndex( indexName, indexedClass, keyGen, descending,
            unique, indexProperties);
    }

    /**
     * Exactly like defineIndex, but no exception if index already exists
     * @param indexName Name that identifies index; must be unique in database
     * @param indexedClass Class that index applies to; index applies only to this class, not
     * to sub-classes
     * @param keyGen Object that will convert an object of indexedClass to a Comparable key for the index
     * @param descending Set to true to maintain index in descending order of the keys
     * @param unique Set to true to indicate that the key values will be unique for all objects indexed
     * @param indexProperties Store-specific index properties.  All stores must
     * accept a null for this value, which will result in default properties
     * being used.
     */
    public void redefineIndex( String indexName, Class indexedClass,
            KeyGenerator keyGen, boolean descending, boolean unique, Properties indexProperties)
    {
    	try
    	{
    		defineIndex( indexName, indexedClass,
    	            keyGen, descending, unique, indexProperties);
    	}
    	catch ( IndexExistsException ie)
    	{
    		// Don't care
    	}
    }
    
    public void removeIndex( String indexName)
        throws ObjectStoreException
    {
        store.removeIndex( indexName);
    }

    /**
     * Returns an object in an index with a given key value
     * @param indexName Name of index
     * @param key Value to find in index
     * @return Object in index with that value for the key, or null if there is no such object
     * @throws ObjectStoreException
     */
    public Object findObject( String indexName, Comparable key)
        throws ObjectStoreException
    {
        ObjectKey ref=store.findObject( indexName, key);
        if ( ref==null)
            return null;
        return getObjectByKey( ref);
    }

    public IndexIterator greaterThanOrEqualEntries( String indexName,
        Comparable key)
        throws ObjectStoreException
    {
        return new DBIndexIterator( store.greaterThanOrEqualEntries( indexName,
            key));
    }

    public Iterator getAll( Class storedClass)
        throws ObjectStoreException
    {
        return new DBAllIterator( store.getAll( storedClass));
    }

    class DBIndexIterator implements IndexIterator
    {
        private IndexIterator base;
        DBIndexIterator( IndexIterator i)
        {
            base=i;
        }

        public boolean hasNext()
        {
            return base.hasNext();
        }

        public boolean isExactMatch()
        {
            return base.isExactMatch();
        }

        public Object next()
        {
            return getObjectByKey( (ObjectKey)base.next());
        }

        public void remove()
        {
            base.remove();
        }
    }

    class DBAllIterator implements Iterator
    {
        private Iterator<ObjectKey> base;
        DBAllIterator( Iterator<ObjectKey> i)
        {
            base=i;
        }

        public boolean hasNext()
        {
            return base.hasNext();
        }

        public Object next()
        {
            return getObjectByKey(base.next());
        }

        public void remove()
        {
            base.remove();
        }
    }

    public static Comparable ObjectRefIndexHelper( ObjectRef ref)
    {
    	return (Comparable)ref.impl.objectKey;
    }
}
