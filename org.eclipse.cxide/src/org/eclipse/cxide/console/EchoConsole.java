package org.eclipse.cxide.console;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;

public class EchoConsole{

	public EchoConsole(){
		//Criar a nova consola
		IOConsole console = new IOConsole("Nova Consola", null);
		IConsole consoles[] = new IConsole[1];
		consoles[0]=console;
		//Adicionar a nova consola ao ambiente
		ConsolePlugin.getDefault().getConsoleManager().addConsoles( consoles );		
	}
	
	

}
