package org.eclipse.cxide.console;

import org.eclipse.cxide.Activator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

/**
 * This class is used to add contribution to the CxNormalConsole
 * Namely a restart button on the toolbar of the console
 * @author andreramos
 *
 */
public class ConsoleButtonsContribution implements IConsolePageParticipant {

	    private Action remove, stop;
	    private IActionBars bars;
	    
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(IPageBookViewPage page, IConsole console) {
		// TODO Auto-generated method stub
	        IPageSite site = page.getSite();
	        this.bars = site.getActionBars();

	        createTerminateAllButton();
	        createRemoveButton();

	        bars.getMenuManager().add(new Separator());
	        bars.getMenuManager().add(remove);

	        IToolBarManager toolbarManager = bars.getToolBarManager();

	        toolbarManager.appendToGroup(IConsoleConstants.LAUNCH_GROUP, stop);
	        toolbarManager.appendToGroup(IConsoleConstants.LAUNCH_GROUP,remove);

	        bars.updateActionBars();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activated() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivated() {
		// TODO Auto-generated method stub

	}

	 private void createTerminateAllButton() {
	        ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(getClass(), "/icons/cx.gif");
	        this.stop = new Action("Terminate all", imageDescriptor) {
	            public void run() {
	                //code to execute when button is pressed
	            	Activator.getNormalConsole().restart();
	            }
	        };

	    }

	    private void createRemoveButton() {
	        ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(getClass(), "/icons/cx.gif");
	        this.remove= new Action("Remove console", imageDescriptor) {
	            public void run() {
	                //code to execute when button is pressed
	            }
	        };
	    }

	
}