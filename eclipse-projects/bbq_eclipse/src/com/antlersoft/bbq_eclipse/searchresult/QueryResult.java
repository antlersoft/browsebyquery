/**
 * 
 */
package com.antlersoft.bbq_eclipse.searchresult;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;

import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;

/**
 * @author mike
 *
 */
public class QueryResult implements ISearchResult {
	
	private Query _query;
	private ArrayList _searchResultListeners;
	private ArrayList _resultItems;
	
	QueryResult( Query query)
	{
		_query=query;
		_searchResultListeners=new ArrayList();
		_resultItems=new ArrayList();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResult#addListener(org.eclipse.search.ui.ISearchResultListener)
	 */
	public void addListener(ISearchResultListener l) {
		synchronized ( _searchResultListeners)
		{
			_searchResultListeners.add( l);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResult#removeListener(org.eclipse.search.ui.ISearchResultListener)
	 */
	public void removeListener(ISearchResultListener l) {
		synchronized ( _searchResultListeners)
		{
			_searchResultListeners.remove( l);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResult#getLabel()
	 */
	public String getLabel() {
		return _query.getLabel();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResult#getTooltip()
	 */
	public String getTooltip() {
		return getLabel();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResult#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return Bbq_eclipsePlugin.getImageDescriptor( "icons/weber-small.gif");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchResult#getQuery()
	 */
	public ISearchQuery getQuery() {
		return _query;
	}

	void addResultItem( Object item)
	{
		_resultItems.add( item);
		synchronized ( _searchResultListeners)
		{
			QueryResultEvent evt=new QueryResultEvent( QueryResult.this, QueryResultEvent.ADDED);
			for ( Iterator i=_searchResultListeners.iterator(); i.hasNext();)
			{
				ISearchResultListener listener=(ISearchResultListener)i.next();
				listener.searchResultChanged( evt);
			}
		}
	}
	
	ArrayList getResultItems()
	{
		return _resultItems;
	}
}
