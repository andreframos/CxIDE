package thingsToSEE;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IMarkerResolution;

public class QuickFix implements IMarkerResolution {
    String label;
    QuickFix(String label) {
       this.label = label;
    }
    public String getLabel() {
       return label;
    }
    
	@Override
	public void run(IMarker marker) {
		 MessageDialog.openInformation(null, "QuickFix Demo",
		          "This quick-fix is not yet implemented");
	}
 }