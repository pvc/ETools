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
package org.pv.core;

//Uses adapter to gen profile from diagram
import java.lang.reflect.Method;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;
import org.pv.core.Utils;
import org.pv.plugin.Copyright;



public class Repeat  {
	static final Utils utils=Utils.getSingleton();
	static final Preferences globals=utils.getGlobals();
	
	public void run() {
		String name=globals.getString("lastAction");
		utils.log("LastAction="+name);
		if (name=="") {utils.log("No repeatable action has yet been run"); return;}
//		if (name==myName) {utils.log("*** Last repeatable action is the Repeat action - ignored"); return;}
//		utils.log("Rerunning: "+name);
		try {
		if (name.endsWith("java")) {
			PVLoader r=new PVLoader();
			r.execute(utils.getFile(name));
		} else {utils.log("Last action was invalid - select a valid action to run");}	
//		else {
//			Object o=Platform.getBundle("com.ibm.pbe.PVTools").loadClass(name).newInstance();
//			Method run=null;
//			try {run=o.getClass().getMethod("run",null);} catch (NoSuchMethodException e) {} catch (Exception e) {e.printStackTrace(utils.getLogger());}
//			if (run!=null) {try {run.invoke(o,null);} catch (Exception e) {e.printStackTrace(utils.getLogger());}  }
//		}	
		} catch (Exception e) {
			e.printStackTrace(utils.getLogger());
		}
	
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
