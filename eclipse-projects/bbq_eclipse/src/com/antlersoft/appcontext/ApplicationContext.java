package com.antlersoft.appcontext;

import java.awt.Image;
import java.awt.Toolkit;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.URL;

import java.util.Properties;

import com.antlersoft.util.URLFrame;

public class ApplicationContext implements AppContext
{
	private Properties m_properties;

	public ApplicationContext( String[] args)
	{
		m_properties=new Properties();
		for ( int i=0; i<args.length; i++)
		{
			int equal_pos=args[i].indexOf( '=', 1);
			if ( equal_pos>=0)
			{
				m_properties.setProperty( args[i].substring( 0,
					equal_pos), args[i].substring( equal_pos+1));
			}
		}
	}

	public Image getImage( URL url)
	{
		return Toolkit.getDefaultToolkit().getImage( url);
	}
	public java.net.URL getCodeBase()
	{
		try
		{
			return new URL( m_properties.getProperty( "CODEBASE"));
		}
		catch (  Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String getParameter( String parameter_name)
	{
		return m_properties.getProperty( parameter_name);
	}

	public void showDocument( URL url, String target)
	{
		URLFrame frame=new URLFrame( url);
		frame.setSize( 400, 300);
		frame.show();
	}

	public Toolkit getToolkit()
	{
		return Toolkit.getDefaultToolkit();
	}

    public boolean isExitOk()
    {
    	return true;
    }

    /**
     * Input stream for configuration information.  If no config information
     * had been save, an empty input stream
     * is returned.
     */
	public InputStream getConfigStream( String title)
		throws IOException
	{
		File file=getConfigFile( title);
		if ( ! file.canRead())
		{
			return new ByteArrayInputStream( new byte[0]);
		}
		return new FileInputStream( file);
	}

	public OutputStream setConfigStream( String title)
		throws IOException
	{
		return new FileOutputStream( getConfigFile( title));
	}

	private static File getConfigFile( String title)
	{
		String home_directory=System.getProperty( "user.home");
		return new File( home_directory, "."+title);
	}

	public static void main( String[] args)
		throws Exception
	{
		AppContext context=new ApplicationContext( args);
		Starter.start( context);
	}
}
