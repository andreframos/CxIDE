package org.eclipse.cxide;

import java.net.URI;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/*
 * Class of the CxProlog Project Wizard
 */

public class CustomProjectNewWizard extends Wizard implements INewWizard, IExecutableExtension {

	private static final String WIZARD_NAME = "CX WIZARD";
	
	//The page of the wizard
	private WizardNewProjectCreationPage _pageOne;
	
	private IConfigurationElement _configurationElement;
	
	
	public CustomProjectNewWizard() {
		setWindowTitle(WIZARD_NAME);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 * What to do when wizard is finished
	 */
	
	@Override
	public boolean performFinish() {
	    String name = _pageOne.getProjectName();
	    URI location = null;
	    if (!_pageOne.useDefaults()) {
	        location = _pageOne.getLocationURI();
	    } // else location == null
	 
	    CustomProjectSupport.createProject(name, location);
	   
	    BasicNewProjectResourceWizard.updatePerspective(_configurationElement);
	    
	    return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 * Info of the page of the wizard
	 */
	@Override
	public void addPages() {
	    super.addPages();
	 
	    _pageOne = new WizardNewProjectCreationPage("CxProlog Project Wizard");
	    _pageOne.setTitle("CxProlog Project Wizard");
	    _pageOne.setDescription("Create a new CxProlog Project");
	 
	    addPage(_pageOne);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 * The default info of the wizard
	 */
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
	    _configurationElement = config;
	}
}
