package org.eclipse.cxide.Menu_ops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.cxide.console.CxDynamicConsole;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchWindow;

import prolog.Prolog;


/*
 * Class that allows to create, delete or alter Menus
 */
public class Menu_Operations {

	//All the menus created by the user 
	//private static ArrayList<MenuManager> menusCreated = new ArrayList<MenuManager>();
	
	private static Hashtable<String,CxMenu> menusCreated = new Hashtable<String,CxMenu>();
	
	//The current workbench window
	private static IWorkbenchWindow  window = Workbench.getInstance().getActiveWorkbenchWindow();
	
	//The main menu of the current workbench window
	private static MenuManager menuManager = ((WorkbenchWindow)window).getMenuManager();
	
	/**
	 * Shows all the indexes and respective ids of the menus created by the user
	 * If no menus of such kind exist it prints: No Menus Created
	 */
	public static void printMenusCreated(){
		System.out.println("---------------------------");
		System.out.println("D-> Menu_Operations:printMenus");
		int index=0;
		
		if(menusCreated.size()==0)
			System.out.println("No Menus Created");
		else{	
			Iterator<String> it = menusCreated.keySet().iterator();
			
			while(it.hasNext()){
				System.out.println(it.next());
			}
		}
		System.out.println("--------------------------");
		
	}
	
	/**
	 * Get a menu created by the user
	 * @param index of the menu created by the user
	 * @return the menu with the given index, or null if such menu does not exist
	 */
	
	public static MenuManager getMenuCreated(int index){
		System.out.println("D-> Menu_Operations:getMenuCreated");
		return menusCreated.get(index);
	}
	
	/**
	 * Creates a new Menu in the main menu bar
	 * @param menu_name - The new menu name
	 * @param menu_id - The new menu ID
	 */
	
	public static void createMenu(String menu_name){
		System.out.println("D-> Menu_Operations:createMenu");
		class About1 extends Action {
	        public About1() {
	            super("Zero item", AS_PUSH_BUTTON);
	        }
	        public void run() {
	        	
	        	Prolog.CallProlog("writeln('Need to define an action')");
	        	 CxDynamicConsole.runDynamicConsole();
	        }
	    }
		
		String menu_id = menu_name+"_ID";
		CxMenu menu = new CxMenu(menu_name, menu_id);
		
		menu.add(new About1());
	    menuManager.add(menu);
	    menusCreated.put(menu_id, menu);
	    menuManager.update();
	    ((WorkbenchWindow) window).updateActionBars();

	    
	    
	}
	
	
	
	public static void createMenu(String menu_name,String item_name, String action){
		System.out.println("D-> Menu_Operations:createMenu with action String & item_name");
		final String action_ = action;
		final String item = item_name;
		class About1 extends Action {
	        public About1() {
	            super(item, AS_PUSH_BUTTON);
	        }
	        public void run() {
	        	Prolog.CallProlog(action_);
	        	 CxDynamicConsole.runDynamicConsole();
	        }
	    }
		
	
		String menu_id = menu_name+"_ID";
		CxMenu menu = new CxMenu(menu_name, menu_id);
		menu.add(new About1());
	    menuManager.add(menu);
	    menuManager.update();
	    menusCreated.put(menu_id, menu);
	    ((WorkbenchWindow) window).updateActionBars();
	    
	}
	
	public static void c(String a, String[] vec){
		System.out.println(a);
		System.out.println(Arrays.toString(vec));
		System.out.println(vec[0]);
		System.out.println(vec[1]);
		//System.out.println(vec[2]);
		;
	}
	
	public static void createMenu(String menu_name,String[] actions){
		System.out.println("D-> Menu_Operations:createMenu with action String & item_name");
		
		
		CxMenu menu = new CxMenu(menu_name, menu_name+"_ID");
		
		for (int i=0; i<actions.length ; i+=2)
			menu.add(createAction(actions[i],actions[i+1]));
		
		
		String menu_id = menu_name+"_ID";
	    menuManager.add(menu);
	    menuManager.update();
	    menusCreated.put(menu_id,menu);
	    ((WorkbenchWindow) window).updateActionBars();
	    
	}
	
