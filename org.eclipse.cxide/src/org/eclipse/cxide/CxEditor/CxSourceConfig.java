/**
 * @author Andre Ramos, 2014
 */
package org.eclipse.cxide.CxEditor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.cxide.utilities.ColorManager;
import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.quickassist.IQuickAssistAssistant;
import org.eclipse.jface.text.quickassist.QuickAssistAssistant;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import thingsToSEE.ErlangQuickAssistProcessor;
/**
 * Nesta classe é adicionado o Scanner para o realce de syntax
 * É adicionado o content assist
 * É adicionado o annotation hoover
 * É adicionado o Quick Fix
 * @author andreramos
 *
 */
public class CxSourceConfig extends SourceViewerConfiguration {

	private ColorManager colorManager;
	CxScanner scanner;

	public CxSourceConfig(ColorManager colorManager) {
		
		this.colorManager = colorManager;
	}

	protected CxScanner getPLScanner() {
		if (scanner == null) {
			reinitScanner();
		}
		return scanner;
	}

	public void reinitScanner() {
		try {
			scanner = new CxScanner(colorManager);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		scanner.setDefaultReturnToken(new Token(new TextAttribute(colorManager
				.getColor(colorManager.getDefaultColor()))));
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getPLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	/**
	 * Instalar o content assist no CxEditor
	 */
	public IContentAssistant getContentAssistant(ISourceViewer sv) {
		ContentAssistant ca = new ContentAssistant();
		IContentAssistProcessor cxContentAssist = new CxPrologContentAssist();
		ca.setContentAssistProcessor(cxContentAssist, IDocument.DEFAULT_CONTENT_TYPE);
		
		ca.setEmptyMessage("No documentation available");
		ca.setInformationControlCreator(getInformationControlCreator(sv));
		ca.setProposalSelectorBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		ca.setProposalSelectorForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_BLACK));
		return ca;
	}
	
	//TODO: Fazer no futuro o annotationHoover
	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
	    return new DefaultAnnotationHover();
	}
	//TODO: Fazer no futuro o annotationHoover
	@Override
	  public ITextHover getTextHover(ISourceViewer sv, 
		       String contentType) {
	
		return new DefaultTextHover(sv);
		      }
	
	//TODO: Fazer no futuro o Quick Assistant
	@Override 
	public  IQuickAssistAssistant getQuickAssistAssistant(ISourceViewer sourceViewer){
		 IQuickAssistAssistant assistant = new QuickAssistAssistant();
		 assistant.setQuickAssistProcessor(new ErlangQuickAssistProcessor());
		 System.out.println("Setting quick fix assistant");
		 return assistant;
		  //  assistant
		           // .setInformationControlCreator(getQuickAssistAssistantInformationControlCreator());
	}
	
	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		return new MonoReconciler(new MyReconciler(), false);
	}

}
