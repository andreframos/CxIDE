	package org.eclipse.cxide.Menu_ops;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.cxide.Activator;
import org.eclipse.cxide.CxEditor.CxEditor;
import org.eclipse.cxide.console.CxDynamicConsole;
import org.eclipse.cxide.console.CxNormalConsole;
import org.eclipse.cxide.views.OutlineView;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.MarkerAnnotation;

import prolog.Prolog;

/**
 * Classe que disponibiliza operações dinâmicas
 * sobre o CxEditor
 * @author andreramos
 *
 */
public class Editor_Operations {
	
	static ArrayList<Object> filePred = new ArrayList<Object>();
	static ArrayList<OutlineElement> fileOutlineInfo = new ArrayList<OutlineElement>();
	static String[] auxArgs;
	/**
	 * Obter o deslocamento de uma determinada linha do atual
	 * ficheiro em edição
	 * @param O nº da linha do ficheiro
	 * @return O deslocamento dessa linha no ficheiro
	 */
	public static int getLineOffset(int line){
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IEditorPart editorPart = Activator.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();

				if (editorPart instanceof AbstractTextEditor) {
	try {
		return ((AbstractTextEditor) editorPart).getDocumentProvider().getDocument(editorPart.getEditorInput()).getLineOffset(line);
	} catch (BadLocationException e) {
		e.printStackTrace();
	}
				}
				return 0;
	}
	
	/**
	 * Realça o intervalo especificado no CxEditor
	 * @param start - O inicio do intervalo
	 * @param offset - O Comprimento do intervalo
	 */
	