	public static void createMenu(String menu_name,String prolog_cmd){
	
		//O novo menu a ser adicionado
		MenuManager menu = new MenuManager(menu_name, menu_name);
		//A ação que define o comportamento do menu
		Action prologAction = createAction(prolog_cmd, prolog_cmd);
		//Adicionar a ação ao novo menu
		menu.add(prologAction);
		//O novo menu será sub-menu da barra principal de menus
	    menuManager.add(menu);
	    menuManager.update();
	}
	
	private static Action createAction(String action_name, String action_cmd){
		final String action_Name = action_name;
		final String action_CMD = action_cmd;
		class About1 extends Action {
	        public About1() {
	            super(action_Name, AS_PUSH_BUTTON);
	        }
	        public void run() {
	        	Prolog.CallProlog(action_CMD);
	        	 CxDynamicConsole.runDynamicConsole();
	        }
	    }
		
		return new About1();
	}
	
	

	
	
	
	/**
	 * Creates a new Sub-Menu to be added to the parent Menu. This sub_menu will only have an action
	 * @param menuManager - The MenuManager where the parent menu belongs
	 * @param parentID - The ID of the parent Menu, this is the Menu in which the subMenu will be added
	 * @param subMenu_name - The name of the new sub-menu
	 * @param subMenu_Id - The Id of the new sub-menu
	 * @param action - The only action of the sub-menu
	 */
	public static void createSubMenu(MenuManager menuManager, String parentID, String subMenu_name, String subMenu_Id, Action action){
		System.out.println("D-> Menu_Operations:createSubMenu with Action");
		MenuManager parent = (MenuManager) menuManager.find(parentID);
		
		CxMenu subMenu = new CxMenu(subMenu_name, subMenu_Id);
		subMenu.add(action);
		parent.add(subMenu);
		parent.update();
		menuManager.update();
		menusCreated.put(subMenu_Id,subMenu);
		  ((WorkbenchWindow) window).updateActionBars();
	}
	
	/**
	 * Creates a new Sub-Menu to be added to the parent Menu. This sub_menu will only have an action
	 * @param menuManager - The MenuManager where the parent menu belongs
	 * @param parentID - The ID of the parent Menu, this is the Menu in which the subMenu will be added
	 * @param subMenu_name - The name of the new sub-menu
	 * @param subMenu_Id - The Id of the new sub-menu
	 * @param actions - The only action of the sub-menu
	 */
	public static void createSubMenu(MenuManager menuManager, String parentID, String subMenu_name, String subMenu_Id, ArrayList<Action> actions){
		System.out.println("D-> Menu_Operations:createSubMenu with Action Array");
		
		MenuManager parent = (MenuManager) menuManager.find(parentID);
		
		CxMenu subMenu = new CxMenu(subMenu_name, subMenu_Id);
		Iterator<Action> it = actions.iterator();
		
		while(it.hasNext())
			subMenu.add(it.next());
		
		parent.add(subMenu);
		parent.update();
		menuManager.update();
		menusCreated.put(subMenu_Id,subMenu);
		  ((WorkbenchWindow) window).updateActionBars();
	}
	
	/**
	 * Deletes a menu with the given Id
	 * @param menuManager
	 * @param item_id
	 */
	public static void deleteMenu_byID(String menu_id){
		System.out.println("D-> Menu_Operations:deleteMenu with Id");
		
		MenuManager p_menu; 
		IContributionItem item; 
		
		if(menusCreated.containsKey(menu_id)){
			p_menu = (MenuManager) menusCreated.get(menu_id).getParent(); 
		    item = menusCreated.get(menu_id);
		}else{
			item = menuManager.find(menu_id);
			p_menu = (MenuManager) ((MenuManager)item).getParent();
			
		}
		 
		 
		  if (item != null) {
		        // clean old one
		       p_menu.remove(item);

		        // refresh menu gui
		        p_menu.update();
		    }
		  ((WorkbenchWindow) window).updateActionBars();
	}
	
