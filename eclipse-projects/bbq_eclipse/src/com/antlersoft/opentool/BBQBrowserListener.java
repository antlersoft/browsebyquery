
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