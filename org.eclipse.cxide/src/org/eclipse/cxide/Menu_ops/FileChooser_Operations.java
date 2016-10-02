package org.eclipse.cxide.Menu_ops;

import java.io.File;


import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


public class FileChooser_Operations {
	
	// a dummy JPanel needed to be the parent of the file choose dialog
	static class fc extends JPanel{
		
	}
	
	/**
	 * Invoke file chooser dialog to pick a file
	 * @return the absolute path of that file
	 */
	public static String FileChooseDialog(){
		return dialogGetPath(1);
	}
	
	/**
	 * Invoke directory chooser dialog to pick a directory
	 * @return the absolute path of that directory
	 */
	public static String DirChooseDialog(){
		return dialogGetPath(2);
	}
	
	/**
	 * Invoke a dialog to pick a file or a directory
	 * @return the absolute path of the file or directory selected
	 */
	public static String FileDirChooseDialog(){
		return dialogGetPath(3);
	}
	
	/**
	 * The method who actually creates the dialog to search for a file or directory
	 * @param type - if type==1 then dialog to search file
	 *               if type==2 then dialog to search directories
	 *               if type==3 then dialog to search file or directories
	 * @return
	 */
	private static String dialogGetPath(int type){
		String path="";
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		
		switch(type){
			case 1: 
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			break;
			
			case 2:
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			break;
			
			case 3:
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			break;
		}
		
		int result = fileChooser.showOpenDialog(new fc());
		
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    path=selectedFile.getAbsolutePath();
		}
		return path;
	}
	
	/**
	 * Verifies if a certain project already exists in the workspace
	 * DANGER: The path must be that of a project, a normal dir or file will raise an error
	 * @param path - The absolute path of the project
	 * @return - True if the project already exists in the workspace, false otherwise
	 */
	public static boolean existsProject(String path){
		System.out.println("............");
		System.out.println(path);
		IProjectDescription description = null;
		try {
			description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path(path + "/.project"));
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			System.out.println("PROBLEM");
			e1.printStackTrace();
		} 
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		IProject[] array = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(int count = 0; count <= array.length - 1; count ++){
		  if(project.equals(array[count])){
		    return true;
		  }
		}
		
		return false;
	}
	
	public static void importProject(){
		String baseDir = DirChooseDialog();// Select the location of files to import
		
		IProjectDescription description;
		IProject project=null;
		try {
			description = ResourcesPlugin.getWorkspace().loadProjectDescription(  new Path(baseDir+"/.project"));
			project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
			project.create(description, null);
			project.open(null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void exportProject(String currentPath, String copyPath){
		String baseDir = "/home/andreramos/runtime-org.eclipse.cxide/r";// Select the location of files to import
		copyPath="/home/andreramos/runtime-org.eclipse.cxide/ra";
		IProjectDescription description;
		IProject project=null;
		try {
			description = ResourcesPlugin.getWorkspace().loadProjectDescription(  new Path(baseDir+"/.project"));
			project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		
			project.copy(new Path(copyPath), false, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


public static void open_external_file_onEditor(String Path){
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	
	File fileToOpen = new File(Path);
	if (fileToOpen.exists() && fileToOpen.isFile()) {
	    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
	    
	    try {
	        IDE.openEditorOnFileStore( page, fileStore );
	    } catch ( PartInitException e ) {
	    	e.printStackTrace();
	    }
	} else {
	    //Do something if the file does not exist
		System.err.println("The File doenst exists");
	}
	
}
	
	

}
