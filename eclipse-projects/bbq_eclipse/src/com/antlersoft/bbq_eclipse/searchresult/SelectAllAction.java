/**
 * Copyright (c) 2011 Michael A. MacDonald
 */
package com.antlersoft.bbq_eclipse.searchresult;

import org.eclipse.jface.action.Action;

/**
 * Select all items in QueryResultView
 * @author Michael A. MacDonald
 *
 */
class SelectAllAction extends Action {

	QueryResultView _view;
	/**
	 * @param text
	 */
	public SelectAllAction(QueryResultView view) {
		super("Select all");
		_view = view;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		_view._viewer.getTable().selectAll();
	}
}
