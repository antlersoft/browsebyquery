package com.antlersoft.bbq_eclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

import com.antlersoft.analyzer.IndexAnalyzeDB;

import com.antlersoft.analyzer.query.QueryParser;

import com.antlersoft.bbq_eclipse.preferences.PreferenceConstants;

import com.antlersoft.odb.ObjectDBException;

/**
 * The main plugin class to be used in the desktop.
 */
public class Bbq_eclipsePlugin extends AbstractUIPlugin {

	//The shared instance.
	private static Bbq_eclipsePlugin plugin;
	
	private IndexAnalyzeDB m_db;
	private QueryParser m_qp;
	
	/**
	 * The constructor.
	 */
	public Bbq_eclipsePlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		if ( m_db!=null)
			m_db.closeDB();
	}

	/**
	 * Returns the shared instance.
	 */
	public static Bbq_eclipsePlugin getDefault() {
		return plugin;
	}

	private String getDBPath()
	{
		return getPreferenceStore().getString( PreferenceConstants.P_DB_PATH);
	}
	
	public static IStatus createStatus( String message, Throwable ex, int severity, int code)
	{
		return new Status(severity, "bbq_eclipse", code, message, ex);
	}
	
	public void logError( String message, Throwable ex)
		throws CoreException
	{
		IStatus status=createStatus( message, ex, IStatus.ERROR, 0);
		getLog().log( status);
		throw new CoreException( status);
	}
	
	/**
	 * Return the database
	 */
	public synchronized IndexAnalyzeDB getDB() throws CoreException
	{
		try
		{
			if ( m_db==null)
			{
				m_db=new IndexAnalyzeDB();
		        try
		        {
		            m_db.openDB( getDBPath());
		        }
		        catch ( ObjectDBException odb)
		        {
		            if ( odb.getUnderlying() instanceof java.io.InvalidClassException)
		                m_db.clearDB( getDBPath());
		            else
		                throw odb;
		        }			
			}
		}
		catch ( Exception e)
		{
			logError( e.getLocalizedMessage(), e);
		}
		return m_db;
	}
	
	/**
	 * Return the query parser
	 */
	public synchronized QueryParser getQueryParser()
	{
		if ( m_qp==null)
		{
			m_qp=new QueryParser();
		}
		return m_qp;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("bbq_eclipse", path);
	}
}
