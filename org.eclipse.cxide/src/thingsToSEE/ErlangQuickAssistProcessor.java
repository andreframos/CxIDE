package thingsToSEE;

import java.util.ArrayList;

import org.eclipse.cxide.utilities.Editor_Utilities;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ErlangQuickAssistProcessor implements IQuickAssistProcessor {

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return "quick fix ERROR";
	}

	@Override
	public boolean canFix(Annotation annotation) {
		// TODO Auto-generated method stub
		System.out.println("TRYING SOMETHING");
		return true;
	}

	@Override
	public boolean canAssist(IQuickAssistInvocationContext invocationContext) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ICompletionProposal[] computeQuickAssistProposals(
			IQuickAssistInvocationContext invocationContext) {
		System.out.println("NESQUIIIIICKKKKKKKKKKKKKKKKKKKKKKK");
	
		return null;
	}

}
