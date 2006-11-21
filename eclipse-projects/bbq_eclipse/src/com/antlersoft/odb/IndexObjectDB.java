
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

    public void defineIndex( String indexName, Class indexedClass,
        KeyGenerator keyGen, boolean descending, boolean unique, Properties indexProperties)
        throws ObjectStoreException
    {
        store.defineIndex( indexName, indexedClass, keyGen, descending,
            unique, indexProperties);
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
