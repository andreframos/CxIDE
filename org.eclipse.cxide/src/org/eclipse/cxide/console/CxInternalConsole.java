package org.eclipse.cxide.console;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.cxide.Activator;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;

import prolog.Prolog;



/**
 * A classe que representa a consola dinâmica do CxProlog
 * isto é a consola que trabalha sobre a biblioteca dinâmica
 * e fornece o feedback das extensões realizadas através do
 * CxProlog ao IDE
 * @author andreramos
 *
 */
public class CxInternalConsole extends IOConsole{
	private static final String consoleName = "CxProlog Internal Console";
	private static BufferedWriter writeToConsole;
	private static boolean terminated = false;
	private static boolean need_main=true;
	static Thread ReadUserWriteConsole;
	static BufferedReader readUserInput;
	static InputStream is;
	static InputStream is1;
	static OutputStream os;
	static Robot robot=null;
	/**
	 * A thread that reads the output of CxProlog and writes
	 * it in the Dynamic CxProlog Console.
	 * 
	 * Prolog.coroutiningRunABit("mythread") gives some execution
	 * time to the cxProlog thread also this method returns
	 * true - if there is something to read in the buffer
	 * false - otherwise
	 * 
	 * So if it returns true we read what is in the buffer
	 *  with Prolog.coroutiningOutputText("mythread"), write it to te CxInternalConsole
	 *  and execute
	 *  again the ReadCxPrologWriteConsole thread
	 *  Activator.getDefault().getWorkbench().getDisplay().asyncExec(new ReadCxPrologWriteConsole(buff_writeConsole));
	 *  to read the remaining info in the CxBuffer
	 *  
	 *  Caso falso tentamos ler apenas uma linha do buffer.
	 * 
	 * @author andreramos
	 *
	 */
	private static class ReadCxPrologWriteConsole implements Runnable {
	
		//Buffer para escrever para a consola do CxProlog
		BufferedWriter buff_writeConsole;
		
		public ReadCxPrologWriteConsole(BufferedWriter buff_w){
			this.buff_writeConsole=buff_w;
		}
	    
