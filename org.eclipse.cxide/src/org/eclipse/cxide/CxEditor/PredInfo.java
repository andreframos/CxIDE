package org.eclipse.cxide.CxEditor;

/**
 * Class utilizada para representar informações de um predicado
 * mais especificamente o seu functor e a sua descrição
 * 
 * É utilizado no ContentAssist para adicionar novas
 * completions proposals
 * @author andreramos
 *
 */
public class PredInfo {
	
	
	private String predicate;
	private String description;
	
	public PredInfo(String predicate, String description){
		this.predicate = predicate;
		this.description = description;
	}
	
	//Obter o functor do predicado
	protected String getPred(){
		return predicate;
	}
	
	//Obter a descrição do predicado
	//Esta descrição é a que vem no manual do CxProlog
	protected String getDesc(){
		return description;
	}
	
	protected String getInfo(){
		return predicate+"\n"+description;
	}

}
