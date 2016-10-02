/**
 * @author Andre Ramos, 2014
 */
package org.eclipse.cxide.CxEditor;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.cxide.utilities.ColorManager;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * Classe que representa o Editor do CxProlog
 * @author andreramos
 *
 */
public class CxEditor  extends TextEditor{

	static ColorManager colorManager;
	static CxSourceConfig configuration;
	private static IEditorInput input;
	
	private static Annotation[] oldAnnotations;
	private static ProjectionAnnotationModel annotationModel;
	private static ISourceViewer viewer;
	private ProjectionSupport projectionSupport;

	public CxEditor() {
		super();
		try {
			System.out.println("STARTING CX_EDITOR...");
			colorManager = new ColorManager();
			//No CxSourceConfig é adicionada a realce de sintaxe
			//o Content assist a annotattion hoover e os quickfixes ao editor
			
			configuration = new CxSourceConfig(colorManager);
			setSourceViewerConfiguration(configuration);
			setEditorContextMenuId("CxEditor");
			
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
	

	public static IEditorInput getInput() {
		return input;
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	
	//ADICIONADO PARA ABRIR FICHEIROS EXTERNOS NO EDITOR
		private IDocumentProvider createDocumentProvider(IEditorInput input) {
			try{
			if(input instanceof IFileEditorInput){
				System.out.println("TExtDocumentoProvider");
				return new TextDocumentProvider();
			}else {
				return new TextDocumentProvider();
			}
			}catch(Exception e){
				System.err.println("Erro ao tentar abrir ficheiros no CxEditor.createDocumentProvider");
				e.printStackTrace();
			}
			return null;
		}
		
		//ADICIONADO PARA ABRIR FICHEIROS EXTERNOS NO EDITOR
		@Override
		protected final void doSetInput(IEditorInput input) throws CoreException {
			setDocumentProvider(createDocumentProvider(input));
			super.doSetInput(input);
		}
		
		//////////////////////////////////////////
		@Override
		public void createPartControl(Composite parent)
	    {
	        super.createPartControl(parent);
	        ProjectionViewer viewer =(ProjectionViewer)getSourceViewer();
	        
	        projectionSupport = new ProjectionSupport(viewer,getAnnotationAccess(),getSharedColors());
			projectionSupport.install();
			
			//turn projection mode on
			viewer.doOperation(ProjectionViewer.TOGGLE);
			
			annotationModel = viewer.getProjectionAnnotationModel();
			
	    }
		
		//TODO: Colapso e expação de testo
		public static void updateFoldingStructure(ArrayList<Position> positions)
		{
			Annotation[] annotations = new Annotation[positions.size()];
			
			//this will hold the new annotations along
			//with their corresponding positions
			HashMap<ProjectionAnnotation,Position> newAnnotations = new HashMap<ProjectionAnnotation,Position>();
			
			for(int i =0;i<positions.size();i++)
			{
				ProjectionAnnotation annotation = new ProjectionAnnotation();
				
				newAnnotations.put(annotation,positions.get(i));
				annotations[i]=annotation;
			}
			
			annotationModel.modifyAnnotations(oldAnnotations,newAnnotations,null);
			
			oldAnnotations=annotations;
		}
		
		@Override
		 protected ISourceViewer createSourceViewer(Composite parent,
		            IVerticalRuler ruler, int styles)
		    {
		        viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);

		    	// ensure decoration support has been created and configured.
		    	getSourceViewerDecorationSupport(viewer);
		    	
		    	return viewer;
		    }
		
		/*
		 * Usado para reiniciar o scanner do editor.
		 * É apenas utilizado aquando da mudança das prefências
		 */
		public static void reinitEditorScanner(){
			try{
				try{
				viewer.invalidateTextPresentation();
				configuration.reinitScanner();
				
				}catch(Exception e){
					e.printStackTrace();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		
		}


		
		

}
