
package org.eclipse.cxide.CxEditor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;


//ADICIONADO PARA ABRIR FICHEIROS EXTERNOS NO EDITOR
public class TextDocumentProvider extends TextFileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.getDocument(element);
		
		return document;
	}
	
	
    /////////////////////////////////////////////////////////////////7
	
	protected FileInfo createFileInfo(Object element) throws CoreException {
    	FileInfo info = super.createFileInfo(element);
    	if(info==null){
    		info = createEmptyFileInfo();
    	}
    	IDocument document = info.fTextFileBuffer.getDocument();
    	if (document != null) {
    			System.out.println("Here");
    			
    		/* register your partitioner and other things here 
                       same way as in your fisrt document provider */
    	}
    	return info;
    }
}