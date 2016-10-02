package org.eclipse.cxide.views;

import java.util.Iterator;

import org.eclipse.core.runtime.IPath;
import org.eclipse.cxide.Activator;
import org.eclipse.cxide.CxEditor.CxEditor;
import org.eclipse.cxide.Menu_ops.Editor_Operations;
import org.eclipse.cxide.Menu_ops.OutlineElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;



public class OutlineView extends ViewPart implements IPartListener2 {

	protected static TreeViewer treeViewer;
	protected Text text;
	protected MovingBoxLabelProvider labelProvider;


	protected PredNode root;

	
	public OutlineView() {
		IWorkbenchPage page = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window != null) {
			page = window.getActivePage();
		}

		if (page == null) {
			// Look for a window and get the page off it!
			IWorkbenchWindow[] windows = PlatformUI.getWorkbench()
					.getWorkbenchWindows();
			for (int i = 0; i < windows.length; i++) {
				if (windows[i] != null) {
					window = windows[i];
					page = windows[i].getActivePage();
					if (page != null)
						break;
				}
			}
		}
		if (page == null)
			System.out.println("Page NULL");
		else
			System.out.println("Page Not Null");

		page.addPartListener(this);
	}

	public boolean getEditorFile() {
		IWorkbench workb = Activator.getDefault().getWorkbench();
		if (workb == null) {
			return false;
		}

		IWorkbenchWindow[] wins = workb.getWorkbenchWindows();
		if (wins.length <= 0) {
			return false;

		}

		int i = 0;
		while ((i < wins.length)
				&& (!(wins[i].getPartService().getActivePart() instanceof IEditorPart)))
			i++;
		if (i >= wins.length) {
			return false;
		}

		IEditorPart editor = (IEditorPart) wins[i].getPartService()
				.getActivePart();

		IEditorInput input = editor.getEditorInput();
		if (input == null) {
			return false;
		}
		IPath path = ((FileEditorInput) input).getPath();
		if (path == null) {
			return false;
		}

		return true;

	}

	/*
	 * @see IWorkbenchPart#createPartControl(Composite)
	 */
	public void createPartControl(Composite parent) {
		/*
		 * Create a grid layout object so the text and treeviewer are layed out
		 * the way I want.
		 */
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 2;
		parent.setLayout(layout);

		/*
		 * Create a "label" to display information in. I'm using a text field
		 * instead of a lable so you can copy-paste out of it.
		 */
		text = new Text(parent, SWT.READ_ONLY | SWT.SINGLE | SWT.BORDER);
		// layout the text field above the treeviewer
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		text.setLayoutData(layoutData);

		// Create the tree viewer as a child of the composite parent
		treeViewer = new TreeViewer(parent);
		treeViewer.setContentProvider(new MovingBoxContentProvider());
		labelProvider = new MovingBoxLabelProvider();
		treeViewer.setLabelProvider(labelProvider);

		treeViewer.setUseHashlookup(true);

		// layout the tree viewer below the text field
		layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.verticalAlignment = GridData.FILL;
		treeViewer.getControl().setLayoutData(layoutData);

		// Create menu, toolbars, filters, sorters.
		
	
		createToolbar();
		hookListeners();

		treeViewer.setInput(getInitalInput());
		treeViewer.expandAll();
	}



	protected void hookListeners() {
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				System.out.println("oioioi");
				// if the selection is empty clear the label
				if (event.getSelection().isEmpty()) {
					text.setText("");
					return;
				}
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event
							.getSelection();
					StringBuffer toShow = new StringBuffer();
					int begPred = -1;
					int rangePred = -1;
					for (Iterator iterator = selection.iterator(); iterator
							.hasNext();) {
						Node domain = (Node) iterator.next();
						String value = labelProvider.getText(domain);
						begPred = labelProvider.getPredStart(domain);
						rangePred = labelProvider.getPredRange(domain);
						toShow.append(value);
						toShow.append(", ");
					}
					// remove the trailing comma space pair
					if (toShow.length() > 0) {
						toShow.setLength(toShow.length() - 2);
					}
					text.setText(toShow.toString());
					Editor_Operations.SelectReveal(begPred, rangePred);
					System.out.println("oioio: "+toShow.toString()+ " "+begPred+ " "+rangePred);
				}
			}
		});
	}

	





	
	

	



	protected void createToolbar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	public PredNode getInitalInput() {
	
		
		return createXMLforOutline();
	}

	/*
	 * @see IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
	}
	
	public static void refresh(){
		Control control = treeViewer.getControl();
		treeViewer.setInput(createXMLforOutline());
		treeViewer.expandAll();
		control.setRedraw(true);
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		try {
			System.out.println(partRef.getPartName());
			if (partRef.getPart(false) instanceof CxEditor) {
				Control control = treeViewer.getControl();
				treeViewer.setInput(createXMLforOutline());
				treeViewer.expandAll();
				control.setRedraw(true);
			}
		} catch (Exception e) {
			System.err.println("OOOOOOOOOOOOOPSSSS");
			e.printStackTrace();

		}
		System.out.println("part activated");
	}
	
	public static PredNode createXMLforOutline() {
		System.out.println("............Called............");
		Iterator<OutlineElement> it = Editor_Operations.getFileFunctors();
		PredNode root = null;
		root = new PredNode();
		while(it.hasNext()){
			OutlineElement predInfo = it.next();
			PredNode newPred=new PredNode(predInfo.getFunctor()+"\\"+predInfo.getArity(),predInfo.getStart(),predInfo.getRange());
			String[] args = predInfo.getArgs();
			int i=0;
			while(i<args.length){
				newPred.add(new AtomNode(args[i]));
				i++;
			}
			root.add(newPred);
		}
		/*String p = "ola(xml).";
		
		
		PredElement newPred=new PredElement("ola" + "/" + 1);
		newPred.add(new Atom("andre"));
		root.add(newPred);
		newPred=new PredElement("ola" + "/" + 1);
		newPred.add(new Atom("ana"));
		root.add(newPred);
		newPred=new PredElement("adeus" + "/" + 2);
		newPred.add(new Atom("andre"));
		newPred.add(new Atom("ana"));
		root.add(newPred);*/
	return root;
	}
	
	public static String getCurrentlySelectFilePath() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench == null ? null : workbench
				.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = window == null ? null : window
				.getActivePage();

		IEditorPart editor = activePage == null ? null : activePage
				.getActiveEditor();
		IEditorInput input = editor == null ? null : editor.getEditorInput();
		IPath path = input instanceof FileEditorInput ? ((FileEditorInput) input)
				.getPath() : null;
		if (path != null) {
			return (path.toOSString());
		} else {
			return null;
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		System.out.println("BroughtoTop");
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		System.out.println("closed");
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		System.out.println("deactivated");
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		System.out.println("open");
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		System.out.println("hidden");
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		if (partRef instanceof EditorPart)
			System.out.println("Iam an editor;");
		System.out.println("visible");
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		System.out.println("inputChanged");
	}
}
