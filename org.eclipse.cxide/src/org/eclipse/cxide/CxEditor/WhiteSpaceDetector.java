package org.eclipse.cxide.CxEditor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * Regra para detectar espaços em branco
 * que é utilizada no scanner do editor
 * @author andreramos
 *
 */
public class WhiteSpaceDetector implements IWhitespaceDetector {

	@Override
	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == ',' || c == ':' ||
				c == '-' || c == '+' || c == ';' || c == '\\' || c == '/' ||  c == '=' ||
				c == '(' ||  c == ')' || c == '[' ||  c == ']' ||  c == '|');
	}
}
