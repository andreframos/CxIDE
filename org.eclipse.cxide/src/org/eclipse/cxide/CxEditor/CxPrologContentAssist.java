package org.eclipse.cxide.CxEditor;

/**
 * @author Andre Ramos, 2014
 */

import java.util.ArrayList;

import org.eclipse.cxide.utilities.Editor_Utilities;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationPresenter;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Classe que implementa o ContentAssist
 * Ou seja completação automática e informação sobre
 * os vários conteúdos que existem atualmente
 * 
 * Ter noção que o ContentAssist está baseado num xml (builts.xml)
 * @author andreramos
 *
 */
public class CxPrologContentAssist implements IContentAssistProcessor {

	//validador para saber se algo deve ser mostrado no contentAssist ou não
	protected IContextInformationValidator fValidator = new Validator();
	ExtractBuiltins xmlInfo = new ExtractBuiltins();
	
	//Necessário pois estava a dar Erro File not Found
	//Assim temos o absolute path do ficheiro
	private String getBuiltinsImgPath(){
		return System.getProperty("user.dir")+"/icons/contentAssist/builtin.png";
	}
	
	//Necessário pois estava a dar Erro File not Found
	//Assim temos o absolute path do ficheiro
	private String getUserDefinedImgPath(){
		return  System.getProperty("user.dir")+"/icons/contentAssist/userDef.png";
	}

	
	protected static class Validator implements IContextInformationValidator,
			IContextInformationPresenter {
		protected int fInstallOffset;

		/*
		 * @see IContextInformationValidator#isContextInformationValid(int)
		 */
		public boolean isContextInformationValid(int offset) {
			return Math.abs(fInstallOffset - offset) < 5;
		}

		/*
		 * @see IContextInformationValidator#install(IContextInformation,
		 * ITextViewer, int)
		 */
		public void install(IContextInformation info, ITextViewer viewer,
				int offset) {
			fInstallOffset = offset;
		}

		/*
		 * @see
		 * org.eclipse.jface.text.contentassist.IContextInformationPresenter
		 * #updatePresentation(int, TextPresentation)
		 */
		public boolean updatePresentation(int documentPosition,
				TextPresentation presentation) {
			return false;
		}
	}

    /**
     * Retorna uma lista de propostas de completação para a posição atual do cursor
     */
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		try {
			System.out.println("Computing Completion Proposals");
			
			//Para obter a palavra(prefixo) que se pretende completar
			int index = offset - 1;
			StringBuffer prefix = new StringBuffer();
			IDocument document = viewer.getDocument();
			while (index > 0) {
				try {
					char prev = document.getChar(index);
					if (Character.isWhitespace(prev)) {
						break;
					}
					prefix.insert(0, prev);
					index--;
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			
			//Lista de prosposta de completação
			ArrayList<CompletionProposal> proposals = new ArrayList<CompletionProposal>();
			
			//Vetor as informações de todos os elementos preds definidos no
			//documento xml builts
			PredInfo[] builtins = xmlInfo.getBuiltinsXML();
			
			//Vetor com os nomes de todos os predicaos definidos por o utilizador
			//TODO: Neste caso ainda não existe a descrição, a mesma poderá ser adicionada no futuro
			//O utilizador insere como comentário especial e a descrição e predicado são adicionados
			
			//Ter em conta que os predicados user defined são obtidos atraves da biblioteca dinâmica
			//enquanto que os builtins são obtidos através do xml.
			//Esta parte deverá ser normalizada no futuro para ambos os tipos de predicados serem
			//obtidos por a biblioteca dinâmica 
			String[] userDefined = Editor_Utilities.getUserDefined();
			
			//Caso exista um prefixo, vai ser percorridos todos os predicados
			//builtins e userdefined para averiguar se completam o prefixo
			//Para cada predicado que complete o prefixo
			if (prefix.length() > 0) {	
				String word = prefix.toString();
				for (int i = 0; i < builtins.length; i++) {
					String keyword = builtins[i].getPred();
					String desc = builtins[i].getDesc();
					if (keyword.startsWith(word)
							&& word.length() < keyword.length()) {

						Image img = new Image(Display.getDefault(),
								this.getBuiltinsImgPath());

						proposals
								.add(new CompletionProposal(keyword, index + 1,
										offset - (index + 1),
										keyword.length() + 1, img, keyword,
										null, desc));

					}
				}

				for (int i = 0; i < userDefined.length; i++) {
					String keyword = userDefined[i];
					if (keyword.startsWith(word)
							&& word.length() < keyword.length()) {

						Image img = new Image(Display.getDefault(),
								this.getUserDefinedImgPath());

						proposals.add(new CompletionProposal(keyword + " ",
								index + 1, offset - (index + 1), keyword
										.length() + 1, img, keyword + " ",
								null, "UserDefined predicate"));
					}
				}

				//Caso não exista um prefixo são adicionadas todas a propostas
				// de completação possíveis
				//TODO: Pode ser optimizado para não ter de gerar esta lista sempre
			} else {
				for (int i = 0; i < builtins.length; i++) {
					String keyword = builtins[i].getPred();
					String desc = builtins[i].getDesc();
					try {
						Image img = new Image(Display.getDefault(),
								this.getBuiltinsImgPath());

						proposals.add(new CompletionProposal(keyword + " ",
								index + 1, offset - (index + 1), keyword
										.length() + 1, img, keyword + " ",
								null, desc));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (int i = 0; i < userDefined.length; i++) {
					String keyword = userDefined[i];
					Image img = new Image(Display.getDefault(),
							this.getUserDefinedImgPath());

					// ContextInformation c = new ContextInformation("pwd",
					// MessageFormat.format(
					// "You''re about to delete {0} rows.", 5));
					proposals.add(new CompletionProposal(keyword + " ",
							index + 1, offset - (index + 1),
							keyword.length() + 1, img, keyword + " ", null,
							"UserDefined predicate"));
				}

			}
			
			if (!proposals.isEmpty()) {
				return (ICompletionProposal[]) proposals
						.toArray(new ICompletionProposal[proposals.size()]);
			}
			return null;
		} catch (Exception e) {
			System.err.println("Erro no CxPrologContentAssist no CompletionProposals");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return new char[] { '#' };
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return fValidator;
	}

}

