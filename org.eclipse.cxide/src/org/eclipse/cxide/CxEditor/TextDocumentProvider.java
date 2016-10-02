
package org.eclipse.cxide.CxEditor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;


//ADICIONADO PARA ABRIR FICHEIROS EXTERNOS NO EDITOR
public class TextDocumentProvider extends TextFileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.getDocument(element);
		
		return document;
	}
	
	protected FileInfo createFileInfo(Object element) throws CoreException {
    	FileInfo info = super.createFileInfo(element);
    	if(info==null){
    		info = createEmptyFileInfo();
    	}
    	IDocument document = info.fTextFileBuffer.getDocument();
    	if (document != null) {

    		/* register your partitioner and other things here 
                       same way as in your fisrt document provider */
    	}
    	return info;
    }
}