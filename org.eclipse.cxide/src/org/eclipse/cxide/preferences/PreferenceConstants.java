package org.eclipse.cxide.preferences;

import org.eclipse.swt.graphics.RGB;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {

	public static String CX_PATH = "/home/andreramos/z/cxprolog-0.98.2/cxprolog";

	public static final String P_BOOLEAN = "booleanPreference";

	public static  String SV_PORT= "serverPort";
	public static  String SV_PORT_VALUE= "30007";
	
	public static String SV_IP = "serverIp";
	public static String SV_IP_VALUE = "127.0.0.1";
	
	public static String COMMENT_COLOR_NAME_PREFERENCE= "Comments";
	public static RGB COMMENT_COLOR__PREFERENCE= new RGB(124,0,124);
	
	public static String VARIABLE_COLOR_NAME_PREFERENCE= "Variables";
	public static RGB VARIABLE_COLOR__PREFERENCE= new RGB(0,0,255);
	
	public static String STRING_COLOR_NAME_PREFERENCE= "Strings";
	public static RGB STRING_COLOR__PREFERENCE= new RGB(0,0,124);
	
	public static String OTHERS_COLOR_NAME_PREFERENCE= "Others";
	public static RGB OTHERS_COLOR__PREFERENCE= new RGB(255,0,0);
	
	public static String BUILTINS_COLOR_NAME_PREFERENCE= "Builtins";
	public static RGB BUILTINS_COLOR__PREFERENCE= new RGB(0,124,124);
	
	public static String USER_DEFINED_COLOR_NAME_PREFERENCE= "UserDefined";
	public static RGB USER_DEFINED_COLOR__PREFERENCE= new RGB(0,255,0);
	
	
	
	public static void changeCxPath(String newPath){
		CX_PATH = newPath;
	}
	
	public static void changeCxPort(String newPort){
		SV_PORT_VALUE= newPort;
	}
	
	public static void changeCxIP(String newIp){
		SV_IP_VALUE= newIp;
	}
	
	public static void changeCommentColor(int r, int g, int b){
		COMMENT_COLOR__PREFERENCE= new RGB(r,g,b);
	}
	
	public static void changeStringColor(int R, int G, int B){
		STRING_COLOR__PREFERENCE = new RGB(R,G,B);
	}
	
	public static void changeVariableColor(int R, int G, int B){
		System.out.println("Changing Variable");
		VARIABLE_COLOR__PREFERENCE = new RGB(R,G,B);
	}
	
	public static void changeOthersColor(int R, int G, int B){
		OTHERS_COLOR__PREFERENCE = new RGB(R,G,B);
	}
	
	public static void changeBuiltinsColor(int R, int G, int B){
		BUILTINS_COLOR__PREFERENCE = new RGB(R,G,B);
	}
	
	public static void changeUserDefinedColor(int R, int G, int B){
		USER_DEFINED_COLOR__PREFERENCE = new RGB(R,G,B);
	}
	
	
	
}
