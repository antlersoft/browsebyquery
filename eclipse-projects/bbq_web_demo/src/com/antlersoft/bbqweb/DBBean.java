/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.bbqweb;

import com.antlersoft.analyzer.AnalyzerDB;
import com.antlersoft.analyzer.IndexAnalyzeDB;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

/**
 * @author Michael A. MacDonald
 *
 */
public class DBBean {
	private IndexAnalyzeDB m_db;
	private ServletContext m_context;
	
	private static Logger m_logger=Logger.getLogger("com.antlersoft.bbq_web_demo"); 
	
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
				m_logger.fine ( "Opening db");
				m_db.openDB( "/mnt/external2/scratch/eclipse_eclipse.pj");
			}
			catch ( Exception e)
			{
				m_db=null;
				if ( m_context!=null)
				{
					m_context.log( "Failed to open database:", e);
					m_logger.log( Level.WARNING, "Failed to open database:", e);
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
				m_logger.fine( "Closing db");
				m_db.closeDB();
			}
			catch ( Exception e)
			{
				if ( m_context!=null)
					m_context.log( "Failed to close database:", e);
				m_logger.log( Level.WARNING, "Failed to close database:", e);
			}
		}
	}
}
