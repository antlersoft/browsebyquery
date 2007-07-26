<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII" import="com.antlersoft.analyzer.*;import com.antlersoft.analyzer.query.*; import java.util.*;"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
Hi there
<%!
	AnalyzerDB m_db;
	AnalyzerQuery m_query;
	
	public void jspInit()
	{
		m_db=new IndexAnalyzeDB();
		try
		{
			m_db.openDB( "/mnt/external2/scratch/eclipse_eclipse.pj");
		}
		catch ( Exception e)
		{
			m_db=null;
			log( "Failed to open database:", e);
		}
		m_query=new AnalyzerQuery();
	}
	
	public void jspDestroy()
	{
		if ( m_db!=null)
		{
			try
			{
				m_db.closeDB();				
			}
			catch ( Exception e)
			{
				log( "Exception closing database:", e);
			}
		}
	}
%>
<%
String q=request.getParameter( "query");
if ( q!=null)
{
out.write( q);
}
%>
<form>
<input type="text" name="query"/>
</form>
<%
if ( q!=null)
{
	out.write("<br>");
	m_query.setLine( q);
	for ( Enumeration e = m_query.getExpression().evaluate(m_db); e.hasMoreElements();)
	{
		out.write( e.nextElement().toString());
		out.write( "<br>");
	}
}
%>
</body>
</html>