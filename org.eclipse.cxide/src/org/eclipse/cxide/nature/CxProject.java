package org.eclipse.cxide.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class CxProject implements IProjectNature {
	 private IProject project;
	 public static final String NATURE_ID = "org.eclipse.cxide.CxNatureId";
	@Override
	public void configure() throws CoreException {
		// TODO Auto-generated method stub
		  // only called once the nature has been set
	    
	    // configure the project...
	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub
		// only called once the nature has been set    
	    
	    // reset the project configuration.
	}

	@Override
	public IProject getProject() {
		// TODO Auto-generated method stub
		return project;
	}

	@Override
	public void setProject(IProject project) {
		// TODO Auto-generated method stub
	    this.project = project;
	}

}
