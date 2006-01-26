/**
 * 
 */
package com.antlersoft.bbq_eclipse.searchresult;

import java.util.Enumeration;

import com.antlersoft.analyzer.query.SetExpression;
import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;
import com.antlersoft.odb.ObjectDBException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

/**
 * @author mike
 *
 */
public class Query implements ISearchQuery {
	
	private QueryResult _currentResult;
	private SetExpression _expression;
	private String _expressionLabel;
	
	public Query( SetExpression expression, String expressionLabel)
	{
		_expression=expression;
		_expressionLabel=expressionLabel;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchQuery#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus run(IProgressMonitor monitor)
			throws OperationCanceledException {
		monitor.beginTask( "Run query", IProgressMonitor.UNKNOWN);
		IStatus run_status=Bbq_eclipsePlugin.createStatus("",null,IStatus.OK,0);
		try
		{
			monitor.subTask( "Get enumeration");
			monitor.worked(1);
			Enumeration e=_expression.execute( Bbq_eclipsePlugin.getDefault().getDB());
			monitor.subTask( "Step through enumeration");
			while ( e.hasMoreElements() && ! monitor.isCanceled())
			{
				((QueryResult)getSearchResult()).addResultItem( e.nextElement());
				monitor.worked( 1);
			}
			if ( e.hasMoreElements())
				throw new OperationCanceledException("Query operation canceled");
		}
		catch ( OperationCanceledException oce)
		{
			throw oce;
		}
		catch ( ObjectDBException odb)
		{
			run_status=Bbq_eclipsePlugin.createStatus( "DB Error executing BBQ Query", odb, IStatus.ERROR, 0);
		}
		catch ( CoreException ce)
		{
			run_status=ce.getStatus();
		}
		catch ( Exception e)
		{
			run_status=Bbq_eclipsePlugin.createStatus( "Error running BBQ Query", e, IStatus.ERROR, 0);
		}
		return run_status;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchQuery#getLabel()
	 */
	public String getLabel() {
		return _expressionLabel;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchQuery#canRerun()
	 */
	public boolean canRerun() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchQuery#canRunInBackground()
	 */
	public boolean canRunInBackground() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.ISearchQuery#getSearchResult()
	 */
	public synchronized ISearchResult getSearchResult() {
		if ( _currentResult==null)
			_currentResult=new QueryResult( this);
		return _currentResult;
	}
}
