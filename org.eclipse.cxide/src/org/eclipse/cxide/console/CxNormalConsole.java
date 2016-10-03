package org.eclipse.cxide.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.cxide.Activator;
import org.eclipse.cxide.preferences.PreferenceConstants;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;


/**
 * Classe que representa a consola normal do CxProlog
 * A consola normal é a execução de uma instalação local do CxProlog
 * Assim é fácil ao utilizador mudar a versão utilizada do CxProlog.
 * 
 * É utilizado o Project builder para lançar um novo processo do CxProlog
 * definido nas preferências.
 * 
 * A consola permitira ao utilizador interagir com o processo do CxProlog
 * como se estivesse no terminal
 * @author andreramos
 *
 */
public class CxNormalConsole extends IOConsole {
	
    private static final String consoleName = "CxProlog External Console";
	CxNormalConsole consol;
	IOConsoleOutputStream outConsoleStream;
	IOConsoleInputStream inConsoleStream;
	
	//Utilizado para lançar o processo CxProlog
	ProcessBuilder processBuilder;
	
	//Os Runnables responsáveis por ler o input do CxProlog ou do user e 
	//escrever o mesmo para a consola
	ReadCxWriteConsole readCx;
	ReadUserWriteCx readConsoleWriteCx;
	
	//Threads para executar os runnables acima 
	Thread readCx_thread;
    Thread readConsoleWriteCx_thread;
    
    static BufferedWriter writeCx;
    static BufferedWriter writeConsole;

    public CxNormalConsole() {
        super(consoleName, null);
        System.out.println("D-> CxNormalConsole Starting...");
        
        //Adicionar a consola ao ambiente
        consol = this;
        ConsolePlugin.getDefault().getConsoleManager().addConsoles( new IConsole[]{this} );		
		
        outConsoleStream = consol.newOutputStream();
        inConsoleStream = consol.getInputStream();
	writeConsole = new BufferedWriter( new OutputStreamWriter(outConsoleStream));
        //Onde o verdadeiro trabalho é efectuado
		this.createCxProcess();

    }

	
	
	/**
	 * Thread que lê o input do utilizador e envia-o para
	 * o CxProlog
	 * @author andreramos
	 *
	 */
	private static class ReadUserWriteCx implements Runnable {
		BufferedWriter w;
		String command;
		BufferedReader b;
		PrintWriter outConsole;
		private volatile boolean running = true;
		
		 public void terminate() {
		        running = false;
		        System.out.println(running);
		    }
		
		ReadUserWriteCx(OutputStream cxOutputStream, InputStream inputConsole, PrintWriter outConsole ) {
			this.w =  new BufferedWriter(new OutputStreamWriter(cxOutputStream));
			this.b=   new BufferedReader(new InputStreamReader(inputConsole));
			command = "";
			this.outConsole=outConsole;
		}
	
		public void run() {
			try {
				while(running && Thread.interrupted()!= true){
					outConsole.write("\n[main] ?- ");
					outConsole.flush();
					if((command = b.readLine()) != null) {
	                     
						command += "\n";
						
						w.write(command);
						w.flush();
					}
					try{Thread.sleep(10);} catch(Exception ee) {
						ee.printStackTrace();
					}
				}
			} catch (EOFException e) {
				System.err
						.println("PROBLEM DURING theWrite.run() EOFexception");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("PROBLEM DURING theWrite.run() IOException");
				e.printStackTrace();

			}
		}
	}
	
	/**
	 * Thread que lê o feedback do CxProlog.
	 * Esta thread corre indeterminadamente e sempre que
	 * existe algo para ler vindo do CxProlog o mesmo é lido
	 * e enviado para a consola normal do CxProlog
	 * @author andreramos
	 *
	 */
	private static class ReadCxWriteConsole implements Runnable {
		//buff para ler do cxprolog
		BufferedReader cxBuff;
		//para escrever para a consola
		private PrintWriter outConsole;
		
		//tentativa de terminar as threads de forma segura
		private volatile boolean running = true;
		
		 public void terminate() {
		        running = false;
		        System.out.println(running);
		    }
		
		
		ReadCxWriteConsole(InputStream cxInputStream, OutputStream outConsoleStream) {
			System.out.println("D -> Starting ReadCxFeedBack...");
			cxBuff= new BufferedReader(new InputStreamReader(cxInputStream));
			outConsole = new PrintWriter(new OutputStreamWriter(outConsoleStream));
		}

