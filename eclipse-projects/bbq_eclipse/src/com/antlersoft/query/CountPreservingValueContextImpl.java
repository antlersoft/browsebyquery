package com.antlersoft.query;

import java.util.Iterator;

public class CountPreservingValueContextImpl implements CountPreservingValueContext {
    public void inputObject(ValueObject value, DataSource source, Object input) {
		for ( Iterator i=value.getValueCollection().iterator(); i.hasNext();)
		{
			ValueObject vobj=(ValueObject)i.next();
			((CountPreservingValueContext)vobj.getContext()).inputObject( vobj, source, input);
		}
    }
    public int getContextType() {
		return ValueContext.COUNT_PRESERVING;
    }
}