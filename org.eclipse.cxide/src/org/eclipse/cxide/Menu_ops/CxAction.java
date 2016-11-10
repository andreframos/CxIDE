package org.eclipse.cxide.Menu_ops;

import org.eclipse.cxide.console.CxInternalConsole;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import prolog.Prolog;

public class CxAction extends Action {
	private String name;
	private String cmd;
	
	public CxAction(String name, String cmd) {
		super(name, AS_PUSH_BUTTON);
		this.name=name;
		this.cmd=cmd;
	}

	@Override
	public void run() {
    	
    	Prolog.CallProlog(cmd);
	}
	
	public String getName(){
		return name;
	}
	
	public String getCmd(){
		return cmd;
	}

}
