package org.eclipse.cxide.preferences;

import org.eclipse.cxide.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class CxPrologPreferences extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {

public CxPrologPreferences() {
super(GRID);
setPreferenceStore(Activator.getDefault().getPreferenceStore());
setDescription("Configure here your CxIDE");
}

/**
* Creates the field editors. Field editors are abstractions of the common
* GUI blocks needed to manipulate various types of preferences. Each field
* editor knows how to save and restore itself.
*/
public void createFieldEditors() {

addField(new FileFieldEditor(PreferenceConstants.CX_PATH,
		"CxProlog Path:", getFieldEditorParent()));

addField(new StringFieldEditor(PreferenceConstants.SV_IP, "Server Ip:",
		getFieldEditorParent()));

addField(new IntegerFieldEditor(PreferenceConstants.SV_PORT,
		"Server Port:", getFieldEditorParent()));

}

@Override
protected void adjustGridLayout() {
super.adjustGridLayout();
}

/*
* (non-Javadoc)
* 
* @see
* org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
*/
public void init(IWorkbench workbench) {
}

}
