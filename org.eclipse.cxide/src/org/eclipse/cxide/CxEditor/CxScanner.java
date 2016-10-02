package org.eclipse.cxide.CxEditor;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.cxide.utilities.ColorManager;
import org.eclipse.cxide.utilities.Editor_Utilities;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Um scanner genérico que pode ser programado
 * com uma sequência de regras.
 * Para obter o próximo token as regras são verificadas
 * em sequência e caso uma se verifique é retornado o respectivo token
 * @author andreramos
 *
 */
public class CxScanner extends RuleBasedScanner implements
		IPropertyChangeListener {

	static ColorManager manager;
	
	//As regras para o realce de sintaxe
	// As regras são definidas no arranque do IDE
	// O utilizador define as mesmas com predicatos como
	//editor_addSingleLineRule
	//mais informações em Editor_Utilities
	static IRule[] rules = new IRule[0];

	
	public CxScanner(ColorManager manager) throws CoreException {
		CxScanner.manager = manager;
		Editor_Utilities.setColorManager(manager);
		IRule[] rules = Editor_Utilities.getRules();
		setRules(rules);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {

	
	}

}
