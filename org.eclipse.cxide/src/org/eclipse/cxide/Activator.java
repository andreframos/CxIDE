package org.eclipse.cxide;


import org.eclipse.cxide.console.CxDynamicConsole;
import org.eclipse.cxide.console.CxNormalConsole;
import org.eclipse.cxide.perspectives.WorkFlowStudioPerspectiveAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;



/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.cxide"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private static CxNormalConsole consoleNormal;
	private static CxDynamicConsole consoleDynamic;
	/**
	 * The constructor
	 * 
	 * Starting the Prolog here
	 */
	public Activator() {
		
		System.out.println("A Iniciar");
		
		prolog.Prolog.StartProlog();
		
		//Consultar ficheiros iniciais
		prolog.Prolog.CallProlog("consult('root.pl')");
	    prolog.Prolog.CallProlog("consult('config.pl')");
		
	    
	    //Adicionar um listener para mudanças de perspectiva que detecta que a perspectiva
	    //foi mudada e pode fazer algo.
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		WorkFlowStudioPerspectiveAdapter perspectiveListener = new WorkFlowStudioPerspectiveAdapter();
        workbenchWindow.addPerspectiveListener(perspectiveListener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	      
		  //Lançar as duas consolas do IDE
		  consoleDynamic = new CxDynamicConsole();
		  consoleNormal = new CxNormalConsole();
	    
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Allows acess to the normal CxProlog Console
	 * This acess is required to dynamically restart the console
	 * @see org.eclipse.cxide.console.ConsoleButtonsContribution 
	 * 
	 * @return The non dynamic CxProlog Console
	 */
	public static CxNormalConsole getNormalConsole(){
		return consoleNormal;
	}
	
	/**
	 * Allows acess to the dynamic CxProlog Console
	 * This acess is required to dynamically restart the console
	 * @see 
	 * 
	 * @return The dynamic CxProlog Console
	 */
	public static CxDynamicConsole getDynamicConsole(){
		return consoleDynamic;
	}
}
