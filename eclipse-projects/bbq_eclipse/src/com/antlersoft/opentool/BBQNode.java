
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