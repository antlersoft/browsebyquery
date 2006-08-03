/**
 * Title:        AppContext
 * Description:  Abstracts services that can be provided variously by
 * applications, applets and Java Web Start
 * <p>Copyright (c) 2003-2006  Michael A. MacDonald<p>
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
