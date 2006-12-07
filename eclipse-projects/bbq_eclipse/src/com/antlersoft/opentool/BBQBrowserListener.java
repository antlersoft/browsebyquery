
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

import com.borland.primetime.ide.BrowserListener;
import com.borland.primetime.ide.Browser;
import com.borland.primetime.node.Node;
import com.borland.primetime.node.LightweightNode;
import com.borland.primetime.ide.NodeViewer;
import com.borland.primetime.node.Project;

import com.borland.jbuilder.node.JBProject;

public class BBQBrowserListener implements BrowserListener
{
    private BBQTool tool;

    public BBQBrowserListener( BBQTool t)
    {
        tool=t;
    }

    public void browserViewerDeactivating(Browser parm1, Node parm2, NodeViewer parm3) throws com.borland.primetime.util.VetoException
    {
        //TODO: Implement this com.borland.primetime.ide.BrowserListener method
    }

    public void browserViewerActivated(Browser parm1, Node parm2, NodeViewer parm3)
    {
        //TODO: Implement this com.borland.primetime.ide.BrowserListener method
    }

    public void browserNodeClosed(Browser parm1, Node parm2)
    {
        //TODO: Implement this com.borland.primetime.ide.BrowserListener method
    }

    public void browserNodeActivated(Browser parm1, Node parm2)
    {
        //TODO: Implement this com.borland.primetime.ide.BrowserListener method
    }

    public void browserProjectClosed(Browser parm1, Project parm2)
    {
        //TODO: Implement this com.borland.primetime.ide.BrowserListener method
    }

    public void browserProjectActivated(Browser parm1, Project parm2)
    {
System.out.println( "Project Activated");
        if ( parm2 instanceof JBProject)
        {
            Node[] nodes=parm2.getChildren();
            for ( int i=0; i<nodes.length; i++)
            {
                if ( nodes[i] instanceof BBQNode)
                {
                    ((BBQNode)nodes[i]).setTool( tool);
                    return;
                }
            }
            BBQNode node=(BBQNode)LightweightNode.createLightweightNode(
                BBQNode.BBQ_NODE_TYPE,
                parm2, parm2,
                "BBQ "+parm2.getDisplayName());
            node.setTool( tool);
        }
    }

    public void browserClosed(Browser parm1)
    {
        //TODO: Implement this com.borland.primetime.ide.BrowserListener method
    }

    public void browserClosing(Browser parm1) throws com.borland.primetime.util.VetoException
    {
        //TODO: Implement this com.borland.primetime.ide.BrowserListener method
    }

    public void browserDeactivated(Browser parm1)
    {
        //TODO: Implement this com.borland.primetime.ide.BrowserListener method
    }

    public void browserActivated(Browser parm1)
    {
        //TODO: Implement this com.borland.primetime.ide.BrowserListener method
    }

    public void browserOpened(Browser parm1)
    {
        //TODO: Implement this com.borland.primetime.ide.BrowserListener method
    }
}