package org.eclipse.cxide.utilities;

import org.eclipse.cxide.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

public class DefineCommands extends ExtensionContributionFactory {

	@Override
	public void createContributionItems(IServiceLocator serviceLocator,
			IContributionRoot additions) {
		// TODO Auto-generated method stub
		CommandContributionItemParameter p = new CommandContributionItemParameter(serviceLocator, "",
		        "org.eclipse.ui.file.exit",
		        SWT.PUSH);
		    p.label = "Exit the application";
		    p.icon = Activator.getImageDescriptor("icons/cx.gif");

		    CommandContributionItem item = new CommandContributionItem(p);
		    item.setVisible(true);
		    additions.addContributionItem(item, null);
	}

}
