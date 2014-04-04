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
package com.ibm.pbe.patterns;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.ui.actions.NewExampleAction;
import org.pv.core.PBEPath;
import org.pv.core.Utils;
import org.w3c.dom.Element;

//import com.ibm.pbe.core.P4ebRSAUtils;
import com.ibm.pbe.prefs.IPBEModelHandler;
import com.ibm.pbe.prefs.IPBEModelHandler2;
import com.ibm.pbe.prefs.PBEMLEEditor;

public class EditorLauncher implements IEditorLauncher {
	final static Utils utils=Utils.getSingleton();

	//  Method to allow invocatiion for test purposes	
	//	Use Ctl-Alt-R to run this method, Ctl-Alt-M to add the Action to PVTools menu, Ctl-ALT-= to rerun last action in PVTools menu
	public void run() {
		open(null);
	}

	public void open(IPath extPath) {
		IPath file=utils.getActiveFile().getFullPath();
		Element root = utils.getDocRoot(utils.getFile(file));
		
		if (root==null) {utils.log(file+"\nPlease select a valid appdef and retry");return;}
		String handler=root.getAttribute("handler");
		utils.log("Appdef Handler="+handler);
		if (handler.length()>0) {
			PBEPath hPath=null;Class targetClass=null;
			if (handler.charAt(0)=='@') {
				if ("@PBEWIDModuleHandler,@PBEBaseHandler".contains(handler)) {targetClass=PatternApplicator.class;}
			} else {
				hPath= new PBEPath(handler);
				//		IPBEModelHandler2 handler; // won't work - why?
				//			try{Object h=hPath.toExecutable();utils.dump(h.getClass());handler=(IPBEModelHandler2)h;} catch (ClassCastException e) {e.printStackTrace(utils.getLogger());return;}
				//			try{handler=(IPBEModelHandler)hPath.toExecutable();} catch (ClassCastException e) {utils.log(file+": "+hPath.toString()+" is not a valid Modelhandler");return;}
				//			if (handler==null) {utils.log("Could not load Pattern handler: "+hPath+" from "+file);return;}
				//			handler.run(root);
				targetClass=hPath.loadClass();
			}
			utils.log("Handler set to: "+targetClass);
			if (targetClass==null) {utils.log("Invalid handler set in appdef "+file+" - please correct & retry");return;}
			Method run=null; 
			try {run=targetClass.getMethod("run",Element.class);} catch (NoSuchMethodException e) {} catch (Exception e) {e.printStackTrace(utils.getLogger());}
			if (run!=null) {
				try {
					if (Modifier.isStatic(run.getModifiers()))  {
						run.invoke(targetClass, root);
					} else  {run.invoke(targetClass.newInstance(), root);}
				} catch (Exception e) {e.printStackTrace(utils.getLogger());return;}
			} else {utils.log("ModelHandler run method not found on "+targetClass.getName());return;}
		} else {
		IAction a=new NewExampleAction();
		new PatternApplicator().run(root);
		}
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
