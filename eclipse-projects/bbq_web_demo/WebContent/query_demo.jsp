<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII" import="com.antlersoft.analyzer.*;import com.antlersoft.analyzer.query.*; import java.util.*;"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Browse-by-Query Live Demo</title>
       
  <style type="text/css"> 
  div.sidebar {
	float: right;
	width: 15em;
	background: silver; 
	margin: 0.5em;
	padding: 0.5em;
	}
  div.odd { background:silver; }
  div.even {}
  td.bar { background:silver; }
  </style>
  <script language="javascript">
  	var explanation = new Array(7);
  	explanation[0] = "";
  	explanation[1] = "References to the string &quot;aastore&quot; in any class."
  	explanation[2] = "Interfaces that derive from org.eclipse.core.runtime.IAdaptable";
  	explanation[3] = "All methods with any argument of type org.eclipse.core.runtime.IAdaptable";
  	explanation[4] = "All public methods, not in packages marked &quot;internal&quot;, that return org.eclipse.core.runtime.IProgressMonitor"
  	explanation[5] = "All methods in classes in the package org.eclipse.core.runtime with more than 3 arguments"
  	explanation[6] = "Calls to any method that references a string that contains the substring &quot;properties&quot;"
  </script>
</head>
<body>
<jsp:useBean id="b_db" class="com.antlersoft.bbqweb.DBBean" scope="application"/>
<jsp:useBean id="b_query" class="com.antlersoft.bbqweb.QueryBean" scope="session"/>
<jsp:setProperty name="b_query" property="context" value="<%= config.getServletContext() %>"/>
<jsp:setProperty name="b_query" property="DB" value="<%= b_db.getDB() %>"/>
<jsp:setProperty name="b_query" property="queryText" value="<%= request.getParameter("query") %>"/>
<table cellpadding="2" cellspacing="2" border="0" width="100%" text="#000000" bgcolor="#c3cc77" link="#000099" vlink="#990099"
 alink="#000099">
              <tbody>
                <tr>
				<td><a href="http://browsebyquery.sourceforge.net">browse-by-query home</td>
               <td >   <a href="http://www.antlersoft.com/freesoftwarenews.html">antlersoft free software</a>          </td>
				<td><a href="http://sourceforge.net/projects/browsebyquery">sourceforge project page</a></td>
<td></td>
                </tr>
  </tbody>            
</table>

<h1>Browse-by-Query Live Demo</h1>
<table cellspacing="3" cellpadding="3">
<colgroup>
<col width="20%" />
<col/>
</colgroup>
<tbody>
<tr>
<td valign="top" class="bar">
<p>
This demo lets you query a large Browse-by-Query database, which was built from all the Eclipse 3.2 base and Java development jars as well as the Browse-by-Query code,
totaling more than 225,000 methods in about 26,000 classes.
</p>
<p>
If you were using an IDE-integrated version of Browse-by-Query, you could click
on one of the results
to open that file and position in your editor.
</p>
<h3>Links</h3>
<p>
<a href="http://browsebyquery.sourceforge.net">Project Home</a>
</p>
<p>
<a href="http://browsebyquery.sourceforge.net/bbq_quick_start.html">Quick-Start Guide</a>
</p>
<p>
<a href="http://browsebyquery.sourceforge.net/bbq_lang/html/index.html">Browse-by-Query Language for Java manual</a>.
</p>
<p>
<a href="http://sourceforge.net/project/showfiles.php?group_id=139907">Download Page</a>
</p>
<p>
<a href="http://sourceforge.net/forum/forum.php?forum_id=469469">Discussion Forum</a>
</p>
<p>
<a href="http://sourceforge.net/forum/forum.php?forum_id=469470">Support Forum</a>
</p>
<p>
<a href="http://sourceforge.net/projects/browsebyquery">SourceForge project page</a>
</p>
<a href="http://sourceforge.net" target="_top"><span class="inlinemediaobject"><img src="http://sourceforge.net/sflogo.php?group_id=139907&amp;type=1" height="31" width="88" longdesc="ld-id2764673.html"></span>    </a>
</td>
<td valign="top">
<form id="query_form" method="post">
<a target="_blank" href="http://browsebyquery.sourceforge.net/bbq_lang/html/index.html">Enter query</a> or choose a sample query below:<br/>
<textarea name="query" cols="60" rows=4 onchange="javascript:document.getElementById('t1').innerHTML=''">
<jsp:getProperty name="b_query" property="queryText"/>
</textarea>
<input type="submit" value="Run Query"/>
<br>
<label>
Query history:
	<select name="query_history" onchange="javascript:query.value=query_history.options.item(query_history.selectedIndex).text; document.getElementById('t1').innerHTML=''">
	<jsp:getProperty name="b_query" property="htmlQueryHistory"/>
	</select>
</label>
<br/>
<label>
Saved Values:
	<select name="stored_values" onchange="javascript:query.value=query.value + ' ' + stored_values.options.item(stored_values.selectedIndex).text + ' '">
	<jsp:getProperty name="b_query" property="htmlStoredValues"/>
	</select>
</label>
<br/>
<label>
Sample Queries:
	<select name="sample_queries" onchange="javascript:query.value=sample_queries.options.item(sample_queries.selectedIndex).text; document.getElementById('t1').innerHTML=explanation[sample_queries.selectedIndex]">
		<option>(select one)</option>
		<option>references to string "aastore"</option>
		<option>interface derived classes of class "org.eclipse.core.runtime.IAdaptable"</option>
		<option>methods of arguments with type of class "org.eclipse.core.runtime.IAdaptable"</option>
		<option>not matching ".internal." public methods with type of class "org.eclipse.core.runtime.IProgressMonitor"</option>
		<option>(count of ( arguments of ) &gt; 3) methods in classes in package "org.eclipse.core.runtime"</option>
		<option>calls to methods containing references to matching ".properties" all strings</option>
	</select>
</label>
<div id="t1" name="t1">
</div>
</form>
<h3>Results</h3>
<jsp:getProperty name="b_query" property="htmlQueryResult"/>
</td>
</tr>
</tbody>
</table>
<table cellpadding="2" cellspacing="2" border="0" width="100%" text="#000000" bgcolor="#c3cc77" link="#000099" vlink="#990099"
 alink="#000099">
              <tbody>
                <tr>
				<td><a href="http://browsebyquery.sourceforge.net">browse-by-query home</td>
               <td>   <a href="http://www.antlersoft.com/freesoftwarenews.html">antlersoft free software</a>          </td>
				<td><a href="http://sourceforge.net/projects/browsebyquery">sourceforge project page</a></td>
<td></td>
                </tr>
  </tbody>            
</table></body>
</html>
