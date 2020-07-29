/**
 * Copyright (c) 2011 Michael A. MacDonald
 */
package com.antlersoft.bbq_eclipse.searchresult;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

/**
 * Copy selection from QueryResultView as text to the clipboard
 * @author Michael A. MacDonald
 *
 */
public class CopyAction extends Action {
	QueryResultView _view;
	
	CopyAction(QueryResultView view)
	{
		super("Copy");
		_view = view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		ISelection viewSelection = _view.getSelection();
	    if (viewSelection != null && !viewSelection.isEmpty()
	            && viewSelection instanceof IStructuredSelection) {
	    	IStructuredSelection selection = (IStructuredSelection)viewSelection;
	    	StringBuilder sb = new StringBuilder();
            String lineSep = System.getProperty("line.separator");
	    	for (Iterator<?> i = selection.iterator(); i.hasNext();)
	    	{
	    		sb.append(i.next().toString());
	    		sb.append(lineSep);
	    	}
	    	Clipboard clipboard = new Clipboard(_view.getControl().getDisplay());
	    	try {
		    	Object[] clipObjects = new Object[] { sb.toString() };
		    	Transfer[] clipTransfers = new Transfer[] { TextTransfer.getInstance() };
		    	clipboard.setContents(clipObjects, clipTransfers);
	    	}
	    	finally
	    	{
	    		clipboard.dispose();
	    	}
	    }
	}

}
