
/**
 * Title:        Remote File System for OpenTools<p>
 * Description:  An OpenTools vfs filesystem for accessing files on a remote host<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.opentool;

import com.borland.primetime.properties.NodeArrayProperty;
import com.borland.primetime.properties.NodeProperty;
import com.borland.primetime.properties.PropertyGroup;
import com.borland.primetime.properties.PropertyPage;
import com.borland.primetime.properties.PropertyPageFactory;

import com.borland.jbuilder.node.JBProject;

public class BBQPathsGroup implements PropertyGroup
{
    private static String CATEGORY="com.antlersoft.bbq";
    private static String PATH_NAME="DBPath";
    private static String CLASS_PATH="ClassPath";
    private static String UPDATE_TIME="UpdateTime";
    private static String IMPORTS="Imports";
    private static String RECENT_QUERIES="RecentQueries";

    static NodeProperty pathsProperty=new NodeProperty(
        CATEGORY, PATH_NAME, "");
    static NodeProperty classPathProperty=new NodeProperty(
        CATEGORY, CLASS_PATH, "");
    static NodeProperty updateTimeProperty=new NodeProperty(
        CATEGORY, UPDATE_TIME, "0");
    static NodeArrayProperty importsProperty=new NodeArrayProperty( CATEGORY,
        IMPORTS);
    static NodeArrayProperty recentQueriesProperty=new NodeArrayProperty( CATEGORY,
        RECENT_QUERIES);

    public BBQPathsGroup()
    {
    }

    public void initializeProperties()
    {
    }

    public PropertyPageFactory getPageFactory(final Object topic)
    {
        if ( topic instanceof BBQNode)
        {
            return new PropertyPageFactory( "BBQ", "BBQ Database Path") {
                    public PropertyPage createPropertyPage()
                    {
                        return new BBQPathsPage( (BBQNode)topic);
                    }
                };
        }
        else
            return null;
    }
}