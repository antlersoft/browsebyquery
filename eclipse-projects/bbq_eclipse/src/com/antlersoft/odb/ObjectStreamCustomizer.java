
/**
 * Title:        antlersoft java software<p>
 * Description:  antlersoft Moose
 * antlersoft BBQ<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
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