
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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.borland.primetime.node.*;

public class BBQNode extends LightweightNode
{
    private static Icon icon;
    public static final String BBQ_NODE_TYPE="BBQ_NODE_TYPE";
    private BBQTool tool;

    public BBQNode( Project proj, Node parent, String name)
    {
        super( proj, parent, name);
        if ( icon==null)
            icon=new ImageIcon( getClass().getResource( "bbqicon.gif"));
    }

    public Icon getDisplayIcon()
    {
        return icon;
    }

    public int getDisplaySequence()
    {
        return 110;
    }

    void setTool( BBQTool t)
    {
        tool=t;
    }

    BBQTool getTool()
    {
        return tool;
    }
}