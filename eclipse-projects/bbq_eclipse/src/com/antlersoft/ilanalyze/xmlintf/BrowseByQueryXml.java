/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.ilanalyze.xmlintf;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.antlersoft.query.DataSource;

import com.antlersoft.query.environment.QueryLanguageEnvironment;

/**
 * Executes a query against a query language environment/data source shared
 * with the UI thread
 * @author Michael A. MacDonald
 *
 */
public class BrowseByQueryXml implements IBrowseByQuery {
	QueryLanguageEnvironment env;
	DataSource source;
	
	public BrowseByQueryXml( QueryLanguageEnvironment environment, DataSource datasource)
	{
		env=environment;
		source=datasource;
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.ilanalyze.xmlintf.IBrowseByQuery#PerformQuery(com.antlersoft.ilanalyze.xmlintf.QueryRequest)
	 */
	public QueryResponse PerformQuery(QueryRequest qr) {
		QueryRunner runner=new QueryRunner( qr);
		try
		{
			SwingUtilities.invokeAndWait(runner);
		}
		catch ( InvocationTargetException ite)
		{
			Exception e=( ite.getCause() instanceof Exception ? (Exception)ite.getCause() : ite);
			return new QueryResponse( new RequestException( e));
		}
		catch ( InterruptedException ie)
		{
			return new QueryResponse( new RequestException( ie));
		}
		return runner.response;
	}

	/**
	 * Executes the query in a Runnable
	 * @author Michael A. MacDonald
	 *
	 */
	class QueryRunner implements Runnable
	{
		QueryRequest request;
		QueryResponse response;
		
		QueryRunner( QueryRequest qr)
		{
			request=qr;
			response=null;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try
			{
				env.setLine( request.getText());
				response=new QueryResponse( env.getExpression().evaluate( source));
			}
			catch ( Exception e)
			{
				response=new QueryResponse( new RequestException( e));
			}
		}
	}
}
