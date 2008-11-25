
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
package com.antlersoft.odb;

import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Object DB implementations might use Java ObjectInputStream and ObjectOutputStream
 * to read/write objects to/from persistent storage.  An implementation of this
 * interface can specify how object streams are created for this purpose, so that
 * particular implementations or optimizations can be included.
 * @author Michael A. MacDonald
 *
 */
public interface ObjectStreamCustomizer
{
    public abstract ObjectInputStream createObjectInputStream( InputStream is)
        throws IOException;

    public abstract ObjectOutputStream createObjectOutputStream(
        OutputStream os)
        throws IOException;

    public abstract void resetObjectInputStream( ObjectInputStream ois)
        throws IOException;

    public abstract void resetObjectOutputStream( ObjectOutputStream oos)
        throws IOException;

    public static BaseCustomizer BASE_CUSTOMIZER=new BaseCustomizer();

    static class BaseCustomizer implements ObjectStreamCustomizer
    {
        public ObjectInputStream createObjectInputStream( InputStream is)
            throws IOException
        {
            return new ObjectInputStream( is);
        }

        public ObjectOutputStream createObjectOutputStream(
            OutputStream os)
            throws IOException
        {
            return new ObjectOutputStream( os);
        }

        public void resetObjectInputStream( ObjectInputStream ois)
            throws IOException
        {
        }

        public void resetObjectOutputStream( ObjectOutputStream oos)
            throws IOException
        {
            oos.reset();
        }
    }
}