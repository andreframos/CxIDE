package org.eclipse.cxide.CxEditor;



import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.cxide.Activator;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;

import prolog.Prolog;



public class MyReconciler implements IReconcilingStrategy, IReconcilingStrategyExtension {

	@Override
	public void setDocument(IDocument document) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		// TODO Auto-generated method stub
		
	}
	

	int i=0;
	private static class VerifyErrors implements Runnable {
		
	    
		public void run() {
	    	
	    	Prolog.CallProlog("editor_getCodeErrors");
	    }
	}
	

	
	@Override
	public void reconcile(IRegion partition) {
		// TODO Auto-generated method stub
		
		Activator.getDefault().getWorkbench().getDisplay().asyncExec(new VerifyErrors());
		System.out.println(i);
		
		
	}

	@Override
	public void setProgressMonitor(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialReconcile() {
		// TODO Auto-generated method stub
		Activator.getDefault().getWorkbench().getDisplay().asyncExec(new VerifyErrors());
	}

}
