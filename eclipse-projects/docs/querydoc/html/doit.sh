#!/bin/sh

xsltproc /opt/kde/share/apps/ksgmltools2/docbook/xsl/html/chunk.xsl $1

perl -pi -e '
if ( /^(.*)(\<\/body\>.*)/ )
{
print $1;
print "\n";
print <<EOF
<table cellpadding="2" cellspacing="0" border="0" width="100%">
              <tbody text="#000000" bgcolor="#c3cc77" link="#000099" vlink="#990099" alink="#000099">
                <tr>
                  <td valign="center"><a href="http://browsebyquery.sourceforge.net">browse-by-query home</a>             </td>
               <td valign="center" align="center">   <a href="http://www.antlersoft.com/freesoftwarenews.html">antlersoft free software</a>          </td>
		<td align="right" valign="center">
<a href="http://sourceforge.net/projects/browsebyquery" target="_top" valign="center">project page</a> 
<a href="http://sourceforge.net/projects/browsebyquery" target="_top" valign=
"center"><span class="inlinemediaobject"><img src="http://sourceforge.net/sflogo.php?group_id=139907&amp;type=1" height="31" width="88" longdesc="ld-id2764673.html"></span></a>
				</td>
                </tr>
                                               
  </tbody>            
</table>
EOF
;
print $2;
$_="";
}
' *.html
