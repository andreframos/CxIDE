package org.eclipse.cxide.Menu_ops;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import prolog.Prolog;
public class EditorContextMenu extends ContributionItem {

	// Data structure (List of String Lists) containing info of the new items to be added to
	// the Editor context menu
	// A list represents a new MenuItem: 
	// -> The first element of the list is the item name
	// -> The second element of the list is the command
	private static List<List<String>> items_info = new ArrayList<List<String>>();
	
	
	public EditorContextMenu() {
		// TODO Auto-generated constructor stub
	}

	public EditorContextMenu(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void fill(Menu menu, int index) {
        //Here you could get selection and decide what to do
        //You can also simply return if you do not want to show a menu
      
        //Create the base MenuItem CxProlog
        MenuItem openItem = new MenuItem(menu, SWT.CASCADE);
        openItem.setText("&CxProlog");
        //Create a menu to associate with the MenuItem CxProlog
        final Menu submenu = new Menu(menu.getShell(), SWT.DROP_DOWN);
        openItem.setMenu(submenu);
        
        //Add all user created items to the CxProlog menu
       try{
        Iterator<List<String>> it = items_info.iterator();

        System.out.println("Checking Menus");
        if(items_info.size()!=0)
        	while(it.hasNext()){
        		List<String> menuInfo = it.next();
        		String menuName= menuInfo.get(0);
        		MenuItem nItem = new MenuItem(submenu, SWT.PUSH);
                nItem.setText("&"+menuName);
                final String cmd = menuInfo.get(1);
                
                nItem.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        //what to do when menu is subsequently selected.
                        Prolog.CallProlog(cmd);
                    }
                });
        	}
        else
        	System.out.println("NO MENUS");
       }catch(Exception e){
    	   e.printStackTrace();
       }
        openItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                //what to do when menu is subsequently selected.
                System.err.println("Dynamic menu selected");
            }
        });
    }	
	
	/**
	 * Adds a new item to the context menu of the CxProlog Editor
	 * @param menuName - The name of the new menu item
	 * @param cmd - The command to be executed by the new menu item
	 */
	
	public static void add_item_editor(String menuName, String cmd){
		try{
			System.out.println("HERRRRRRRRRRRRRRRRRRRRRRRRRRR");
			ArrayList<String> newItem=new ArrayList<String>();
			newItem.add(menuName);
			newItem.add(cmd);
			items_info.add(newItem);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	class About1 extends Action {
        public About1() {
            super("Zero item", AS_PUSH_BUTTON);
        }
        public void run() {
        	
        	Prolog.CallProlog("writeln('Need to define an action')");
        	 
        }
    }

}
