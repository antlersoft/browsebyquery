
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.opentool;

import java.util.HashMap;

import com.borland.primetime.PrimeTime;

import com.borland.primetime.ide.Browser;
import com.borland.primetime.ide.NodeViewerFactory;
import com.borland.primetime.ide.NodeViewer;
import com.borland.primetime.ide.Context;

import com.borland.primetime.node.Node;

import com.borland.jbuilder.node.JBProject;

public class BBQTool implements NodeViewerFactory
{
    private HashMap projectToAnalyzer;

    private BBQTool()
    {
        projectToAnalyzer=new HashMap();
    }

    public NodeViewer createNodeViewer(Context parm1)
    {
System.out.println( "createNodeViewer");
        try
        {
            ProjectAnalyzer analyzer;
            if ( ! ( parm1.getNode() instanceof JBProject))
                return null;
            synchronized ( projectToAnalyzer)
            {
                analyzer=(ProjectAnalyzer)projectToAnalyzer
                    .get( (JBProject)parm1.getNode());
                if ( analyzer==null)
                {
                    analyzer=new ProjectAnalyzer( (JBProject)parm1.getNode());
                    projectToAnalyzer.put( parm1.getNode(), analyzer);
                }
            }
            return new ProjectBBQ( analyzer, parm1.getBrowser());
        }
        catch ( Exception e)
        {
            return null;
        }
    }

    public boolean canDisplayNode(Node parm1)
    {
System.out.println( "canDisplayNode");
        return parm1 instanceof JBProject;
    }

    public static void initOpenTool(byte major,
        byte minor)
    {

System.out.println( "initOpenTool");

        // Check OpenTools version number
        if (major != PrimeTime.CURRENT_MAJOR_VERSION)
            return;
/*
  // Add a new menu item to the Help menu
  com.borland.jbuilder.JBuilderMenu.GROUP_Help.add(new com.borland.primetime.ide.BrowserAction(
      "Say Hello") {
    public void actionPerformed(com.borland.primetime.ide.Browser browser) {
      javax.swing.JOptionPane.showConfirmDialog(null,
          "Hello, World!");
    }
  });
*/
        Browser.registerNodeViewerFactory( new BBQTool());
    }
}