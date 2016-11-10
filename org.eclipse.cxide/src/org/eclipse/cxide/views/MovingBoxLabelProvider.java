package org.eclipse.cxide.views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import thingsToSEE.FilesBundle;



public class MovingBoxLabelProvider extends LabelProvider {	
	private Map imageCache = new HashMap(11);
	
	/*
	 * @see ILabelProvider#getImage(Object)
	 */
	public Image getImage(Object element) {
		
		/*ImageDescriptor descriptor = null;
		if (element instanceof MovingBox) {
			descriptor = Activator.getImageDescriptor("movingBox.gif");
		} else if (element instanceof Book) {
			descriptor = Activator.getImageDescriptor("book.gif");
		} else if (element instanceof BoardGame) {
			descriptor = Activator.getImageDescriptor("gameboard.gif");
		} else {
			throw unknownElement(element);
		}*/
		Image img;
		if (element instanceof PredNode) {
			img = new Image(Display.getDefault(),
					FilesBundle.getAbsFilePath("icons/pred.png"));
		} else if (element instanceof AtomNode) {
			img = new Image(Display.getDefault(),
					FilesBundle.getAbsFilePath("/icons/javaOutlineGreenDot.png"));
		} else if (element instanceof UserElement) {
			img = new Image(Display.getDefault(),
					FilesBundle.getAbsFilePath("/icons/contentAssist/userDef.png"));
		} else {
			throw unknownElement(element);
		}
		//obtain the cached image corresponding to the descriptor
	/*	Image image = (Image)imageCache.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			imageCache.put(descriptor, image);
		}*/
		return img;
	}

	/*
	 * @see ILabelProvider#getText(Object)
	 */
	public String getText(Object element) {
		if (element instanceof PredNode) {
			if(((PredNode)element).getName() == null) {
				return "Box";
			} else {
				return ((PredNode)element).getName();
			}
		} else if (element instanceof AtomNode) {
			return ((AtomNode)element).getTitle();
		} else if (element instanceof UserElement) {
			return ((UserElement)element).getTitle();
		} else {
			throw unknownElement(element);
		}
	}
	
	public int getPredStart(Object element) {
		if (element instanceof PredNode) {
			return ((PredNode)element).getStart();
			} else {
				return -1;
			}
		}
	
	public int getPredRange(Object element) {
		if (element instanceof PredNode) {
			return ((PredNode)element).getRange();
			} else {
				return -1;
			}
		}
	

	public void dispose() {
		for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
			((Image) i.next()).dispose();
		}
		imageCache.clear();
	}

	protected RuntimeException unknownElement(Object element) {
		return new RuntimeException("Unknown type of element in tree of type " + element.getClass().getName());
	}

}