	/**
	 * Deletes a menu with the given Id
	 * @param menuManager
	 * @param item_id
	 */
	public static void deleteMenu_byID(MenuManager menuManager, String item_id){
		System.out.println("D-> Menu_Operations:deleteMenu with Id");
		 IContributionItem item = menuManager.find(item_id);
		 
		 
		  if (item != null) {
		        // clean old one
		        menuManager.remove(item);

		        // refresh menu gui
		        menuManager.update();
		    }
		  ((WorkbenchWindow) window).updateActionBars();
	}
	
	/**
	 * Deletes a menu and all its sub-menus
	 * @param menuManager - the Menu to be deleted
	 * TODO só apaga os sub-menus se o menu eliminado foi criado pelo utilizador
	 */
	
	public static void deleteMenu(String menu_id){
		System.out.println("D-> Menu_Operations:deleteMenu and all his sub-menus");
		
		if(menusCreated.containsKey(menu_id)){ //Se o menu foi criado pelo utilizador
		
		ArrayList<String> toRemove = new ArrayList<String>(); 
		Iterator<String> it = menusCreated.keySet().iterator();
		 while(it.hasNext()){
			 String name = it.next();
			 System.out.println("A avaliar " +name);
			 
			 
			 MenuManager aux = menusCreated.get(name);
			 
			 MenuManager p_aux = (MenuManager) aux.getParent();
			 System.out.println("Criado");
			 if(aux!=null && p_aux!=null)
			 {
				if(aux.getId().equals(menu_id)){
				 	System.out.println("Encontrou: "+menu_id);
				 	deleteMenu_byID(menu_id);
				 	toRemove.add(menu_id);
				 	//printMenusCreated();
			 	}
			 	System.out.println("Aki");
			 	if(p_aux.getId().equals(menu_id)){
			 		deleteMenu(aux.getId());
			 	}
			 }
			 System.out.println("----Processed-----");
		 }
		        //menuManager.removeAll();
		 
		 Iterator<String> it2 = toRemove.iterator();
		 
		 while (it2.hasNext())
			 menusCreated.remove(it2.next());
		 
		}else{ //Se não for um menu criado pelo utilizador
			MenuManager p_menu; 
			IContributionItem item; 
			item = menuManager.find(menu_id);
			p_menu = (MenuManager) ((MenuManager)item).getParent();

			  if (item != null) {
			        // clean old one
			       p_menu.remove(item);

			        // refresh menu gui
			        p_menu.update();
			    }
			
			
		}
		 
		        System.out.println("KABOUUUUUUUUU");

		        ((WorkbenchWindow) window).updateActionBars();
	}
	
	/**
	 * Deletes a menu and all its sub-menus
	 * @param menuManager - the Menu to be deleted
	 */
	
	public static void deleteMenu(MenuManager menuManager){
		System.out.println("D-> Menu_Operations:deleteMenu and all his sub-menus");
		 
		 int index=0;
		 while(index<menusCreated.size()){
			 
			 MenuManager aux = menusCreated.get(index);
			 System.out.println("aux = "+aux.getId());
			 MenuManager p_aux = (MenuManager) aux.getParent();
			 System.out.println("p_aux = "+p_aux.getId());
			 
			 if(aux.getId().equals(menuManager.getId())){
				 System.out.println("Found EQUAL at "+index);
				 menusCreated.remove(index);
				 printMenusCreated();
				 index--;
			 }
			 if(p_aux.getId().equals(menuManager.getId())){
				 System.out.println("Found a son at "+index);
				 deleteMenu(aux);
			 }
			 index++;
			 System.out.println("----Processed-----");
		 }
		        menuManager.removeAll();
		        

		        ((WorkbenchWindow) window).updateActionBars();
	}
	
	public static void addContextMenuAction(){
	}
	
	
	
	/*
	static class Ch4_StatusAction extends Action
	{
	  StatusLineManager statman;
	  short triggercount = 0;
	  public Ch4_StatusAction(StatusLineManager sm) 
	  {
	    super("&Trigger@Ctrl+T", AS_PUSH_BUTTON);
	    statman = sm;
	    setToolTipText("Trigger the Action"); 
	    //setImageDescriptor(ImageDescriptor.createFromFile
	      //(this.getClass(),"eclipse.gif"));
	  }
	  public void run() 
	  {
	    triggercount++;
	    statman.setMessage("The status action has fired. Count: " + 
	      triggercount);
	  }
	}*/
	
}
