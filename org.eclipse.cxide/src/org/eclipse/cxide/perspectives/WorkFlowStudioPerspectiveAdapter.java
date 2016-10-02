package org.eclipse.cxide.perspectives;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;


/**
 * Forçar atualização de perspectiva aquanda da sua mudança
 * @author andreramos
 *
 */

public class WorkFlowStudioPerspectiveAdapter extends PerspectiveAdapter {
	 

 
@Override
    public void perspectiveActivated(IWorkbenchPage page,
            IPerspectiveDescriptor perspectiveDescriptor) {
        super.perspectiveActivated(page, perspectiveDescriptor);
        // --- Update main menu and cool bar
        System.out.println("Perspective change detected");
        IWorkbenchWindow workbenchWindow =  PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (workbenchWindow instanceof WorkbenchWindow)
        {
            ((WorkbenchWindow)workbenchWindow).getMenuBarManager().update(true);
            ((WorkbenchWindow)workbenchWindow).getCoolBarManager2().update(true);
        }
}
}