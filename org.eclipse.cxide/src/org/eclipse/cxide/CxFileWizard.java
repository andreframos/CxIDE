package org.eclipse.cxide;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

public class CxFileWizard extends Wizard implements INewWizard{

	private IStructuredSelection selection;
	    private CxFileWizardPage newFileWizardPage;
	    private IWorkbench workbench;
	 

	    public CxFileWizard() {

	        setWindowTitle("New Config File");

	    } 


	    @Override
	    public void addPages() {

	        newFileWizardPage = new CxFileWizardPage(selection);
	        addPage(newFileWizardPage);
	    }
	   
	    @Override
	    public boolean performFinish() {
	       
	        IFile file = newFileWizardPage.createNewFile();
	     
	        try {
				IDE.openEditor(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage(), file);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        if (file != null)
	            return true;
	        else
	            return false;
	    }

	    public void init(IWorkbench workbench, IStructuredSelection selection) {
	        this.workbench = workbench;
	        this.selection = selection;
	    }
	}


