
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

public class IndexObjectDB extends ObjectDB
{
    private IndexObjectStore store;

    public IndexObjectDB( IndexObjectStore indexStore)
    {
        super( indexStore);
        store=indexStore;
    }

    public void defineIndex( String indexName, Class indexedClass,
        KeyGenerator keyGen, boolean descending, boolean unique)
        throws ObjectStoreException
    {
        store.defineIndex( indexName, indexedClass, keyGen, descending,
            unique);
    }

    public void removeIndex( String indexName)
        throws ObjectStoreException
    {
        store.removeIndex( indexName);
    }

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
        private Iterator base;
        DBAllIterator( Iterator i)
        {
            base=i;
        }

        public boolean hasNext()
        {
            return base.hasNext();
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

    public static Comparable ObjectRefIndexHelper( ObjectRef ref)
    {
    	return (Comparable)ref.impl.objectKey;
    }
}
