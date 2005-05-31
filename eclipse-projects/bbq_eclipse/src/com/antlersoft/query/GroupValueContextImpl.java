/*
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
 */
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