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
package org.pv.core.actions;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jdt.internal.core.util.Util;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.NewExampleAction;
import org.osgi.framework.Bundle;
import org.pv.core.Utils;
import org.pv.plugin.Activator;


//public class P4ebEditAction extends Pluglet implements IWorkbenchWindowActionDelegate {
public class GenericActionDelegate implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
//	private CreatePattern actionImpl=new CreatePattern();
	static final Utils utils=Utils.getSingleton();
//	static Activator plugin=Activator.getDefault();
	static final Preferences globals=utils.getGlobals();
//	static final String REPEAT="com.ibm.pbe.core.Repeat"; 
//	static final String RUNNER="com.ibm.pbe.core.PBELoader"; 
	static final String PLUGIN=Activator.getPluginId(); 
	
private ISelection sel;
	public void run() {
		IAction a=new NewExampleAction();
		a.setId("com.ibm.pv.toolkit.actionImpl.MenuTester@001");
		run(a);
	}
	
	public void run(IAction action) {
//		utils.log("Id: "+action.getId());
//		utils.log("DefId: "+action.getActionDefinitionId());
//		utils.log("Selection Dump executed");
		String fqname=action.getId();
		
		String name=fqname.split("@")[0];
//		String name=fqname.substring(0,fqname.lastIndexOf('.'));
//		utils.log("\nPBE UI delegating request to: "+name);
		Object o=null;
		try {
			o=Platform.getBundle(PLUGIN).loadClass(name).newInstance();
			Method run=null;
			try {run=o.getClass().getMethod("run",null);} catch (Exception e) {e.printStackTrace(utils.getLogger());}
			if (run!=null) {try {run.invoke(o,null);} catch (Exception e) {e.printStackTrace(utils.getLogger());}  }
			else {utils.log(name+" does not have a run method - cannot continue");}
			
		} catch (NullPointerException e) {
			Bundle b = Platform.getBundle(PLUGIN);
			if (b==null) {utils.log("***Error: Plugin "+PLUGIN+" could not be located");return;}
			if (o==null) {utils.log("Class "+name+" could not be found");return;}
			e.printStackTrace(utils.getLogger());
		} catch (Exception e) {
			e.printStackTrace(utils.getLogger());
	}
		
//		if (REPEAT.equals(name)||RUNNER.equals(name)) {} else {globals.setValue("lastAction",name);}
	}

	/**
	 * @param sc
	 */


	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.sel=selection;
//		utils.log("Selection: "+selection);
//		utils.log("Action: "+action);
//		if (selection instanceof IStructuredSelection) {
//			IStructuredSelection s = (IStructuredSelection) selection;
//		}
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
