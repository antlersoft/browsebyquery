
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

import com.borland.primetime.build.BuildListener;
import com.borland.primetime.build.BuildProcess;
import com.borland.primetime.node.Project;
import com.borland.primetime.vfs.Url;

public class BBQBuildListener extends BuildListener
{
    private boolean _isStatic;

    public BBQBuildListener( boolean isStatic)
    {
        _isStatic=isStatic;
    }

    public void buildProblem(BuildProcess parm1, Project parm2, Url parm3, boolean parm4, String parm5, int parm6, int parm7, String parm8)
    {
System.out.println( "Build problem: "+parm3.getFile()+( parm4 ? " True" : " False"));
System.out.println( "["+parm5+"] "+parm6+" "+parm7+" ["+parm8+"]");
    }

    public void buildMessage(BuildProcess parm1, String parm2, String parm3)
    {
System.out.println( "Build message ["+parm2+"] ["+parm3+"]");
    }

    public void buildStatus(BuildProcess parm1, String parm2, boolean parm3)
    {
System.out.println( "Build status "+parm2+":"+( parm3 ? "True" : "False"));
    }

    public void buildFinish(BuildProcess parm1)
    {
System.out.println( "Build finish");
    }

    public void buildStart(BuildProcess parm1)
    {
System.out.println( "Build start");
        if ( _isStatic)
            parm1.addBuildListener( new BBQBuildListener( false));
    }
}