	public static void SelectReveal(int start, int offset)
	{
		try {
			IEditorPart editorPart = Activator.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();

				if (editorPart instanceof AbstractTextEditor) {
					((AbstractTextEditor) editorPart).selectAndReveal(start, offset);
					}
				
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
	
	/**
	 * Obter o texto atualmente selecionado, poderá ser importante
	 * Por exemplo: para injectar código na consola do CxIDE
	 * @return
	 */
	public static String getSelectedText(){
		String selText = "";
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IEditorPart editorPart = Activator.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();

				if (editorPart instanceof AbstractTextEditor) {
//TODO String selectedText = null;
					
					IEditorSite iEditorSite = editorPart.getEditorSite();
					if (iEditorSite != null) {
						//get selection provider
						ISelectionProvider selectionProvider = iEditorSite
								.getSelectionProvider();
						if (selectionProvider != null) {
							ISelection iSelection = selectionProvider
									.getSelection();
							//offset
							selText =((ITextSelection) iSelection).getText();
						
						}
					}
			}
				
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selText;
	}
	
	/**
	 * Obter todo o texto do atual ficheiro em edição
	 * 
	 * @return O texto do atual ficheiro em edição
	 */
	public static String getCurrentEditorContent() {
	    final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
	        .getActiveEditor();
	    if (!(editor instanceof ITextEditor)) return null;
	    ITextEditor ite = (ITextEditor)editor;
	    IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
	    return doc.get();
	}
	
	/**
	 * Obtém o caminho do atual ficheiro em edição
	 * Pode ser utilizado para consultar o ficheiro
	 * @return
	 */
	public static String getCurrentEditorFilePath(){
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = 
		        workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = 
		         window.getActivePage();

		IEditorPart editor = 
		         activePage.getActiveEditor();
		

		
		IEditorInput input = 
		       editor.getEditorInput();


		
		IPath path = input instanceof FileEditorInput 
		        ? ((FileEditorInput)input).getPath()
		        : null;
		if (path != null)
		{
			return path.toString();
	}
		return "";
	}
	
	
	/**
	 * Obtem o caminho de todos os ficheiros atualmente abertos
	 * @return
	 */
	public static String[] getCurrentEditorAllFilesPath(){
		IEditorReference[] ref = PlatformUI.getWorkbench()
			    .getActiveWorkbenchWindow().getActivePage()
			    .getEditorReferences();
		
		String[] filesPath = new String[ref.length];

		for (int i=0; i<ref.length; i++){
			try {
				FileEditorInput file = ((FileEditorInput)ref[i].getEditorInput());
				IPath path = file.getPath();
				filesPath[i]=path.toString();
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return filesPath;
	}
	
	
	
	    public static void errorsHighlight(Object[] errors){
	    	
	    }
	
	
	public static void addProblemMarker(int charStart, int offset, String msg){
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		IEditorInput input = editor.getEditorInput();
		IPath path = ((FileEditorInput)input).getPath();
		String workspace=	ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		IPath relativePath=path.makeRelativeTo(new Path(workspace));
		String relPath=relativePath.toString();
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(relativePath);
		IResource r = file;
		
		try {
			 IMarker problemMarker = r.createMarker(IMarker.PROBLEM);
			// IMarker cxMarker = r.createMarker("com.simple.plugin.CxMarkers");
			// MarkerAnnotation annotation;
			// annotation = new MarkerAnnotation("com.simple.plugin.CxPrologAnnotationType",cxMarker);
			// annotation.update();
			 
			 if (problemMarker.exists()) {
			 //line = getLineOffset(line-1);
				
		            problemMarker.setAttribute(IMarker.MESSAGE, msg);
		           // cxMarker.setAttribute(IMarker.MESSAGE, "CxMarker "+msg);
		         //   m.setAttribute(IMarker.LINE_NUMBER, line);
		           // problemMarker.setAttribute(IMarker.CHAR_START, line+charStart);
		           // cxMarker.setAttribute(IMarker.CHAR_START, line+charStart);
		            problemMarker.setAttribute(IMarker.CHAR_START, charStart);
			      //  cxMarker.setAttribute(IMarker.CHAR_START, charStart);
		           // problemMarker.setAttribute(IMarker.CHAR_END, line+charStart+offset);
		           // cxMarker.setAttribute(IMarker.CHAR_END, line+charStart+offset);
		            
			        problemMarker.setAttribute(IMarker.CHAR_END, offset);
			       // cxMarker.setAttribute(IMarker.CHAR_END, offset);
			        
		            problemMarker.setAttribute(IMarker.LOCATION, relPath);
		            //cxMarker.setAttribute(IMarker.LOCATION, relPath);
		            
		            problemMarker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
		           // cxMarker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
		            
		            problemMarker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		            //cxMarker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			 }
			 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return relativePath;
	}
	
	public static void delProblemMarkers(){
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		IEditorInput input = editor.getEditorInput();
		
		IResource resource = (IResource) input.getAdapter(IResource.class);
		  IMarker[] problems = null;
		   int depth = IResource.DEPTH_INFINITE;
		  
		      try {
				problems = resource.findMarkers(IMarker.PROBLEM, true, depth);
			
				for(int i=0; i<problems.length; i++){
					System.out.println(problems[i].getAttribute(IMarker.MESSAGE));
					problems[i].delete();
				}
		      } catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	public static void updateFolding(Object[] predInfo){
		ArrayList<Position> predPositions = new ArrayList<Position>();
		int i=0;
		while(i<predInfo.length){
			int beggining_offset=(int) predInfo[i];
			int line=(int) predInfo[++i];
			int line_offset=(int) predInfo[++i];
			int ending_offset=(int) predInfo[++i];
			int end_line=(int) predInfo[++i];
			int unknown = (int) predInfo[++i];
			predPositions.add(new Position(beggining_offset-1, ending_offset-beggining_offset-1));
		}
	}
	
			
	public static void errors_Folding_Outline(Object[] predInfo, Object[] errorInfo){
		System.out.println("ERRORS: "+Arrays.toString(errorInfo));
		//Estrutura de dados com os predicados no atual ficheiro em edição
		//para cada predicado são tambem armazenadas as informações sobre
		// o seu offset de inicio no ficheiro e o range do mesmo.
		//O filePred é utilizado parao Outline.
		filePred.clear();
		//Esta estrutura de dados tal como o filePred é utilizada para o Outline
		//No entanto, esta estrutura só é utilizada numa parte mais avançada
		//do mesmo. O fileOutlineInfo terá as informações de todos os
		// funtores existentes no ficheiro, a sua aridade, offset inicio e range
		fileOutlineInfo.clear();
		//Apagar todos os marcadores de erros do ficheiro atual em edição
		delProblemMarkers();
		//Offset de inicio no ficheiro dum predicado
		int beggining_offset = 0;
		//linha de inicio dum predicado
		int line;
		//Offset na linha dum predicadp
		int line_offset;
		//Offset de fim no ficheiro dum predicado
		int ending_offset = 0;
		//Linha final no ficheiro dum predicado
		int end_line;
		//Info desconhecida sobre o predicado
		int unknown; 
		//Informação sobre o tipo de erro que aconteceu
		String error_info;
		//Estrutura utilizada para o folding no editor
		//Tem as posições de todos os predicados do ficheiro atual em edição
		
		ArrayList<Position> fPositions = new ArrayList<Position>();
		int i=0;
		//Percorrer o vetor com as informações dos erros existentes
		// e adicionar os erros como marcadores de problemas ao editor
		while(i<errorInfo.length){
			beggining_offset=(int) errorInfo[i];
			line=(int) errorInfo[++i];
			line_offset=(int) errorInfo[++i];
			ending_offset=(int) errorInfo[++i];
			end_line=(int) errorInfo[++i];
			unknown= (int) errorInfo[++i];
		    error_info = (String) errorInfo[++i];
		    addProblemMarker(beggining_offset-1, ending_offset, error_info);
			i++;
		}
		i=0;
		//Todo o conteudo do atual ficheiro em edição
		//É necessário para conseguir obter as informações sobre os predicados
		String content = getCurrentEditorContent();
		
		//Processar informações sobre todos os predicados do ficheiro
		//Esta informações permitirão atualizar o folding do editor e a vista estruturada
		while(i<predInfo.length){
			beggining_offset=(int) predInfo[i];
			line=(int) predInfo[++i];
			line_offset=(int) predInfo[++i];
			ending_offset=(int) predInfo[++i];
			end_line=(int) predInfo[++i];
			unknown = (int) predInfo[++i];
			//Adicionar informações sobre localização do predicado para efeitos de folding
			fPositions.add(new Position(beggining_offset-1, ending_offset-beggining_offset-1));
			//Obter um predicado do ficheiro
			String pred = content.substring(beggining_offset-1, ending_offset-1);
			//Adicionar o predicado obtido anteriormente e a sua localizacao para a OutlineView.
			filePred.add("'"+pred+"'");
			filePred.add(beggining_offset-1);
			filePred.add(ending_offset-beggining_offset);
			System.out.println("--------------------------------------------");
			System.out.println("Predicate: "+pred);
			i++;
		}
		//Caso existam predicados no atual ficheiro em edição.
		if(fPositions.size()!=0){
		//Atualizar o folding do editor
		CxEditor.updateFoldingStructure(fPositions);
		System.out.println("Getting Outline Info: "+filePreds());
		int beg = beggining_offset-1;
		int range= ending_offset-beggining_offset-1;
		//Calcular a nova outline view.
		Prolog.CallProlog("getOutlineInfo("+filePreds()+")");
		System.out.println("-------------------------------------");
		}
		//Atualizar a nova outline view.
		OutlineView.refresh();
		
	}
	
	
	//Obter uma representacao textual de todos os predicados
	//Para depois fazer Prolog.CallProlog("getOutlineInfo("+filePreds()+")")
	public static String filePreds(){
		return Arrays.toString(filePred.toArray());
	}
	
	//Obter um iterador sobre as informações dos predicados
	//Usado no createContentOutline
	public static Iterator<OutlineElement> getFileFunctors(){
		return fileOutlineInfo.iterator();
	}
	
	// Adicionar informações sobre um predicado
	public static void addFileFunctor(String functor, int arity, int start, int offset){
		fileOutlineInfo.add(new OutlineElement(functor,arity,auxArgs,start,offset));
		System.out.println("Added "+functor + " " + arity + " " + start + " "+ offset);
	}
	//Adicionar informações sobre os argu,entos encontrados para o predicado
	// a ser atualmente processado
	public static void addFileArg(String args[]){
		System.out.println("ADDing arg: "+Arrays.toString(args));
		auxArgs=args;
		
	}
	
	public static void injectSelCodeConsole(){
		String selected = Editor_Operations.getSelectedText();
		String activeConsole = ConsolePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("org.eclipse.ui.console.ConsoleView").getTitle();
		String normal = "Console (CxProlog Console)";
		if(activeConsole.equals(normal))
			CxNormalConsole.injectCode(selected+"\r\n");
		else
			CxDynamicConsole.injectCode(selected+"\r\n");
	}
	
}
