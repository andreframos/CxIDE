package org.eclipse.cxide.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
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

import prolog.Prolog;


/**
 * A classe que representa a consola dinâmica do CxProlog
 * isto é a consola que trabalha sobre a biblioteca dinâmica
 * e fornece o feedback das extensões realizadas através do
 * CxProlog ao IDE
 * @author andreramos
 *
 */
public class CxDynamicConsole extends IOConsole{
	private static final String consoleName = "CxProlog Internal Console";
	private static BufferedWriter writeToConsole;
	
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
	 *  with Prolog.coroutiningOutputText("mythread"), write it to te CxDynamicConsole
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
	    	System.out.println("TRYING TO READ FROM CXPROLOG");
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
		BufferedReader readUserInput;
		BufferedWriter writeToConsole;
		
		public ReadUserWriteConsole(InputStream is, OutputStream os) {
			readUserInput = new BufferedReader(new InputStreamReader(is));
	    	writeToConsole = new BufferedWriter(new OutputStreamWriter(os));
		}
	    public void run() {
	    	System.out.println("TRYING TO READ FROM USER CONSOLE AND SENDING TO CXPROLOG");
	    	String user_input;
	    	try {
	    		//Para não bloquear e continuar sempre a tentar ler
				while(Thread.interrupted()!= true){
					
					if((user_input = readUserInput.readLine()) != null) {
	                    //Enviar a linha lida para a thread do CxProlog 
						Prolog.coroutiningInput("cxThread", user_input+"\n");
						//Dar tempo de execução à thread CxProlog e ler a sua resposta
						Activator.getDefault().getWorkbench().getDisplay().asyncExec(new ReadCxPrologWriteConsole(writeToConsole));
					}
					//TODO: ORIGEM DE POSSIVEL ERRO DO SLEEP INTERRUPTED
					//Para não bloquear
					try{Thread.sleep(10);} catch(Exception ee) {
						ee.printStackTrace();
					}
				}
			} catch (EOFException e) {
				System.err
						.println("PROBLEM DURING CxDynamicConsole.ReadUserWriteConsole EOFexception");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("PROBLEM DURING CxDynamicConsole.ReadUserWriteConsole IOException");
				e.printStackTrace();

			}
	    }
	}
			
	
	public CxDynamicConsole(){
		super(consoleName, null);
		System.out.println("D-> Creating CxDynamic Console");
		
		//Criar consola e adicionar a mesma ao ambiente
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { this });
	
		//Lançar a Thread do CxProlog que ficara responsável
		//pela interação com a consola.
		System.out.println("A CRIAR THREAD");
		Prolog.CallProlog("cxThread :- '$top_level2'.");
		Prolog.CallProlog("thread_new(cxThread,cxThread,fail).");
		
		
		
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
		Thread ReadUserWriteConsole = 
		new Thread(new ReadUserWriteConsole (this.getInputStream(),this.newOutputStream()), "writeUsCx");
		ReadUserWriteConsole.start();
		
		
	}
	
	//Método Criado porque era necessário que os métodos criados pelo utilizador
	// os chamados CxMenus chamassem o runAbit aquando duma interação
	// caso contrário o feedback só apareceria em outra oportunidade
	public static void runDynamicConsole(){
		System.out.println("D-> Running Dynamic Console...");
		try {
			writeToConsole.write("\n");
			writeToConsole.flush();
			Activator.getDefault().getWorkbench().getDisplay().syncExec(new ReadCxPrologWriteConsole(writeToConsole));
			Thread.sleep(10);
			//Mimicar o normal funcionamento do terminal
			writeToConsole.write("\n[main] ?- ");
			writeToConsole.flush();
		} catch (IOException | InterruptedException e) {
			System.err.println("Erro na CxDynamic Console");
			e.printStackTrace();
		}
		
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
