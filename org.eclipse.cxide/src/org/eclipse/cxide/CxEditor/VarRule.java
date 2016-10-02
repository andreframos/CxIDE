package org.eclipse.cxide.CxEditor;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Classe que define a regra de deteção de Variáveis
 * que é depois utilizada no scanner do Editor
 * @author andreramos
 *
 */
public class VarRule implements IPredicateRule { 

	IToken token;
	WhiteSpaceDetector wsdetector;
	public VarRule(IToken token) {
		this.token =token;
		wsdetector = new WhiteSpaceDetector();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
	 */
	@Override
	public IToken getSuccessToken() {
		return token;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner, boolean)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		scanner.unread();
		if (!wsdetector.isWhitespace((char)scanner.read()))
			return Token.UNDEFINED;
		int c = scanner.read();
		if (!(c  == '_' || c >= 'A' && c <= 'Z') || c == ICharacterScanner.EOF) { 
			scanner.unread();
			return Token.UNDEFINED;
		}
		do {
		  c = scanner.read();
		} while (!wsdetector.isWhitespace((char)c) && c != ICharacterScanner.EOF);
		scanner.unread();
		return getSuccessToken();
				
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}
}


