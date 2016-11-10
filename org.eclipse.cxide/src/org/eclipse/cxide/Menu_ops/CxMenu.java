package org.eclipse.cxide.Menu_ops;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


/**
 * Todos os menus criados através de CxProlog são CxMenus
 * Assim é possível especificar comportamentos inerentes aos CxMenus.
 * Por exemplos os CxMenus podem ser definidos como apenas visíveis
 * se a perspectiva atual for a perspectiva CxProlog
 * @author andreramos
 *
 */
public class CxMenu extends MenuManager {
	private LinkedList<Object> subMenus;
	
	public CxMenu(String menu_name, String menu_id){
		super(menu_name, menu_id);
		subMenus=new LinkedList<Object>();
	}
	
	public void addSubmenu(Object subMenu){
		subMenus.add(subMenu);
	}
	
	public Iterator<Object> getSubMenus(){
		return subMenus.iterator();
	}
	
	@Override
	public boolean isVisible(){
		 IWorkbenchWindow workbenchWindow =  PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	        if (workbenchWindow==null)
	            return false;
	        IWorkbenchPage activePage = workbenchWindow.getActivePage();
	        if (activePage==null)
	            return false;
	        if (activePage.getPerspective().getId().contains("org.eclipse.cxide.perspective"))
	            return true;
	        return false;
	} }
