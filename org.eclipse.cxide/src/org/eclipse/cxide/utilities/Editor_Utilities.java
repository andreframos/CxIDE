package org.eclipse.cxide.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.cxide.CxEditor.VarRule;
import org.eclipse.cxide.CxEditor.WhiteSpaceDetector;
import org.eclipse.cxide.CxEditor.WordDetector;
import org.eclipse.cxide.preferences.EditorPreferences;
import org.eclipse.cxide.preferences.PreferenceConstants;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import prolog.Prolog;

public class Editor_Utilities {
	private static String[] builtins;
	private static String[] userDefined;
	private static HashMap<String,ArrayList<RulesInfo>> rulesInfo = new HashMap<String,ArrayList<RulesInfo>>();
	private static IRule[] rules = new IRule[0];
	private static String contentAssistFilePath;
	static ColorManager manager;
	
	public static void setColorManager(ColorManager man){
		manager=man;
	}
	
	public static void addRuleInfo(String fieldName, RulesInfo ruleInfo){
		ArrayList<RulesInfo> al = rulesInfo.get(fieldName);
		if(al==null){
			al = new ArrayList<RulesInfo>();
			rulesInfo.put(fieldName, al);
			rulesInfo.get(fieldName).add(ruleInfo);
		}
	}
	
	public static void addRule(IRule newRule){
		try{
			
			IRule[] aux = new IRule[rules.length+1];
			aux[rules.length]=newRule;
			for (int i=0; i<rules.length ; i++)
				aux[i]=rules[i];
				rules=aux;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static IRule[] getRules(){
		return rules;
	}
	
	public static void setRules(){
		IToken variableToken = tokenFor(manager.getVariableColor());
		IToken stringToken = tokenFor(manager.getStringColor());
		IToken commentToken = tokenFor(manager.getCommentColor());
		IToken wordToken = tokenFor(manager.getDefaultColor());
		addRule(new VarRule(variableToken));
		addRule(new SingleLineRule("?", "?", stringToken, '\\'));
		addRule(new SingleLineRule("'", "'", stringToken, '\\'));
		addRule(new WhitespaceRule(new WhiteSpaceDetector()));
		addRule(new EndOfLineRule("%", commentToken));
		addRule(new WordRule(new WordDetector(), wordToken));
		try {
			addWordsTo((WordRule) rules[5]);
		} catch (CoreException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void changeRuleColor(String fieldName, int r, int g, int b){
		//rules= new IRule[1];
		//IToken token = tokenForColor(new RGB(255, 0, 0));
		//rules[0]=  new SingleLineRule("*", "*", token, '?');
		
		Iterator<RulesInfo> it = rulesInfo.get(fieldName).iterator();
		while(it.hasNext()){
			RulesInfo info = it.next();
		int index = info.getIndex();
		String type = info.getType();
		
		switch(type){
		case "SLR":
			IToken token = tokenForColor(new RGB(r,g,b));
			rules[index]=new SingleLineRule(info.getStartSeq(), info.getEndSeq(), token, info.getEscChar());
		break;
		
		case "EOR":
			IToken token2 = tokenForColor(new RGB(r,g,b));
			rules[index]=new EndOfLineRule(info.getStartSeq(), token2);
			break;
	
		case "DTR":
			System.out.println("DTR");
			IToken token3 = tokenForColor(new RGB(r,g,b));
			rules[index]=new WordRule(new WordDetector(),token3);
					((WordRule)rules[index]).addWord(fieldName,tokenForColor(new RGB(info.r,info.g,info.b)));
					Iterator<String> inter = rulesInfo.keySet().iterator();
					while(inter.hasNext()){
						String fn = inter.next();
						if(rulesInfo.get(fn).get(0).type.equals("WR")){
							Iterator<RulesInfo> word_it = rulesInfo.get(fn).iterator();
							while(word_it.hasNext()){
								RulesInfo ri = word_it.next();
								((WordRule)rules[index]).addWord(ri.word, tokenForColor(new RGB(ri.r,ri.g,ri.b)));
							}
						}
					}
					
					
					
			break;
			
		case "VR":
			IToken token4 = tokenForColor(new RGB(r,g,b));
			rules[index]=new VarRule(token4);
			break;
	
		case "WR":
			IToken token5 = tokenForColor(new RGB(r,g,b));
			System.out.println("word");
			((WordRule)rules[index]).addWord(info.word, token5);
			
			break;
		}
		}
	}
	
	public static void singleLineRule(String fieldName, String startSeq, String endSeq, String escChar, int red, int green, int blue){
		char esc = escChar.charAt(0);
		EditorPreferences.addPrefField(fieldName, red, green, blue);
		IToken token = tokenForColor(new RGB(red,green,blue));
		addRuleInfo(fieldName, new RulesInfo(startSeq,endSeq,esc,rules.length));
		addRule( new SingleLineRule(startSeq, endSeq, token, esc));
	}
	
	public static void endOfLineRule(String fieldName, String startSeq, int red, int green, int blue){
		EditorPreferences.addPrefField(fieldName, red, green, blue);
		IToken token = tokenForColor(new RGB(red,green,blue));
		addRuleInfo(fieldName, new RulesInfo(startSeq,rules.length));
		addRule(new EndOfLineRule(startSeq, token));
		addRule( new MultiLineRule("/*", "*/",token));
	}
	
	public static void addDefaultTextRule(String fieldName, int red, int green, int blue){
		EditorPreferences.addPrefField(fieldName, red, green, blue);
		IToken token = tokenForColor(new RGB(red,green,blue));
		addRuleInfo(fieldName, new RulesInfo(rules.length,"DTR"));
		addRule(new WordRule(new WordDetector(),token));
	}
	
	public static void addVarRule(String fieldName, int red, int green, int blue){
		PreferenceConstants.changeVariableColor(red, green, blue);
		EditorPreferences.addPrefField(fieldName, red, green, blue);
		IToken token = tokenForColor(PreferenceConstants.VARIABLE_COLOR__PREFERENCE);
		addRuleInfo(fieldName, new RulesInfo(rules.length,"VR"));
		addRule(new VarRule(token));
	}
	
	public static void addWordRule(String fieldName,String word, int red, int green, int blue){
		EditorPreferences.addPrefField(fieldName, red, green, blue);
		IToken token = tokenForColor(new RGB(red,green,blue));
		int index = getWordRule();
		
		if (index == -1){
			System.err.println("Editor_Utilities Problems");
			System.err.println("Need to define WordRule first");
			return;
		}
		addRuleInfo(fieldName, new RulesInfo(fieldName,word,index,red,green,blue));
		((WordRule) rules[index]).addWord(word,token);
	}
	
	public static void setContentAssistFile(String path){
		contentAssistFilePath = path;
	}
	
	public static String getContentAssistFile(){
		return contentAssistFilePath;
	}
	
	
	
	public static int getWordRule(){
		int i=0;
		
		while (i<rules.length && !(rules[i] instanceof WordRule))
				i++;
		if(rules.length==i)
			i--;
	
		
		if (rules[i] instanceof WordRule)
			return i;
		else
			return -1;
				
	}
	
	private static Token tokenForColor(RGB color) {
		
		return new Token(new TextAttribute(getColor(color), null, 1));
	}
	
	protected static Map<RGB, Color> fColorTable = new HashMap<RGB, Color>(10);
	private static Color getColor(RGB rgb) {
		Color color = fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
	
	
	private static Token tokenFor(RGB color) {
	
		return new Token(new TextAttribute(manager.getColor(color), null, 1));
	}
	
	
	private static void addWordsTo(WordRule wordRule) throws CoreException {

		// builtins
		addWordsWithPropertyBuiltins(tokenFor(manager.getBuiltinColor()),
				wordRule);

		// User Defined
		addWordsWithPropertyUserDefined(
				tokenFor(manager.getUserDefinedColor()), wordRule);

	}
	
	private static void addWordsWithPropertyBuiltins(IToken keywordToken,
			WordRule wordRule) throws CoreException {
		
		Prolog.CallProlog("getBuiltins(Jvetor)");
		String[] plBuiltInPredicates = Editor_Utilities.getBuiltins();
		Arrays.toString(plBuiltInPredicates);
		for (int i = 0; plBuiltInPredicates != null
				&& i < plBuiltInPredicates.length; i++) {
			wordRule.addWord(plBuiltInPredicates[i], keywordToken);
		}
	}

	private static void addWordsWithPropertyUserDefined(IToken keywordToken,
			WordRule wordRule) throws CoreException {
		Prolog.CallProlog("getUserDefined(Jvetor)");
		String[] plUserDefinedPredicates = Editor_Utilities.getUserDefined();

		for (int i = 0; plUserDefinedPredicates != null
				&& i < plUserDefinedPredicates.length; i++) {
			wordRule.addWord(plUserDefinedPredicates[i], keywordToken);
		}
	}

	public static void setBuiltins(String[] b){ 
		builtins=b;
	}


	public static String[] getBuiltins(){
		return builtins;
	}
	
	public static void setUserDefined(String[] ud){
		System.out.println("User defined length :"+ud.length);
		userDefined=ud;
	}
	
	public static String[] getUserDefined(){
		return userDefined;
	}
	

}