		public void run() {
	    	System.out.println("TRYING TO READ FROM CXPROLOGfff");
			//Deixar a thread do cxProlog correr um pouco
	    	if(Prolog.coroutiningRunABit("cxThread")){
	    		//Ler uma linha do buffer
	    		String output = Prolog.coroutiningOutputText("cxThread");
	    		try {
	    			System.out.println("OUTPUT1: "+output);
	    			//escrever para a consola
					buff_writeConsole.write(output);
					buff_writeConsole.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	
	    		//Executar novamente esta thread para ler o restante input do CxProlog
	    		Activator.getDefault().getWorkbench().getDisplay().asyncExec(new ReadCxPrologWriteConsole(buff_writeConsole));
	    	
	    	}else{
	    		//Ler linha do buffer
	    		String output = Prolog.coroutiningOutputText("cxThread");
	    		try {
	    			System.out.println("OUTPUT2: "+output);
	    			//Escrever para a consola
					buff_writeConsole.write(output);
					buff_writeConsole.flush();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		
	    	;
			}
	    }
	}
	
	//DIALOG X:
	//CMD EXECUTED:
	//OUTPUT:
	
	private static class ReadCxDialogWriteConsole implements Runnable {
		
		//Buffer para escrever para a consola do CxProlog
		BufferedWriter buff_writeConsole;
		String dialog_name;
		String dialog_cmd;
		
		public ReadCxDialogWriteConsole(String dialog_name, String dialog_cmd, BufferedWriter buff_w){
			System.out.println("TRYING TO READ CX DIALOG");
			this.buff_writeConsole=buff_w;
			this.dialog_name=dialog_name;
			this.dialog_cmd=dialog_cmd;
		}
	    
		public void run() {
			try {
				buff_writeConsole.write("\nDialog: "+dialog_name+"\n");
				buff_writeConsole.write("Goal executed: "+dialog_cmd+"\n\n");
				buff_writeConsole.write("Output:\n");
				buff_writeConsole.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	System.out.println("TRYING TO READ FROM CXPROLOG");
			//Deixar a thread do cxProlog correr um pouco
	    	if(Prolog.coroutiningRunABit("cxThread")){
	    		//Ler uma linha do buffer
	    		String output = Prolog.coroutiningOutputText("cxThread");
	    		try {
	    			System.out.println("Cx Output: "+output);
	    			if(output.contains("main")){
	    				need_main=false;
	    			}
	    			//escrever para a consola
					buff_writeConsole.write(output);
					buff_writeConsole.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	
	    		//Executar novamente esta thread para ler o restante input do CxProlog
	    		//Activator.getDefault().getWorkbench().getDisplay().asyncExec(new ReadCxDialogWriteConsole(dialog_name, dialog_cmd, buff_writeConsole));
	    	
	    	}else{
	    		//Ler linha do buffer
	    		String output = Prolog.coroutiningOutputText("cxThread");
	    		try {
	    			System.out.println("Cx Output2: "+output);
	    			if(output.contains("main")){
	    				System.out.println("CX NO MAIN");
	    				need_main=false;
	    			}
	    			//Escrever para a consola
					buff_writeConsole.write(output);
					buff_writeConsole.flush();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		
	    	;
	    	}
	    	
	    }
	}

	
private static class ReadCxMenuWriteConsole implements Runnable {
		
		//Buffer para escrever para a consola do CxProlog
		BufferedWriter buff_writeConsole;
		String menu_name;
		String item_name;
		String cmd;
		
		public ReadCxMenuWriteConsole(String menu_name, String item_name, String cmd, BufferedWriter buff_w){
			this.buff_writeConsole=buff_w;
			this.menu_name=menu_name;
			this.item_name=item_name;
			this.cmd=cmd;
		}
	    
		public void run() {
			try{
				buff_writeConsole.write("\nMenu: "+menu_name+"\n");
				buff_writeConsole.write("\nItem: "+item_name+"\n");
				buff_writeConsole.write("Goal executed: "+cmd+"\n\n");
				buff_writeConsole.write("Output:\n");
		
			
	    	System.out.println("TRYING TO READ CX MENU FROM CXPROLOG");
			//Deixar a thread do cxProlog correr um pouco
	    	System.out.println("RUn a bit    "+Prolog.coroutiningRunABit("cxThread"));
	    	if(Prolog.coroutiningRunABit("cxThread")){
	    		//Ler uma linha do buffer
	    		String output = Prolog.coroutiningOutputText("cxThread");
	    			System.out.println("CxMenu Output: "+output);
	    			if(output.contains("main")){
	    				need_main=false;
	    			}
	    			//escrever para a consola
					buff_writeConsole.write(output);
					buff_writeConsole.flush();
	    	
	    		//Executar novamente esta thread para ler o restante input do CxProlog
	    		//Activator.getDefault().getWorkbench().getDisplay().asyncExec(new ReadCxDialogWriteConsole(dialog_name, dialog_cmd, buff_writeConsole));
	    	
	    	}else{
	    		//Ler linha do buffer
	    		String output = Prolog.coroutiningOutputText("cxThread");
	    			System.out.println("CxMenu Output2: "+output);
	    			if(output.contains("main")){
	    				System.out.println("CXMenu NO MAIN");
	    				need_main=false;
	    			}
	    			//Escrever para a consola
					buff_writeConsole.write(output);
					buff_writeConsole.flush();
					
	    	;
	    	}
				buff_writeConsole.flush();
	}catch(IOException e){
		e.printStackTrace();
	}
		}
}
	
	
	/**
	 * Thread que lê o input do utilizador e escreve para a Thread do CxProlog
	 * Cada vez que o utilizador insere uma nova linha na consola, 
	 * é executada a Thread ReadCxPrologWriteConsole para 
	 * que o CxProlog possa processar o input do utilizador e
	 * responder ao mesmo
	 * 
	 * @author andreramos
	 *
	 */
	private static class ReadUserWriteConsole implements Runnable {
	
		BufferedWriter writeToConsole;
		InputStreamReader readUserInput;
	
		public ReadUserWriteConsole(InputStream is, OutputStream os) {
			readUserInput= new InputStreamReader(is);
	    	writeToConsole = new BufferedWriter(new OutputStreamWriter(os));
	    	
		}
	    public void run() {
	    	try {
	    	System.out.println("TRYING TO READ FROM USER CONSOLE AND SENDING TO CXPROLOG");
	  
	    	
	    		//Para não bloquear e continuar sempre a tentar ler
	    		String user_input="";
	    		while(true){
					int c=0;
					if(terminated){
						System.out.println("Terminated "+ is.available());
						
						terminated=false;
					}
					if(readUserInput.ready()){
						System.out.println("READING");
							//Enviar a linha lida para a thread do CxProlog 
							c = readUserInput.read();
							user_input+=(char)c;
							
							if(c=='\n'){
								System.out.println("SENDING "+user_input);
								Prolog.coroutiningInput("cxThread", user_input);
								//Dar tempo de execução à thread CxProlog e ler a sua resposta
								
								Activator.getDefault().getWorkbench().getDisplay().asyncExec(new ReadCxPrologWriteConsole(writeToConsole));
								user_input="";
							}
							
						}else{
							//TODO: ORIGEM DE POSSIVEL ERRO DO SLEEP INTERRUPTED
							//Para não bloquear
							//System.out.println("Sleeping");
							Thread.sleep(10);
						}
				}
				
			} catch(InterruptedException e){
				System.out.println("OKOKOKOKO");
			}
	    	catch (EOFException e) {
				System.err
						.println("PROBLEM DURING CxInternalConsole.ReadUserWriteConsole EOFexception");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("PROBLEM DURING CxInternalConsole.ReadUserWriteConsole IOException");
				
				e.printStackTrace();

			}
	    }
	    
	    public void stop1() {
	    	System.out.println("Stopping");
	        Thread moribund = Thread.currentThread();
	        moribund.interrupt();
	    }
	}
			
	
	public CxInternalConsole(){
		super(consoleName, null);
		System.out.println("D-> Creating CxDynamic Console");
		
		//Criar consola e adicionar a mesma ao ambiente
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { this });
	
		//Lançar a Thread do CxProlog que ficara responsável
		//pela interação com a consola.
		System.out.println("A CRIAR THREAD");
		Prolog.CallProlog("cxThread :- '$top_level2'.");
		Prolog.CallProlog("thread_new(cxThread,cxThread,fail).");
		try {
			robot = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
				//String output = Prolog.coroutiningOutputText("cxThread");
	
		//Obter outputstream da consola dinamica
		OutputStream os = this.newOutputStream();
		writeToConsole = new BufferedWriter(new OutputStreamWriter(os));
		
		//Mudar a directoria default que a CxThread trabalhará.
		//A directoria será a que o utilizador está a usar no seu IDE.
		String runtimePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		Prolog.coroutiningInput("cxThread", "fs_cwd(_,'"+runtimePath+"').\n");
		
		//Ler algum lixo inicial
		Prolog.coroutiningRunABit("cxThread");
		
		// a resposta do fs_cwd
		String output = Prolog.coroutiningOutputText("cxThread");
	
		try {
			
			//escrever para a consola
			writeToConsole.write("[main] ?- ");
			writeToConsole.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//Lançar Thread que será responsável por ler o input do user
		//e enviar o mesmo para a Consola
		is = this.getInputStream();
		os=this.newOutputStream();
		//readUserInput = new BufferedReader(new InputStreamReader(is));
		
		ReadUserWriteConsole = 
		new Thread(new ReadUserWriteConsole (is,os), "writeUsCx");
		ReadUserWriteConsole.start();
		
		
	}
	

	
	//Método Criado porque era necessário que os métodos criados pelo utilizador
	// os chamados CxMenus chamassem o runAbit aquando duma interação
	// caso contrário o feedback só apareceria em outra oportunidade
	public static void runDynamicConsole(){
		System.out.println("D-> Runningggggg Dynamic Console...");
		try {
			//writeToConsole.write("\n");
			//writeToConsole.flush();
			Activator.getDefault().getWorkbench().getDisplay().syncExec(new ReadCxPrologWriteConsole(writeToConsole));
			Thread.sleep(10);
			//Mimicar o normal funcionamento do terminal
			if(need_main){
			writeToConsole.write("\n[main] ?- ");
			writeToConsole.flush();
			}else{
				need_main=true;
			}
		} catch (IOException | InterruptedException e) {
			System.err.println("Erro na CxDynamic Console");
			e.printStackTrace();
		}
		
	}
	
	public static void runCxDialog(String dialog_name, String dialog_cmd){
		System.out.println("D-> Running Dynamic Console...");
		try {
			//Activator.getDefault().getWorkbench().getDisplay().syncExec(new ReadCxDialogWriteConsole(dialog_name, dialog_cmd, writeToConsole));
			Activator.getDefault().getWorkbench().getDisplay().syncExec(new ReadCxPrologWriteConsole(writeToConsole));
			Thread.sleep(10);
			
		} catch ( InterruptedException e) {
			System.err.println("Erro na CxDynamic Console");
			e.printStackTrace();
		}
		
	}
	
	public static void runCxMenu(String menu_name, String item_name, String cmd){
		System.out.println("D-> Running Dynamic Console...");
		try {
			Activator.getDefault().getWorkbench().getDisplay().syncExec(new ReadCxMenuWriteConsole(menu_name, item_name, cmd, writeToConsole));
			Thread.sleep(10);
			
		} catch ( InterruptedException e) {
			System.err.println("Erro na CxDynamic Console");
			e.printStackTrace();
		}
		
	}
	
	
	
	public static void test(){
		System.out.println("TESTING1111");
		//terminated=true;
		robot.keyPress(10);
		robot.keyRelease(10);
	}
	
	public static void runABit(){
		Activator.getDefault().getWorkbench().getDisplay().asyncExec(new ReadCxPrologWriteConsole(writeToConsole));
	}
	
	public static void injectCode(String code){
			
			try {
				writeToConsole.write(code+"\r\n");
				writeToConsole.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Prolog.coroutiningInput("cxThread", code+"\r\n");
			Activator.getDefault().getWorkbench().getDisplay().asyncExec(new ReadCxPrologWriteConsole(writeToConsole));
		
		
	}
}
