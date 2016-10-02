package org.eclipse.cxide.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.cxide.Menu_ops.Editor_Operations;
import org.eclipse.cxide.console.CxDynamicConsole;


public class InjectCode_Handler extends AbstractHandler {

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		Editor_Operations.injectSelCodeConsole();
		return null;
	}

	

}
