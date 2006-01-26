package com.antlersoft.bbq_eclipse.searchresult;

import org.eclipse.search.ui.SearchResultEvent;

final class QueryResultEvent extends SearchResultEvent {
	static final int ADDED=1;
	
	private int _change;
	
	QueryResultEvent( QueryResult result, int change)
	{
		super( result);
		_change=change;
	}
	
	public int getChange()
	{
		return _change;
	}
}
