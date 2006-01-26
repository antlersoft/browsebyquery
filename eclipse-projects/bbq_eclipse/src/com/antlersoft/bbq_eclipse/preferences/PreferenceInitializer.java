package com.antlersoft.bbq_eclipse.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		
		Bbq_eclipsePlugin plugin=Bbq_eclipsePlugin.getDefault();
		IPreferenceStore store = Bbq_eclipsePlugin.getDefault()
				.getPreferenceStore();
		store.setDefault( PreferenceConstants.P_DB_PATH, plugin.getStateLocation().append( "bbq.pj").toOSString());
	}

}
