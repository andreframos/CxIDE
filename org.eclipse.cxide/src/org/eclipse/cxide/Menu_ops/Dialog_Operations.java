package org.eclipse.cxide.Menu_ops;

import java.util.Hashtable;
/**
 * Classe que é responsável por todas as operações realizadas com diálogos
 * muitas destas operações podem ser realizadas dinâmicamente.
 * É mantida uma HashTable com todos os dialogos sendo os mesmos identificados
 * por o seu id.
 * É necessária manter esta estrutura para que os dialogos criados em execução pelo
 * utilizador seja armazenados para poderem estar acessíveis sempre que necessário
 * @author andreramos
 *
 */
public class Dialog_Operations {
	
	//Todos os dialogos criados pelo utilizador através do CxProlog
	//são armazenados nesta HashTable a string que os identifica
	//é o id que o utilizador usou para criar o diálogo
	public static Hashtable<String,CxDialog> dialogs = new Hashtable<String, CxDialog>();
	
	/**
	 * Cria um novo diálogo com o titulo/id dado pelo utilizador
	 * @param titulo - O titulo do diálogo
	 * TODO: Remover _ID
	 */
	public static void createDialog(String titulo){
		System.out.println("D-> Dialog_Operations:createDialog");
		CxDialog dialog = new CxDialog(titulo);
		dialogs.put(titulo+"_ID",dialog);
	}
	
	/**
	 * Adiciona um novo campo de texto a um diálogo existente
	 * @param titulo - O título do dialogo
	 * @param label_name - O nome da etiqueta do novo dialogo
	 */
	public static void addTextFiel_dialog(String titulo, String label_name){
		System.out.println("D-> Dialog_Operations:add_Text_Field");
		CxDialog dialog = dialogs.get(titulo+"_ID");
		dialog.addTextForm(label_name);
	}
	
	
	/**
	 * Adiciona um novo campo númerico a um diálogo existente
	 * @param titulo - O titulo do diálogo 
	 * @param label_name - O nome da etiqueta do novo dialogo
	 */
	public static void addNumTextFiel_dialog(String titulo, String label_name){
		CxDialog dialog = dialogs.get(titulo+"_ID");
		dialog.addNumForm(label_name);
	}
	
		/**
		 * Revela um dialogo
		 * @param titulo - O titulo do dialogo a revelar
		 */
		public static void showDialog(String titulo){
			CxDialog dialog = dialogs.get(titulo+"_ID");
			dialog.open();
		}
	
	/**
	 * Obtém o valor de um campo textual dum diálogo existente
	 * 
	 * @param dialog_title - O título do diálogo
	 * @param field - O nome da etiqueta do campo textual
	 * @return - O valor atual do campo textual do diálogo selecionado
	 */
	public static String dialog_getTextField(String dialog_title, String field){
		CxDialog dialog = dialogs.get(dialog_title+"_ID");
		return dialog.getTextField(field);
	}
	
	/**
	 * Obtém o valor dum campo numérico de um diálogo existente
	 * @param dialog_title - O título do diálogo
	 * @param field - O nome da etiqueta do campo numérico
	 * @return o valor atual do campo textual do diálogo selecionado
	 */
	public static String dialog_getNumField(String dialog_title, String field){
		CxDialog dialog = dialogs.get(dialog_title+"_ID");
		return String.valueOf(dialog.getNumField(field));
	}
	
	/**
	 * Modifica o comportamento dum dialogo existente
	 * O Utilizador terá de definir um novo comportamento do diálogo recorrenco ao CxProlog
	 * @param dialog_id - O identificador do diálogo
	 * @param action_cmd - Os predicados que definem o novo comportamento do diálogo
	 */
	public static void dialog_setAction(String dialog_title, String action_cmd){
		System.out.println("D-> Dialog_Operations:dialog_setAction");
		CxDialog dialog = dialogs.get(dialog_title+"_ID");
		System.out.println("Dialog Id: "+dialog_title+"_ID");
		System.out.println("Action: "+action_cmd);
		CxDialog.setAction(action_cmd);
	}
	

}
