package org.eclipse.cxide.Menu_ops;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.Workbench;

import prolog.Prolog;

public class CxDialog extends Dialog {
	private  boolean with_output=false;
	private  String output="";
	private String title;
	private static IWorkbenchWindow window = Workbench.getInstance().getActiveWorkbenchWindow();
	private static Shell shell = window.getShell();
	//campos para inserção de texto
	private LinkedHashMap<String, String> forms;
	//campos para inserção de númericos
	private LinkedHashMap<String, Double> numForms;
	//ação a executar
	private CxAction action = null;

	
	 //Cria um novo dialogo com um dado titulo
	 //Com um estilo predefinido
	public CxDialog(String title) {
		this(shell, title, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		this.title=title;
	}
	

	//Cria um novo dialogo com um dadi titulo e estilo
	public CxDialog(Shell parent, String title, int style) {
		super(parent, style);
		forms = new LinkedHashMap<String, String>();
		numForms = new LinkedHashMap<String, Double>();
		setText(title);
	}
	
	//Revela o dialogo ao utilizador
	public void open() {
		// Create the dialog window
		Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		createContents(shell);
		shell.pack();
		shell.open();
		
	}


	/**
	 * Adds a new text form field with a given label
	 * 
	 * @param label_name
	 *            - The label of the new text form field
	 */

	public void addTextForm(String label_name) {
		System.out.println("D-> FormDialog:addTextForm");
		forms.put(label_name, "");
	}

	/**
	 * Adds a new number form field with a given label
	 * 
	 * @param label_name
	 *            - The label of the new number form field
	 */
	public void addNumForm(String label_name) {
		System.out.println("D-> FormDialog:addNumForm");
		numForms.put(label_name, 0.0);
	}

	/**
	 * Get the current value of the text form field with the given label_name
	 * 
	 * @param label_name
	 *            - The label of the text form field
	 * @return The current value of the text form field
	 */
	public String getTextField(String label_name) {
		System.out.println("D-> FormDialog:getTextField");
		return forms.get(label_name);
	}

	/**
	 * Gets the current value of the number field with the given label, the
	 * value is returned has a Double
	 * 
	 * @param label_name
	 *            - The label of the text form field
	 * @return - The current value has a Double of the number field with the
	 *         given label
	 */
	public Double getNumField(String label_name) {
		System.out.println("D-> FormDialog:getNumField");
		return numForms.get(label_name);
	}
	
	public void withOutput(boolean output){
		with_output=output;
	}
	
	public void setOutput(Object[] cx_output){
		with_output=true;
		String aux ="";
		
		if(cx_output!=null){
			int i=0;
			while(i<cx_output.length){
				aux+=cx_output[i].toString()+"\n";
				i++;
			}
		}
		output=Arrays.toString(cx_output);
	}

	/**
	 * Creates a new form field with the given label_name
	 * 
	 * @param label_name
	 *            - The label name of the form field to be created
	 * @param shell
	 *            - The shell of the dialog window
	 */

	private void createForm(String label_name, Shell shell) {
		System.out.println("D-> FormDialog:CreateForm");
		// Show the message
		Label label = new Label(shell, SWT.LEFT);
		label.setText(label_name);
		// Display the input box
		final  Text text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		   text.setLayoutData(new GridData(GridData.FILL_BOTH));
		final String cheat = label_name;
		
		
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Modified");
				if (forms.containsKey(cheat))
					forms.put(cheat, text.getText());
				
				try{
				if (numForms.containsKey(cheat))
					numForms.put(cheat, Double.parseDouble(text.getText()));
				}catch(NumberFormatException ex){
					System.out.println("NOT A NUMBER AT: "+cheat);
				}
			}});

		// Necessary so that when the user closes the dialog the values he
		// entered are saved
		/*text.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				// TODO Auto-generated method stub
				System.out.println(text.getText());
				
				if (forms.containsKey(cheat))
					forms.put(cheat, text.getText());
				
				try{
				if (numForms.containsKey(cheat))
					numForms.put(cheat, Double.parseDouble(text.getText()));
				}catch(NumberFormatException ex){
					System.out.println("NOT A NUMBER AT: "+cheat);
				}
			}
		});*/

	}
	
	
	

	/**
	 * Creates the dialog's contents
	 * 
	 * @param shell
	 *            the dialog window
	 */
	private void createContents(final Shell shell) {
		System.out.println("D-> FormDialog:createContents");
		GridLayout layout = new GridLayout(2, false);
		layout.marginLeft = 10;
		layout.marginRight = 5;
		shell.setLayout(layout);
		Iterator<String> it = forms.keySet().iterator();
		while (it.hasNext())
			createForm(it.next(), shell);

		it = numForms.keySet().iterator();
		while (it.hasNext())
			createForm(it.next(), shell);
		
		
		// Create the OK button and add a handler
		// so that pressing it will set input
		// to the entered value
		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		GridData first_data = new GridData();
		ok.setLayoutData(first_data);

		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				System.out.println("YOU CLICKED OK");
				if (forms.size() != 0 || numForms.size()!=0){
					if (action != null){
						action.run();
					}
				}
				if(with_output)
					MessageDialog.openInformation(
							window.getShell(),
							"Dialog \""+title+ "\" output:",
							output);
				
				shell.close();
			}
		});

		// Create the cancel button and add a handler
		// so that pressing it will set input to null
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		first_data = new GridData();
		cancel.setLayoutData(first_data);
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.close();
			}
		});

		// Set the OK button as the default, so
		// user can type input and press Enter
		// to dismiss
		shell.setDefaultButton(ok);
	}

	/**
	 * Defines the action of the form dialog
	 * 
	 * @param action_cmd
	 *            - A String with the prolog predicates that define the action
	 *            of the form dialog
	 */

	public void setAction( String dialog_title,String action_cmd) {
		System.out.println("D-> FormDialog:setAction");
		action = new CxAction(dialog_title, action_cmd);
	}
	
	
}