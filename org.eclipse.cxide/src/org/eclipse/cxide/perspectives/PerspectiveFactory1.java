package org.eclipse.cxide.perspectives;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import thingsToSEE.FilesBundle;


/**
 * The definition of the CxProlog Perspective
 * Doenst do a lot, only defines that when the perspective is open
 * it should open in de CxEditor the config.pl file
 * @author andreramos
 *
 */
public class PerspectiveFactory1 implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			//TODO: Change the path of root.pl to something general
			IFileStore fileStore = EFS
					.getLocalFileSystem()
					.getStore(
							new Path(FilesBundle.getAbsFilePath("config.pl")));
			IDE.openEditorOnFileStore(page, fileStore);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Problem");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
