/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
package com.antlersoft.bbq_eclipse;

import java.io.FileWriter;

import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

/**
 * @author Michael A. MacDonald
 *
 */
class BBQSaveParticipant implements ISaveParticipant {
	
	private Bbq_eclipsePlugin m_plugin;
	
	private String getNumberedName( int number)
	{
		return Bbq_eclipsePlugin.ENVIRONMENT_FILE_KEY + Integer.toString( number);
	}
	
	BBQSaveParticipant( Bbq_eclipsePlugin plugin)
	{
		m_plugin=plugin;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.ISaveParticipant#doneSaving(org.eclipse.core.resources.ISaveContext)
	 */
	public void doneSaving(ISaveContext context) {
        // delete the old saved state since it is not necessary anymore
		// TODO: 3.2 says don't use java.io.File here anymore
        m_plugin.getStateLocation().append(getNumberedName( context.getPreviousSaveNumber())).toFile().delete();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.ISaveParticipant#prepareToSave(org.eclipse.core.resources.ISaveContext)
	 */
	public void prepareToSave(ISaveContext context) throws CoreException {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.ISaveParticipant#rollback(org.eclipse.core.resources.ISaveContext)
	 */
	public void rollback(ISaveContext context) {
        // since the save operation has failed, delete the saved state we have just written
		// TODO: 3.2 says don't use java.io.File here anymore
        m_plugin.getStateLocation().append(getNumberedName( context.getSaveNumber())).toFile().delete();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.ISaveParticipant#saving(org.eclipse.core.resources.ISaveContext)
	 */
	public void saving(ISaveContext context) throws CoreException {
		// TODO: 3.2 says don't use java.io.File here anymore
		try {
	        switch (context.getKind()) {
	        case ISaveContext.FULL_SAVE:
	        case ISaveContext.PROJECT_SAVE:
	           // save the plug-in state
	           String saveFileName = getNumberedName( context.getSaveNumber());
	           FileWriter writer=new FileWriter( m_plugin.getStateLocation().append(saveFileName).toFile());
	           m_plugin.getQueryParser().writeEnvironment( writer );
	           writer.close();
	           // if we fail to write, an exception is thrown and we do not update the path
	           context.map(new Path(Bbq_eclipsePlugin.ENVIRONMENT_FILE_KEY), new Path(saveFileName));
	           context.needSaveNumber();
	           break;
	        case ISaveContext.SNAPSHOT:
	        	m_plugin.getDB().commit();
	           break;
	        }
		}
        catch ( Exception e)
        {
        	m_plugin.logError( e.getLocalizedMessage(), e);
        }
	}
}
