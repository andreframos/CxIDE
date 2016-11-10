package org.eclipse.cxide;



import org.eclipse.cxide.console.CxInternalConsole;
import org.eclipse.cxide.console.CxExternalConsole;
import org.eclipse.cxide.perspectives.WorkFlowStudioPerspectiveAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import prolog.Prolog;
import thingsToSEE.FilesBundle;



/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.cxide"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private static CxExternalConsole consoleNormal;
	private static CxInternalConsole consoleDynamic;
	/**
	 * The constructor
	 * 
	 * Starting the Prolog here
	 */
	public Activator() {
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	      
		
		System.out.println("A Iniciar");
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		String dynamicLibPath = FilesBundle.getAbsFilePath("shared/libcxprolog.so");
		System.out.println(dynamicLibPath);
		
		prolog.Prolog.StartProlog(dynamicLibPath);
		
		 String rootFilePath = FilesBundle.getAbsFilePath("cxFiles/root.pl");
		 String configFilePath = FilesBundle.getAbsFilePath("cxFiles/config.pl");
		
		System.out.println("Root URL: "+rootFilePath);
		System.out.println("Config URL: "+configFilePath);
		FilesBundle.contentAssistFile=FilesBundle.getAbsFilePath("builts.xml");
		System.out.println("Builts URL: "+FilesBundle.getAbsFilePath("builts.xml"));
		
		
		Prolog.CallProlog("consult('"+rootFilePath+"')");
		Prolog.CallProlog("consult('"+configFilePath+"')");
		

	    //Adicionar um listener para mudanças de perspectiva que detecta que a perspectiva
	    //foi mudada e pode fazer algo.
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		WorkFlowStudioPerspectiveAdapter perspectiveListener = new WorkFlowStudioPerspectiveAdapter();
        workbenchWindow.addPerspectiveListener(perspectiveListener);
		  //Lançar as duas consolas do IDE
		  
		  consoleNormal = new CxExternalConsole();
		  consoleDynamic = new CxInternalConsole();
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
	 * @see org.eclipse.cxide.console.CxInternalButtonsContribution 
	 * 
	 * @return The non dynamic CxProlog Console
	 */
	public static CxExternalConsole getExternalConsole(){
		return consoleNormal;
	}
	
	/**
	 * Allows acess to the dynamic CxProlog Console
	 * This acess is required to dynamically restart the console
	 * @see 
	 * 
	 * @return The dynamic CxProlog Console
	 */
	public static CxInternalConsole getInternalConsole(){
		return consoleDynamic;
	}
}
