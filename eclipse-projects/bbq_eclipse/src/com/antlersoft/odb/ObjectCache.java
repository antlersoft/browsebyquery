
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