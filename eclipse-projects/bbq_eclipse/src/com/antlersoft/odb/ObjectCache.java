
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

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;

class ObjectCache
{
    private HashMap map;
    private ReferenceQueue queue;
    ObjectCache()
    {
        map=new HashMap();
        queue=new ReferenceQueue();
    }

    Object get( Object key)
    {
        clearQueue();
        CacheValue value=(CacheValue)map.get( key);
        if ( value==null)
            return null;
        return value.get();
    }

    void put( ObjectKey key, Object value)
    {
        clearQueue();
        CacheValue newValue=new CacheValue( value, queue, key);
        map.put( key, newValue);
    }

    void clear()
    {
         map.clear();
    }

    Iterator iterator()
    {
        return new ValueIterator( map);
    }

    private void clearQueue()
    {
        for ( CacheValue cv=(CacheValue)queue.poll(); cv!=null;
            cv=(CacheValue)queue.poll())
        {
            map.remove( cv.key);
        }
    }

    static class ValueIterator implements Iterator
    {
        private Iterator baseIterator;
        private Object next;

        ValueIterator( HashMap map)
        {
            baseIterator=map.values().iterator();
            next=null;
        }

        public boolean hasNext()
        {
            while ( next==null && baseIterator.hasNext())
            {
                next=((CacheValue)baseIterator.next()).get();
            }
            return next!=null;
        }

        public Object next()
            throws NoSuchElementException
        {
            if ( ! hasNext())
                throw new NoSuchElementException();
            Object result=next;
            next=null;
            return result;
        }

        public void remove()
            throws UnsupportedOperationException
        {
            throw new UnsupportedOperationException();
        }
    }

    static class CacheValue extends WeakReference
    {
        ObjectKey key;

        CacheValue( Object toCache, ReferenceQueue q, ObjectKey k)
        {
            super( toCache, q);
            key=k;
        }
    }
}