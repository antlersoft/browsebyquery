package com.antlersoft.bbq_eclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.framework.BundleContext;

import com.antlersoft.analyzer.IndexAnalyzeDB;

import com.antlersoft.analyzer.query.QueryParser;

import com.antlersoft.bbq_eclipse.preferences.PreferenceConstants;

import com.antlersoft.odb.DiskAllocatorException;
import com.antlersoft.odb.ObjectDBException;

/**
 * The main plugin class to be used in the desktop.
 */
public class Bbq_eclipsePlugin extends AbstractUIPlugin {

	//The shared instance.
	private static Bbq_eclipsePlugin plugin;
	
	private IndexAnalyzeDB m_db;
	private QueryParser m_qp;
	boolean _pathChanged;
	
	/**
	 * The constructor.
	 */
	public Bbq_eclipsePlugin() {
		plugin = this;
		_pathChanged=false;
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

	private void openDBAtCurrentPath() throws Exception
	{
        try
        {
            m_db.openDB( getDBPath());
            _pathChanged=false;
        }
        catch ( ObjectDBException odb)
        {
            if ( odb.getUnderlying() instanceof java.io.InvalidClassException
            		|| odb.getUnderlying() instanceof DiskAllocatorException)
                m_db.clearDB( getDBPath());
            else
                throw odb;
        }					
	}
	
	/**
	 * Return the database
	 */
	public synchronized IndexAnalyzeDB getDB() throws CoreException
	{
		try
		{
			if ( _pathChanged)
			{
				m_db.closeDB();
				openDBAtCurrentPath();
			}
			if ( m_db==null)
			{
				getPreferenceStore().addPropertyChangeListener( new IPropertyChangeListener() {
					public void propertyChange( PropertyChangeEvent pce) {
						if ( pce.getProperty().equals( PreferenceConstants.P_DB_PATH))
							synchronized ( Bbq_eclipsePlugin.this)
							{
								_pathChanged=true;
							}
					}
				});
				m_db=new IndexAnalyzeDB();
				openDBAtCurrentPath();
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
