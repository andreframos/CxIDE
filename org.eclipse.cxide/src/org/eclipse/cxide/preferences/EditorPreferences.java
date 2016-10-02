/**
 * @author André Ramos, 2014
 */
package org.eclipse.cxide.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.cxide.Activator;
import org.eclipse.cxide.CxEditor.CxEditor;
import org.eclipse.cxide.CxEditor.CxScanner;
import org.eclipse.cxide.Menu_ops.FileChooser_Operations;
import org.eclipse.cxide.utilities.Editor_Utilities;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;

public class EditorPreferences extends StructuredFieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	private Group fontColors;
	private static HashMap<String,RGB> prefFields = new HashMap<String,RGB>();
	public EditorPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());

		Iterator<String> it = EditorPreferences.getPrefFields().keySet().iterator();
		while(it.hasNext()){
			final String fieldName = it.next();
		Activator.getDefault().getPreferenceStore()
		.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty() == fieldName) {
					try{
					AbstractTextEditor e = (AbstractTextEditor) Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
					RGB newColor = (RGB) event.getNewValue();
					Editor_Utilities.changeRuleColor(fieldName,newColor.red,newColor.green,newColor.blue);
					CxEditor.reinitEditorScanner();
					
					IWorkbench workbench = PlatformUI.getWorkbench();
					IWorkbenchWindow window = 
					        workbench == null ? null : workbench.getActiveWorkbenchWindow();
					IWorkbenchPage activePage = 
					        window == null ? null : window.getActivePage();

					IEditorPart editor = 
					        activePage == null ? null : activePage.getActiveEditor();
					IEditorInput input = 
					        editor == null ? null : editor.getEditorInput();
					IPath path = input instanceof FileEditorInput 
					        ? ((FileEditorInput)input).getPath()
					        : null;
					if (path != null)
					{

						IEditorPart te = ((ITextEditor) Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor());
						IEditorReference[] ed=Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().
								findEditors(null, "org.eclipse.cxide.CxEditor", 0);
						
						for (int i=0; i<ed.length; i++)
							System.out.println(ed[i].getName());
							//te.getDocumentProvider().resetDocument((te.getEditorInput()));
							//Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(te, true);
						//System.out.println(path);
						//FileChooser_Operations.open_external_file_onEditor(path.toString());
					    // Do something with path.
					}
					
					
					
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				}
		});
		}
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		fontColors = new Group(getFieldEditorParent(), SWT.NONE);
		fontColors.setText("Font Color: CxEditor Syntax Highlighting");

		Iterator<String> it = prefFields.keySet().iterator();
		while(it.hasNext()){
			String name = it.next();
			ColorFieldEditor newField = getColorFieldEditor(
					name,
					name, fontColors);
			addField(newField);
			
		}
		
	/*	ColorFieldEditor stringColor = getColorFieldEditor(
				PreferenceConstants.STRING_COLOR_NAME_PREFERENCE,
				PreferenceConstants.STRING_COLOR_NAME_PREFERENCE, fontColors);
		ColorFieldEditor otherColor = getColorFieldEditor(
				PreferenceConstants.OTHERS_COLOR_NAME_PREFERENCE,
				PreferenceConstants.OTHERS_COLOR_NAME_PREFERENCE, fontColors);
		ColorFieldEditor variableColor = getColorFieldEditor(
				PreferenceConstants.VARIABLE_COLOR_NAME_PREFERENCE,
				PreferenceConstants.VARIABLE_COLOR_NAME_PREFERENCE, fontColors);
		ColorFieldEditor builtinsColor = getColorFieldEditor(
				PreferenceConstants.BUILTINS_COLOR_NAME_PREFERENCE,
				PreferenceConstants.BUILTINS_COLOR_NAME_PREFERENCE,
				predicateColors);
		ColorFieldEditor userDefinedColor = getColorFieldEditor(
				PreferenceConstants.USER_DEFINED_COLOR_NAME_PREFERENCE,
				PreferenceConstants.USER_DEFINED_COLOR_NAME_PREFERENCE,
				predicateColors);

		addField(commentColor);
		addField(variableColor);
		addField(stringColor);
		addField(otherColor);
		addField(builtinsColor);
		addField(userDefinedColor);
*/
	}

	@Override
	protected void adjustGridLayout() {
		super.adjustGridLayout();

		setColumsWithEqualWidth(fontColors);
	}

	private void setColumsWithEqualWidth(Composite composite) {
		Layout layout = composite.getLayout();
		if (layout instanceof GridLayout) {
			GridLayout gridLayout = (GridLayout) layout;
			gridLayout.makeColumnsEqualWidth = true;
		}
	}

	private MyColorFieldEditor getColorFieldEditor(String name,
			String labelText, Composite parent) {
		MyColorFieldEditor editor = new MyColorFieldEditor(name, labelText,
				parent);
		Label labelControl = editor.getLabelControl();
		if (labelControl != null) {
			labelControl.setLayoutData(getGridData());
		}
		ColorSelector colorSelector = editor.getColorSelector();
		if (colorSelector != null) {
			colorSelector.getButton().setLayoutData(getGridData());
		}
		return editor;
	}

	private GridData getGridData() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.minimumWidth = SWT.DEFAULT;
		return gridData;
	}
	
	public static void addPrefField(String fieldName, int r, int g, int b){
		prefFields.put(fieldName, new RGB(r,g,b));
	}
	
	
	
	public static HashMap<String, RGB> getPrefFields(){
		return prefFields;
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