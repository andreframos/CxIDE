package org.eclipse.cxide.utilities;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class FilesBundle {
	public static String contentAssistFile="";
	
	public static String getAbsFilePath(String fileRelPath){
		Bundle bundle = Platform.getBundle("org.eclipse.cxide");
		URL fileBundle = FileLocator.find(bundle, new Path(fileRelPath), null);
		URL fileURL = null;
		try {
			fileURL = FileLocator.toFileURL(fileBundle);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Missing File: "+fileRelPath);
			System.out.println(e.getMessage());
		}
		return fileURL.getPath();
	}
}
