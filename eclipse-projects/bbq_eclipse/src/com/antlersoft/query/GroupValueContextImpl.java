package com.antlersoft.query;

import java.util.Iterator;

public class GroupValueContextImpl implements GroupValueContext {
    public void startGroup(ValueObject vobj, DataSource source) {
		for ( Iterator i=vobj.getValueCollection().iterator();
			  i.hasNext();)
		{
			((GroupValueContext)((ValueObject)i.next())).startGroup( vobj,
				source);
		}
    }
    public boolean addObject(ValueObject vobj, DataSource source, Object to_add) {
		boolean need_more=false;
		for ( Iterator i=vobj.getValueCollection().iterator();
			  i.hasNext();)
		{
			need_more=need_more || ((GroupValueContext)((ValueObject)i.next())).addObject( vobj,
				source, to_add);
		}
		return need_more;
    }
    public void finishGroup(ValueObject vobj, DataSource source) {
		for ( Iterator i=vobj.getValueCollection().iterator();
			  i.hasNext();)
		{
			((GroupValueContext)((ValueObject)i.next())).finishGroup( vobj,
				source);
		}
    }
    public int getContextType() {
		return ValueContext.GROUP;
    }
}