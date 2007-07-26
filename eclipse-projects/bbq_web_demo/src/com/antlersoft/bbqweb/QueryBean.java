/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.bbqweb;

import java.util.Enumeration;

import javax.servlet.ServletContext;

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
	
	ServletContext m_context;
	
	public void setContext( ServletContext context)
	{
		m_context=context;
	}
	
	public QueryBean()
	{
		m_query=new AnalyzerQuery();
	}
	
	public void setQueryText( String text)
	{
		if ( text!=null)
		{
			m_query.setLine( text);
		}
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
		m_result=null;
		m_text=null;
		m_db=db;
	}
	
	private static void escapeAppend( StringBuilder sb, String s)
	{
		int l=s.length();
		char[] buf=new char[l];
		s.getChars(0, l, buf, 0);
		for ( int i=0; i<l; ++i)
		{
			char ch=buf[i];
			if ( ch=='<')
				sb.append( "&lt;");
			else if ( ch=='&')
				sb.append("&amp;");
			else
				sb.append(ch);
		}
	}
	
	public synchronized String getHtmlQueryResult()
	{
		if ( m_result==null)
		{
			StringBuilder sb=new StringBuilder();
			if ( m_text!=null && m_text.length()>0)
			{
				sb.append("<br>");
				try
				{
					boolean results=false;
					for ( Enumeration e = m_query.getExpression().evaluate(m_db); e.hasMoreElements();)
					{
						results=true;
						escapeAppend( sb, e.nextElement().toString());
						sb.append( "<br>");
					}
					if ( ! results)
					{
						sb.append( "<i>(No results found)</i>");
					}
				}
				catch ( Exception e)
				{
					if ( m_context!=null)
					{
						m_context.log( "Error evaluating "+m_text+":\n", e);
					}
					sb.append( "Error parsing/evaluating query:<br>");
					escapeAppend( sb, e.getMessage());
				}
			}
			m_result=sb.toString();
		}
		
		return m_result;
	}
}
