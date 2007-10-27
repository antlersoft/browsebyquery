package com.antlersoft.bbq_eclipse;

import java.io.File;
import java.io.FileReader;

import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.framework.BundleContext;

import com.antlersoft.analyzer.IndexAnalyzeDB;


import com.antlersoft.bbq_eclipse.preferences.PreferenceConstants;

import com.antlersoft.odb.DiskAllocatorException;
import com.antlersoft.odb.ObjectDBException;
import com.antlersoft.query.environment.AnalyzerQuery;

/**
 * The main plugin class to be used in the desktop.
 */
public class Bbq_eclipsePlugin extends AbstractUIPlugin {

	//The shared instance.
	private static Bbq_eclipsePlugin plugin;
	
	private IndexAnalyzeDB m_db;
	private AnalyzerQuery m_qp;
	boolean _pathChanged;
	
	static final String ENVIRONMENT_FILE_KEY = "environment";
	
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
		
		// TODO:
		// From save participant documentation example; note
		// that we should use the alternative to java.io.File in Eclipse 3.2
		
        ISaveParticipant saveParticipant = new BBQSaveParticipant( this);
        ISavedState lastState =
           ResourcesPlugin.getWorkspace().addSaveParticipant(this, saveParticipant);
        if (lastState == null)
           return;
        IPath location = lastState.lookup(new Path(ENVIRONMENT_FILE_KEY));
        if (location == null)
           return;
        // the plugin instance should read any important state from the file.
        File f = getStateLocation().append(location).toFile();
        if ( f.canRead())
        {
        	try
        	{
        		FileReader reader=new FileReader( f);
        		getQueryParser().readEnvironment( reader);
        		reader.close();
        	}
        	catch ( Exception e)
        	{
        		logNoThrow( e.getLocalizedMessage(), e);
        	}
        }
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
		return new Status(severity, "com.antlersoft.bbq_eclipse", code, message, ex);
	}
	
	private IStatus logNoThrow( String message, Throwable ex)
	{
		IStatus status=createStatus( message, ex, IStatus.ERROR, 0);
		getLog().log( status);
		return status;
	}
	
	public void logError( String message, Throwable ex)
		throws CoreException
	{
		throw new CoreException( logNoThrow( message, ex));
	}
	
	public synchronized void clearDB() throws CoreException
	{
		try
		{
			getDB().clearDB( getDBPath());
		}
		catch ( Exception e)
		{
			logError( "Error clearing BBQ database:"+e.getLocalizedMessage(), e);
		}
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
            m_db.clearDB( getDBPath());
            try
            {
            	logError( "There was a problem opening the BBQ database; the database was cleared and must be rebuilt before use",odb);
            }
            catch ( CoreException ce)
            {
            	
            }
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
	public synchronized AnalyzerQuery getQueryParser()
	{
		if ( m_qp==null)
		{
			m_qp=new AnalyzerQuery();
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
		return AbstractUIPlugin.imageDescriptorFromPlugin("com.antlersoft.bbq_eclipse", path);
	}
}
