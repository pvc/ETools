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

import java.util.Date;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.NewExampleAction;
import org.pv.core.StopWatch;
import org.pv.core.Utils;
import org.w3c.dom.Element;


//public class P4ebEditAction extends Pluglet implements IWorkbenchWindowActionDelegate {
public class Restart implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	Utils utils=Utils.getSingleton();
	private ISelection sel;
	
	public void run() {
//		Test the Action by setting up any required input here, eg:
		IAction a=new NewExampleAction();
		a.setId("com.ibm.pbe.wASConfig.Type1Action1.001");
		run(a);
//		Now Use Ctl-Alt-R to run, Ctl-Alt-M to add to Tools menu, Ctl-ALT-= to rerun last action in Tools menu
	}
	
	public void run(IAction action) {
		utils.log("Running Action: "+this.getClass().getName()+" "+new Date());
		StopWatch timer = utils.getTimer();
		
// Typical statements		
//		EObject eo; Class c;
		
//		EObject sel = utils.getSelectedUMLElement();
//		IResource sel = utils.getSelectedResource();
//		if (sel instanceof View) {eo=((View)sel).getElement();}
//		if (eo instanceof Class) {c=(Class)eo;}
//		if (sel==null) {utils.log("Please select a <insert correct type> and Retry");}
		
//		Document doc=utils.getNewDocument("model");
//		Element root=doc.getDocumentElement();
//		Element child=utils.addElement(root,"newChild");
//		child.setAttribute("att1","att1Value");
//		utils.dump(doc);
//		PatternApplicationStatus status = utils.execDPTK(doc,"com.ibm.pbe.patterns.myPattern");
//		utils.dump(status);
		utils.saveEditors();
		utils.restart();
		utils.log("Action complete: "+this.getClass().getName()+" at "+new Date()+" -- Total elapsed="+timer.getTotalElapsed() );
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
