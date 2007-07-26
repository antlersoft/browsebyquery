<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII" import="com.antlersoft.analyzer.*;import com.antlersoft.analyzer.query.*; import java.util.*;"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Browse-by-Query Demo Page</title>
</head>
<body>
<jsp:useBean id="b_db" class="com.antlersoft.bbqweb.DBBean" scope="application"/>
<jsp:useBean id="b_query" class="com.antlersoft.bbqweb.QueryBean" scope="session"/>
<jsp:setProperty name="b_query" property="DB" value="<%= b_db.getDB() %>"/>
<jsp:setProperty name="b_query" property="queryText" value="<%= request.getParameter("query") %>"/>
<jsp:getProperty name="b_query" property="queryText"/>
<form>
<input type="text" name="query"/>
</form>
<jsp:getProperty name="b_query" property="htmlQueryResult"/>
</body>
</html>