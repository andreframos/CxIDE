package org.eclipse.cxide;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class CxFileWizardPage extends WizardNewFileCreationPage {


    public CxFileWizardPage(IStructuredSelection selection) {
        super("NewConfigFileWizardPage", selection);
        setTitle("CxProlog File");
        setDescription("Creates a new CxProlog File");
        setFileExtension("pl");
    }

    
    /**protected InputStream getInitialContents() {
        try {
            return Activator.getDefault().getBundle().getEntry("/resources/newFileContents.config").openStream();
        } catch (IOException e) {
            return null; // ignore and create empty comments
        }*/
    }
		