		public void run() {
			int c = 0;
			try {

				while (running && Thread.interrupted() != true) {
					//Sempre que existir info do CxProlog para ler
					//as mesmas são lidas até encontrar o char '-'
					//isto foi utilizado pois é suposto parar de ler após o prompt
					// ora o prompt é [main] ?- e por isso o ciclo até - 
					//Obviamente isto deverá ser melhorado no futuro 
					// pois é fácil buggar.
					if(cxBuff.ready()){
						do{
							c=cxBuff.read();
							outConsole.print((char) c);
							outConsole.flush();
						}while((char) c!='-');	
					}
					try{Thread.sleep(10);} catch(Exception ee) {
						ee.printStackTrace();
					}
				}

			} catch (EOFException e) {
				System.err
						.println("Erro no CxNormalConsole.ReadCxFeedback.run()");
				e.printStackTrace();
			} catch (IOException e) {
				System.err
						.println("Erro no CxNormalConsole.ReadCxFeedback.run()");
				e.printStackTrace();
			}
		}
	}
	public void restart(){
		System.out.println("HUGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
		IConsole[] con = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
		for(int i=0; i<con.length; i++){
			if(con[i].getName().equals("CxProlog Console")){
			((IOConsole)con[i]).clearConsole();
			this.readCx.terminate();
			this.readConsoleWriteCx.terminate();
		
			
			try {
				this.readConsoleWriteCx_thread.stop();
				this.readCx_thread.stop();
			
				this.createCxProcess();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
		}
		
	}
	
	
	
	
	
	public void createCxProcess(){
		try {
			System.out.println("D-> Creating CxProcess and starting Threads...");
			//Preparar para criar o processo CxProlog
			processBuilder = new ProcessBuilder("/bin/bash");
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			
			InputStream is = process.getInputStream();
			OutputStream os = process.getOutputStream();
			
			writeCx=new BufferedWriter(new OutputStreamWriter(os));
			
			Writer w = new OutputStreamWriter(process.getOutputStream());
			PrintWriter outConsole = new PrintWriter(new OutputStreamWriter(outConsoleStream));
			BufferedReader b= new BufferedReader(new InputStreamReader(is));
			
			String runtimePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
			
			//Criar o processo CxProlog apartir da instalação local definida nas Prederências
			String local_instalation = Activator.getDefault().getPreferenceStore().getDefaultString(PreferenceConstants.CX_PATH)+"\n";
			System.out.println("OK: "+local_instalation);
			w.write(local_instalation);
			w.flush();
			Thread.sleep(10);
			System.out.println("okoko");
			//Imprimir a versão do CxProlog para que a consola tenha um inicio igual
			// á normal execução do CxProlog num terminal
			String version = b.readLine();
			outConsole.print(version);
			outConsole.flush();
			
			//Mudar a directoria corrente para a directoria onde o utilizador trabalha
			w.write("fs_cwd(_,'"+runtimePath+"').\n");
			w.flush();
			
			int c=0;
			Thread.sleep(10);
			//Ler algum lixo como por exemplo o output da mudança de directoria
			System.out.println("READ LINEBREAK:"+ b.readLine());
			//System.out.println("READ PROMPT AND YES:"+ b.readLine());
			//System.out.println("READ LINEBREAK:"+b.readLine());
			
			
			
			//Lançar Thread para ler do CxProlog e escrever para a consola
			readCx = new ReadCxWriteConsole(is,outConsoleStream);
	        readCx_thread = new Thread(readCx);
			readCx_thread.start();
		
			//Lançar Thread para ler input do user enviá-lo para o CxProlog
		    readConsoleWriteCx = new ReadUserWriteCx(os,inConsoleStream,outConsole);
			readConsoleWriteCx_thread = new Thread(readConsoleWriteCx);
			readConsoleWriteCx_thread.start();
		
	
		} catch (Exception e) {
			System.err.println("Erro CxNormalConsole.createCxProcess...");
			e.printStackTrace();
		}
	}
	
	public static void injectCode(String code){
		try {
			writeConsole.write(code);
			writeConsole.flush();
			writeCx.write(code);
			writeCx.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Injected");
	}
	
	
}