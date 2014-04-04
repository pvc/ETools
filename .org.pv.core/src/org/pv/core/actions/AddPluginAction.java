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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.NewExampleAction;
import org.pv.core.StopWatch;
import org.pv.core.Utils;
import org.pv.plugin.Activator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//import com.ibm.dptk.patternWizard.PatternApplicationStatus;
import com.ibm.pbe.prefs.IPBEFieldValidator;
import com.ibm.pbe.prefs.PBEDialog;

//public class P4ebEditAction extends Pluglet implements IWorkbenchWindowActionDelegate {
public class AddPluginAction implements IWorkbenchWindowActionDelegate,IPBEFieldValidator {
	private IWorkbenchWindow window;
	Utils utils=Utils.getSingleton();
	StopWatch timer = utils.getTimer();
	static final String pluginId=Activator.getPluginId();
//	static final String pluginId="com.ibm.pbe.core";
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
		IProject proj=null;
	try {
		timer.start();
		IResource r = utils.getSelectedResource();
		utils.log("Selection: "+r);
		boolean valid=false;
		if ( r!=null) {
			proj = r.getProject();
			if (proj.hasNature("org.eclipse.pde.PluginNature")) {valid=true;}
		}
		if (!valid) {utils.log("Please select a plugin & retry"); return;}
		
		IFolder folder=null;
		if (utils.isProject(r) || utils.isProject(r.getParent())) {folder=proj.getFolder("src/"+proj.getName().replace('.','/')+"/actions");}
		else if (utils.isFolder(r)) {folder=(IFolder)r;}
		else {folder=(IFolder)r.getParent();}
		
//		Document doc=null; Element root=null;
		Document view;Document model;
		Element modelRoot=(model=utils.getDoc(pluginId, "patterns/AddPluginAction.appdef")).getDocumentElement();
		Element viewRoot=(view=utils.getDoc(pluginId, "patterns/AddPluginAction.pbeDialog")).getDocumentElement();
		
		Element main=utils.getFirstChild(modelRoot);
		main.setAttribute("outProject",proj.getName());
		main.setAttribute("folder",folder.getFullPath().toString());
		
		PBEDialog dlg = new PBEDialog();
		int result=dlg.execute(viewRoot,modelRoot, this);
//		utils.log("Dialog return="+result);
		
		if (Window.CANCEL==result) {utils.log("Pattern execution cancelled");return;}
		IPreferenceStore prefs = dlg.getPrefs();
		IPath path=new Path(main.getAttribute("folder"));
		main.setAttribute("outProject", path.segment(0));
		String pkgPath=path.removeFirstSegments(2).toString();
		String pkg=pkgPath.replace('/','.');
		String id=pkg+'.'+main.getAttribute("name");
		main.setAttribute("pkg", pkg);
		main.setAttribute("path", pkgPath);
		main.setAttribute("id", id);
//		utils.log("Path="+main.getAttribute("path"));
//		utils.dump(model);
		
//		PatternApplicationStatus status = utils.execDPTK(model,modelRoot.getAttribute("pattern"));
		IStatus status = utils.runJet(model.getDocumentElement(),modelRoot.getAttribute("pattern"));
		utils.dump(status);
//		utils.log("Code="+status.getCode()+",Sev="+status.getSeverity());
		
		if (utils.isOKorINFO(status)) {utils.edit(utils.getFile(path.append(main.getAttribute("name")+".java")));}
		IFile pluginFile=utils.getFile(proj,"plugin.xml");
		Element root=utils.getDocRoot(pluginFile);
		Element as=utils.getFirstElementAt(root, "extension/actionSet");
//		utils.dump("found as="+as);
		if (as!=null) {
			Element actionElt=utils.addElement(as,"action");
			actionElt.setAttribute("label", main.getAttribute("label"));
			actionElt.setAttribute("tooltip", "");
			actionElt.setAttribute("class", main.getAttribute("pkg")+'.'+main.getAttribute("name"));
			actionElt.setAttribute("icon", "icons/sample.gif");
			actionElt.setAttribute("menubarPath", main.getAttribute("outProject")+"/core");
			actionElt.setAttribute("id", id);
			utils.save(root.getOwnerDocument(),pluginFile);
			utils.log("New action successfully added to plugin actionset");
		} else {utils.log("This plugin does not have an actionset - action created but not added to menu");}
		
	} catch (Exception e) {e.printStackTrace(utils.getLogger());}
		
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

	public String doField(FieldEditor fe) {
		// TODO Auto-generated method stub
		return null;
	}

	public String doSelect(FieldEditor fe, Object selection) {
		// TODO Auto-generated method stub
		return null;
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
