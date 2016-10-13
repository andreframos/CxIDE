/**
 * @author Andre Ramos, 2014
 */
package org.eclipse.cxide.CxEditor;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.cxide.Menu_ops.FileChooser_Operations;
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
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.FileStoreEditorInput;
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
	
	private static String filePrefix = new SimpleDateFormat( "yyyyMMF_" ).format( new GregorianCalendar().getTime()); 
	
	public IFile getFileInput() {

		IFile  file = null;
			try {
				// Case where the file is in the workspace
				if( getEditorInput() instanceof IFileEditorInput )
					file = ((IFileEditorInput) getEditorInput()).getFile();

				// Outside the workspace
				// Since everything relies on IFiles, we create a temporary file in the workspace
				// linking to the file outside the workspace
				else if( getEditorInput() instanceof FileStoreEditorInput ) {
					System.out.println("CREATING NEW FILE");
					File f = new File(((FileStoreEditorInput) getEditorInput()).getURI());
					IPath path = new Path( f.getAbsolutePath());

					// Create a temporary project
					IProgressMonitor monitor =  new NullProgressMonitor();
					IProject tempProject = ResourcesPlugin.getWorkspace().getRoot().getProject( ".tempo" );
					if( ! tempProject.exists())
						tempProject.create( monitor );

					if( ! tempProject.isOpen())
						tempProject.open( monitor );

					// Remove old temporary files
					// The file prefix is designed so that natural comparison order is enough
					for( IResource res : tempProject.members()) {
						System.out.println("PROJECTS:" +res.getName());
						if( res.getName().substring( 0, 7 ).compareTo( filePrefix ) < 0 ) {
							try {
								res.delete( false, monitor );
							} catch( Exception e ) {
								e.printStackTrace();
							}
						}
					}

					// Create a temporary file
					file = tempProject.getFile( filePrefix + path.lastSegment());
					file.createLink( path, IResource.NONE, monitor );
					
					FileChooser_Operations.open_external_file_onEditor(path.toString());
				}
				
				//IFileEditorInput adapter=(IFileEditorInput) input.getAdapter(IFileEditorInput.class);
				//adapter.
			} catch( CoreException e ) {
				e.printStackTrace();
			}
		

		return file;
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	
	//ADICIONADO PARA ABRIR FICHEIROS EXTERNOS NO EDITOR
		private IDocumentProvider createDocumentProvider(IEditorInput input) {
			System.out.println("Opening File");
			try{
			if(input instanceof IFileEditorInput){
				System.out.println("1");
				//TextDocumentProvider
				return new FileDocumentProvider();
			}else {
				System.out.println("3");
				
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
