package com.antlersoft.bbq_eclipse.searchresult;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;


public class QueryResultViewContentProvider implements IStructuredContentProvider, ISearchResultListener {

  private final QueryResultView queryResultView;

  /**
   * @param queryResultView
   */
  public QueryResultViewContentProvider(QueryResultView queryResultView) {
    this.queryResultView = queryResultView;
  }

  @SuppressWarnings("unchecked") ArrayList _pendingAdds = new ArrayList();

  public synchronized void inputChanged(Viewer v, Object oldInput, Object newInput) {
    if (oldInput != null)
      ((QueryResult) oldInput).removeListener(this);
    if (newInput != null)
      ((QueryResult) newInput).addListener(this);
    _pendingAdds.clear();
  }

  public void dispose() {
  }

  @SuppressWarnings("unchecked") public synchronized void searchResultChanged(SearchResultEvent evt) {

    ArrayList l = ((QueryResult) evt.getSearchResult()).getResultItems();
    _pendingAdds.add(l.get(l.size() - 1));

    if (_pendingAdds.size() == 1) {

      this.queryResultView.getSite().getShell().getDisplay().asyncExec(new Runnable() {

        public void run() {
          synchronized (queryResultView) {
            if (_pendingAdds.size() > 0) {
              // _viewer.add( _pendingAdds.toArray());
              _pendingAdds.clear();
              QueryResultViewContentProvider.this.queryResultView._viewer.refresh();
            }
          }
        }
      });
    }
  }

  public Object[] getElements(Object parent) {
    QueryResult result = (QueryResult) parent;
    return result.getResultItems().toArray();
  }
}