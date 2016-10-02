package org.eclipse.cxide.preferences;

import org.eclipse.swt.widgets.Composite;

interface FieldEditorForStructuredPreferencePage {

	void adjustColumns(int numColumns);
	
	Composite getParent();
	
}



