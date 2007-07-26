/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.bbqweb;

import com.antlersoft.analyzer.AnalyzerDB;
import com.antlersoft.analyzer.IndexAnalyzeDB;

import javax.servlet.ServletContext;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBBean {
	private IndexAnalyzeDB m_db;
	private ServletContext m_context;
	
	public DBBean()
	{
	}
	
	public void setContext( ServletContext context)
	{
		m_context=context;
	}
	
	public synchronized AnalyzerDB getDB()
	{
		if ( m_db==null)
		{
			m_db=new IndexAnalyzeDB();
			try
			{
				m_db.openDB( "/mnt/external2/scratch/eclipse_eclipse.pj");
			}
			catch ( Exception e)
			{
				m_db=null;
				if ( m_context!=null)
				{
					m_context.log( "Failed to open database:", e);
				}
			}
		}
		return m_db;
	}
	
	public void destroy()
	{
		if ( m_db!=null)
		{
			try
			{
				m_db.closeDB();
			}
			catch ( Exception e)
			{
				if ( m_context!=null)
					m_context.log( "Failed to close database:", e);
			}
		}
	}
}
