//*****************************************************************************
//* Licensed Materials - Property of IBM
//*
//* com.ibm.sal.rapidsat
//*
//* (C) Copyright IBM Corp. 2007
//*
//* US Government Users Restricted Rights - Use, duplication or
//* disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
//*****************************************************************************
/*
 * Created on 03-Oct-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.pbe.prefs;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;
import org.osgi.framework.Bundle;
import org.pv.core.Utils;


/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PreferenceTest  {
	Utils utils=Utils.getSingleton();

	
	String sourcePath="wfc/java";
	String importPath="wfc/imports";
	String exportPath="wfc/exports";
	String wsdlPath="wfc/interfaces";
	String serverURL="http://localhost:9080/";
//	static final Preferences prefs=Platform.getPreferencesService();
	
	
	public void plugletmain(String[] args) {execute(null);}
	public void execute() {
		execute(null);
	}
	
	public Object execute(Object o) {
		
		utils.log("**************************************************");
		utils.log("Starting Generation run ...");
		Bundle b = Platform.getBundle("com.ibm.pbe.WPSTest");
		String clName=(String)b.getHeaders().get("Bundle-Activator");
		utils.log(clName);
		Class ba=null;
		try {
		ba = b.loadClass(clName);
		Method m=ba.getMethod("getDefault",new Class[] {});
		Plugin p=(Plugin)m.invoke(ba,new Object[]{});
		Preferences prefs = p.getPluginPreferences();
		utils.log(prefs);
		serverURL=prefs.getString("ServerURL");
		utils.log(serverURL);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(utils.getLogger());
		}
		return null;
		}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
	
	
}
