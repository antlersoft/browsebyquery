/**
 * Title:        AppContext
 * Description:  Abstracts services that can be provided variously by
 * applications, applets and Java Web Start
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.appcontext;

import java.awt.Image;
import java.awt.Toolkit;

import java.net.URL;

public interface AppContext
{
    public  Image getImage( URL url);
    public  URL getCodeBase();
    public  String getParameter( String parameter_name);
	public  void showDocument( URL url, String target);
    public  Toolkit getToolkit();
    public boolean isExitOk();
}
