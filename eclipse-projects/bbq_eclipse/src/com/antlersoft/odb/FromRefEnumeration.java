package com.antlersoft.odb;

import java.util.Enumeration;

public class FromRefEnumeration implements java.util.Enumeration 
{
    private Enumeration _base;
    public FromRefEnumeration( Enumeration base)
    { _base=base; }
    public boolean hasMoreElements()
    {
	return _base.hasMoreElements();
    }
    public Object nextElement()
    {
	return ((ObjectRef)_base.nextElement()).getReferenced();
    }
}
