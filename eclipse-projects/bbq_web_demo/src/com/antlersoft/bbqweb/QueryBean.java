/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.bbqweb;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import com.antlersoft.query.environment.AnalyzerQuery;
import com.antlersoft.analyzer.IndexAnalyzeDB;
import com.antlersoft.analyzer.query.QueryParser;

import com.antlersoft.util.CharClass;

/**
 * @author Michael A. MacDonald
 *
 */
public class QueryBean {
	private AnalyzerQuery m_query;
	
	private String m_text;
	
	private String m_result;
	
	private IndexAnalyzeDB m_db;
	
	private ArrayList m_history;
	
	private static Logger m_logger=Logger.getLogger("com.antlersoft.bbq_web_demo"); 
	private ServletContext m_context;
	
	public void setContext( ServletContext context)
	{
		m_context=context;
	}
	
	public QueryBean()
	{
		m_query=new AnalyzerQuery( new QueryParser());
		m_history=new ArrayList();
	}
	
	public void setQueryText( String text)
	{
		if ( text!=null)
		{
			m_logger.info( text);
			int l;
			for ( l=text.length(); l>0; --l)
			{
				if ( ! CharClass.isWhiteSpace(text.charAt(l-1)))
					break;
			}
			text=text.substring(0,l);
			m_query.setLine( text);
		}
		m_text=text;
		m_result=null;
	}
	
	public String getQueryText()
	{
		if ( m_text==null)
			return "Enter query:";
		return escapeString(m_text);
	}
	
	public IndexAnalyzeDB getDB()
	{
		return m_db;
	}
	
	public void setDB(IndexAnalyzeDB db)
	{
		m_logger.finer( "Setting db");
		if ( db==null)
			m_logger.warning( "db=null");
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
	
	private static String escapeString( String s)
	{
		StringBuilder sb=new StringBuilder( s.length());
		escapeAppend( sb, s);
		return sb.toString();
	}
	
	public synchronized String getHtmlQueryHistory()
	{
		// Make sure current text is on history list
		evaluate();
		StringBuilder sb=new StringBuilder();
		if ( m_history.size()==0)
		{
			sb.append( "<option selected>");
			escapeAppend( sb, "(no query yet)");
			sb.append( "</option>\n");
		}
		boolean first=true;
		for ( Iterator it=m_history.iterator(); it.hasNext();)
		{
			if ( first)
			{
				sb.append("<option selected>");
				first=false;
			}
			else
				sb.append( "<option>");
			escapeAppend( sb, (String)it.next());
			sb.append( "</option>\n");
		}
		
		return sb.toString();
	}
	
	public synchronized String getHtmlStoredValues()
	{
		evaluate();
		m_query.getStoredValues();
		ArrayList al=new ArrayList();
		String[] saved=m_query.getStoredValues();
		if ( saved.length>0)
			al.add( "(select to add to query)");
		else
			al.add( "(no stored values)");
		for ( int i=0; i<saved.length; ++i)
			al.add( saved[i]);
		
		StringBuilder sb=new StringBuilder();
		boolean first=true;
		for ( Iterator i=al.iterator(); i.hasNext();)
		{
			if ( first)
			{
				sb.append( "<option selected>");
				first=false;
			}
			else
				sb.append( "<option>");
			escapeAppend( sb, (String)i.next());
			sb.append("</option>\n");
		}
		
		return sb.toString();
	}
	
	public synchronized String getHtmlQueryResult()
	{
		evaluate();
		
		return m_result;
	}
	
	private synchronized void evaluate()
	{
		if ( m_result==null)
		{
			StringBuilder sb=new StringBuilder();
			if ( m_text!=null && m_text.length()>0)
			{
				int i=1;
				sb.append("<div class='odd'>");
				try
				{
					boolean results=false;
					for ( Enumeration e = m_query.getExpression().evaluate(m_db); e.hasMoreElements();)
					{
						results=true;
						escapeAppend( sb, e.nextElement().toString());
						if ( e.hasMoreElements())
						{
							i++;
							sb.append( "</div>");
							sb.append( "<div class='");
							sb.append( i%2 != 0 ? "odd" : "even");
							sb.append( "'>");
						}
					}
					if ( ! results)
					{
						sb.append( "<i>(No results found)</i>");
					}
					for ( Iterator it=m_history.iterator(); it.hasNext();)
					{
						String s=(String)it.next();
						if ( s.equals( m_text))
						{
							it.remove();
							break;
						}
					}
					m_history.add( 0, m_text);
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
				sb.append("</div>\n");
			}
			m_result=sb.toString();
		}
	}
}
