package com.antlersoft.query;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Implements an enumeration that changes the objects produced by a base enumeration
 * into another object with the filterObject abstract method.
 * @author Michael A. MacDonald
 *
 */
public abstract class FilterEnumeration implements Enumeration
{
    private Enumeration _baseEnumeration;
    private Object next;
    // Kludge because of compiler bug
    private boolean initialized;

    public FilterEnumeration( Enumeration base)
    {
        _baseEnumeration=base;
        // Kludge because of compiler bug
        initialized=false;
    }

    /**
     * Changes the output of the underlying enumeration.
     * Must produce an object or null for each object passed in.
     * @param next Object from base enumeration
     * @return transformed object
     */
    abstract protected Object filterObject( Object next);

    private void getNextObject()
    {
        for ( next=null; next==null &&
            _baseEnumeration.hasMoreElements();)
        {
            next=filterObject( _baseEnumeration.nextElement());
        }
    }

    public boolean hasMoreElements()
    {
        // Kludge because of compiler bug
        if ( ! initialized)
        {
            getNextObject();
            initialized=true;
        }
        return next!=null;
    }

    public Object nextElement()
    {
        // Kludge because of compiler bug
        if ( ! initialized)
        {
            getNextObject();
            initialized=true;
        }
        if ( next==null)
            throw new NoSuchElementException();
        Object retVal=next;
        getNextObject();
        return retVal;
    }
}