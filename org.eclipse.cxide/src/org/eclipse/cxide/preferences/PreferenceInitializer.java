package org.eclipse.cxide.preferences;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.cxide.Activator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

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
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		HashMap<String, RGB> prefsFields = EditorPreferences.getPrefFields();
		Iterator<String> it = prefsFields.keySet().iterator();
		
		while(it.hasNext()){
			String name = it.next();
			RGB color = prefsFields.get(name);
			PreferenceConverter.setDefault(store, name, color);
			store.setToDefault(name);
		}
		
		store.setDefault(PreferenceConstants.P_BOOLEAN, true);
		store.setDefault(PreferenceConstants.SV_PORT, PreferenceConstants.SV_PORT_VALUE);
		store.setDefault(PreferenceConstants.CX_PATH,
				PreferenceConstants.CX_PATH);
		store.setDefault(PreferenceConstants.SV_IP,
				PreferenceConstants.SV_IP_VALUE);
	}

}
