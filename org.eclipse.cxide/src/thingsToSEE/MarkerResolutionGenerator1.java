package thingsToSEE;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;
//org.eclipse.core.resources.problemmarker
public class MarkerResolutionGenerator1 implements IMarkerResolutionGenerator {

	@Override
		public IMarkerResolution[] getResolutions(IMarker mk) {
			System.out.println("Okdaodkasodkasodkasodk");
	         try {
	            Object problem = mk.getAttribute("WhatsUp");
	            return new IMarkerResolution[] {
	               new QuickFix("Fix #1 for "+problem),
	               new QuickFix("Fix #2 for "+problem),
	            };
	         }
	         catch (CoreException e) {
	            return new IMarkerResolution[0];
	         }
	      }
		

}
