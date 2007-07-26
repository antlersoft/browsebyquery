/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.bbqweb;

import com.antlersoft.analyzer.query.AnalyzerQuery;
import com.antlersoft.analyzer.AnalyzerDB;

/**
 * @author Michael A. MacDonald
 *
 */
public class QueryBean {
	private AnalyzerQuery m_query;
	
	private String m_text;
	
	private String m_result;
	
	private AnalyzerDB m_db;
	
	public QueryBean()
	{
		m_query=new AnalyzerQuery();
	}
	
	public void setQueryText( String text)
	{
		m_query.setLine( text);
		m_text=text;
		m_result=null;
	}
	
	public String getQueryText()
	{
		return m_text;
	}
	
	public AnalyzerDB getDB()
	{
		return m_db;
	}
	
	public void setDB(AnalyzerDB db)
	{
		m_db=db;
	}
}
