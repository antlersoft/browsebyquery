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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.URL;

public interface AppContext
{
    /**
     * This may cache images-- use lower level interface if this
     * is undesirable
     */
    public  Image getImage( URL url);
    /**
     * Depending on implementation this may be null or come from
     * (for example) the command line
     */
    public  URL getCodeBase();
    /**
     * Command-line-type parameters
     */
    public  String getParameter( String parameter_name);
	public  void showDocument( URL url, String target);
    public  Toolkit getToolkit();
    public boolean isExitOk();
    /**
     * Input stream for configuration information.  If no config information
     * had been save, an empty input stream
     * is returned.
     */
	InputStream getConfigStream( String title) throws IOException;
	OutputStream setConfigStream( String title) throws IOException;
}
