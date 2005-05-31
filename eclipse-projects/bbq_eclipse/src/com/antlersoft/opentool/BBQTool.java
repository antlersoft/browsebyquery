
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.opentool;

import java.util.HashMap;

import com.borland.primetime.PrimeTime;

import com.borland.primetime.build.BuildProcess;

import com.borland.primetime.ide.Browser;
import com.borland.primetime.ide.NodeViewerFactory;
import com.borland.primetime.ide.NodeViewer;
import com.borland.primetime.ide.Context;

import com.borland.primetime.node.LightweightNode;
import com.borland.primetime.node.Node;

import com.borland.primetime.properties.PropertyManager;

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
            if ( ! ( parm1.getNode() instanceof BBQNode))
                return null;
            synchronized ( projectToAnalyzer)
            {
                JBProject project=(JBProject)parm1.getNode().getProject();
                analyzer=(ProjectAnalyzer)projectToAnalyzer
                    .get( project);
                if ( analyzer==null)
                {
                    analyzer=new ProjectAnalyzer( (BBQNode)parm1.getNode());
                    projectToAnalyzer.put( project, analyzer);
                }
            }
            return new ProjectBBQ( analyzer, parm1.getBrowser(), (BBQNode)
                parm1.getNode());
        }
        catch ( Exception e)
        {
            return null;
        }
    }

    public boolean canDisplayNode(Node parm1)
    {
System.out.println( "canDisplayNode");
        return parm1 instanceof BBQNode;
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
        LightweightNode.registerLightweightNodeClass( BBQNode.BBQ_NODE_TYPE,
            BBQNode.class);
        BBQTool tool=new BBQTool();
        Browser.registerNodeViewerFactory( tool);
        Browser.addStaticBrowserListener( new BBQBrowserListener( tool));
        BuildProcess.addStaticBuildListener( new BBQBuildListener( true));
        PropertyManager.registerPropertyGroup( new BBQPathsGroup());
    }

    void setAnalyzerPath( BBQNode node, String newPath)
    {
        synchronized ( projectToAnalyzer)
        {
            JBProject project=(JBProject)node.getProject();
            ProjectAnalyzer analyzer=(ProjectAnalyzer)projectToAnalyzer
                .get( project);
            if ( analyzer==null)
            {
                BBQPathsGroup.pathsProperty.setValue( node, newPath);
            }
            else
                analyzer.setAnalyzerPath( node, newPath);
        }
    }
}