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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.NewExampleAction;
import org.pv.core.PBEPath;
import org.pv.core.StopWatch;
import org.pv.core.Utils;
import org.pv.core.WsdlDoc;
import org.pv.core.WsdlDoc.Operation;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

//import com.ibm.dptk.parser.WsdlParser;
import com.ibm.pbe.prefs.IPBEFieldValidator;
import com.ibm.pbe.prefs.IPBEModelHandler;
import com.ibm.pbe.prefs.PBEDialog;

public class PatternApplicator implements IWorkbenchWindowActionDelegate, IPBEFieldValidator, IPBEModelHandler {
	private IWorkbenchWindow window;
	Utils utils=Utils.getSingleton();
	private ISelection sel;
	private int minSegs=5;
	IPath root=new Path("C:/MyWorkspaces/MyToolkits/Setup/");
	private String scaPatternId="com.ibm.pbe.patterns.scaproject.jet";
	private IFile inFile; 
	
//  Method to allow invocatiion for test purposes	
//	Use Ctl-Alt-R to run this method, Ctl-Alt-M to add the Action to PVTools menu, Ctl-ALT-= to rerun last action in PVTools menu
	public void run() {
//		Test the Action by setting up any required input here, eg:
		IAction a=new NewExampleAction();
		a.setId("com.ibm.pbe.wMBPatterns.NewAction");
		run(a); // invoke main run method of this action
	}
	
// Main run method for invocation from Eclipse menu system	
	public void run(IAction action) {
		utils.log("\n***Running Action: "+this.getClass().getName()+" "+new Date());
		StopWatch timer = utils.getTimer();
		
// Typical statements which might be used in this body:		
		boolean validSel=false; Document doc=null; Element root=null;
		IResource sel = utils.getSelectedResource();
		if (sel==null) {sel=utils.getEditedFile();}
		if (utils.isFile(sel) && sel.getFileExtension().equals("appdef")) {
			validSel=true;
		} 
		if (!validSel) {utils.log("Please select a valid model (appdef file) and retry"); return;}
		utils.log("Selected file is: "+sel);
		IFile inFile=(IFile)sel;
		run(inFile);

		utils.log("***Action complete: "+this.getClass().getName()+" at "+new Date()+" -- Total elapsed="+timer.getTotalElapsed() );
	}
	
	public int run(IFile in) {
		inFile=in;
		Element modelRoot=utils.getDocRoot(inFile);
		return run(modelRoot);
	}
	public int run(Element contextRoot) {
		Document editModel=null;
		Document modelDoc=contextRoot.getOwnerDocument();
		Element modelRoot=modelDoc.getDocumentElement();
		inFile=(IFile)modelDoc.getUserData("pbeFile");
		utils.log("Pattern input File: "+inFile);
//		String hdlrId=modelRoot.getAttribute("handler");
//		if (hdlrId.length()>0) {invokeHandler(contextRoot,hdlrId); return 99;}
		String edId=modelRoot.getAttribute("editor");
		if ("xml".equals(edId)) {utils.edit(inFile,"org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart"); return Window.OK;}
		
		if ("false".equals(modelRoot.getAttribute("edit"))) {return generate(modelRoot);}
		IPath editModelPath = inFile.getFullPath().removeFileExtension().addFileExtension("pbeDialog");
//		utils.log(editModelPath);
		boolean editorDefined=false;
		URI edURI=URI.createURI(modelRoot.getAttribute("editor"));
//		utils.log(edURI);
		if (!edURI.hasEmptyPath()) {
			editorDefined=true;
			utils.log("Marker1");
			editModel=utils.getDoc(edURI);
		}
		if (editModel==null) {utils.log("Marker2");editModel=utils.getDoc(utils.getFile(editModelPath));editorDefined=false;}
		if (editModel==null) {
			utils.log("Marker3");
			Element modelBodyRoot=getModelBodyRoot(modelRoot);
			editModel=getDefaultEditor(modelBodyRoot);
			utils.save(editModel,editModelPath);
		}
		if (!editorDefined) {
			utils.log("Marker4");
			modelRoot.setAttribute("editor","platform:/resource"+editModelPath);
			utils.save(modelRoot,inFile);
		}
		int result=run (modelRoot,editModel);
//		if (result==Window.CANCEL && !"false".equals(modelRoot.getAttribute("pbe.save"))) {result=utils.askQuestion("Confirm Save", "Pattern Application cancelled - save contents of editor?")?Window.OK:Window.CANCEL;
//		if (result==Window.OK) {utils.save(modelRoot, inFile);utils.dump(inFile);} 
//		}
		if (!"false".equals(modelRoot.getAttribute("pbe.save"))) {utils.save(modelRoot, inFile);}
		if (result==Window.OK) {return generate(modelRoot);} else {return result;}
	}
	private void invokeHandler(Element contextRoot, String hdlrId) {
		new PBEPath(hdlrId);
		
	}

	private Element getModelBodyRoot(Element modelRoot) {
		if (!modelRoot.getLocalName().startsWith("pbe")) {return modelRoot;}
		Element body=utils.getFirstElementAt(modelRoot,"pbe.body");
		utils.dump("pbe.body is: "+body);
		if (body!=null) {return utils.getFirstChild(body);} else {return utils.getFirstChild(modelRoot);}
	}

	public int run(Element modelRoot, Document editModel) {
		
//		Element modelRoot=(model=utils.getDoc(utils.getPluginFileInputStream("com.ibm.pbe.WMBPatterns", "Patterns/HelenModel.appdef"))).getDocumentElement();
//		Element editModelRoot=(editModel=utils.getDoc(utils.getPluginFileInputStream("com.ibm.pbe.WMBPatterns", "Patterns/HelenDialog.xml"))).getDocumentElement();
//		Element modelRoot=(model=utils.getDoc(utils.getFile("WMBPatterns/Patterns/HelenModel.appdef"))).getDocumentElement();
		
		Element editModelRoot=editModel.getDocumentElement();
//		Element editModelRoot=(editModel=utils.getDoc(utils.getFile("WMBPatterns/Patterns/HelenDialog.xml"))).getDocumentElement();
		
		String vName=modelRoot.getAttribute("validator");
		Class val=null;IPBEFieldValidator validator=this;
		if (vName.length()>0) {
			try {val = Class.forName(vName);
			validator=(IPBEFieldValidator) val.newInstance();
			} catch (ClassNotFoundException e) {utils.log("Validator class not found: "+vName);}
			  catch (Exception e) {utils.log("Could not instantiate: "+vName);e.printStackTrace(utils.getLogger());}
		}
		utils.log("Validator set to "+validator.getClass());
//		try {
//		if (wcdlFile.exists()) {wcdlFile.delete(true,null);utils.log("Deleted wcdlFile");}
//		if (!wcdlFile.exists()) {wcdlFile.create(null,true,null);utils.log("Created wcdlFile");} 
//		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
	
		PBEDialog dlg = new PBEDialog();
		if (Window.CANCEL==dlg.execute(editModelRoot,modelRoot, validator)) {utils.log("Pattern execution cancelled");return Window.CANCEL;}
		else {return Window.OK;}
		
	}
	public int generate (Element modelRoot) {
		try {
		//		utils.dump(modelRoot);
		IStatus status;
		String patternId=modelRoot.getAttribute("pattern");
		boolean jet=true;
		if (patternId.endsWith("dptk")) {
//			PatternApplicationStatus dptkstatus = utils.execDPTK(modelRoot,modelRoot.getAttribute("pattern"));
//			utils.dump(dptkstatus);
			utils.log("DPTK Patterns are not supported in this version - please obtain the DPTK version or move to JET");return 99;
		} else if (modelRoot.getAttribute("pbe.env").equals("wid")) {
			utils.log("\nGenerating wcdl in pattern: "+patternId);
			modelRoot.setAttribute("pbe.phase", "genModel");
			processWsdls(modelRoot);
//			status = utils.execDPTK(modelRoot,patternId);
//			utils.dump(status);
			status = utils.runJet(modelRoot, patternId);
			utils.dump(status);
			utils.log("\nGenerating base module");
//			try {wcdlFile.refreshLocal(0,null);
//			utils.getProject(".temp").refreshLocal(IResource.DEPTH_INFINITE,null);
//			utils.getProject(".temp").build(IncrementalProjectBuilder.FULL_BUILD, null);
//			} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
			IFile wcdlFile = utils.getFile(".temp/generated.wcdl"); 
			Element wrapper= utils.getDocRoot(wcdlFile);
			Element wcdlRoot= utils.getFirstChild(wrapper);
			WcdlDeabstracter deab = new WcdlDeabstracter();
			deab.decompact(wcdlRoot);
			utils.dump(wcdlRoot);
			StringBuilder libNames=new StringBuilder();
			Set<IProject> libs=new HashSet<IProject>();
			for (Element lib:utils.getChildElements(wcdlRoot,"library")) {
					libNames.append(lib.getAttribute("name"));
			}
			utils.log("Existing libNames="+libNames);
			for (Element imp:utils.getChildElements(utils.getFirstElementAt(wcdlRoot,"WSDLS"))) {
				try {
					utils.log("Processing wsdl lib from: "+imp.getAttribute("location"));
					String loc=imp.getAttribute("location").split("/")[0];
					if (loc.length()>0 && (libNames.indexOf(loc)==-1)) {libs.add(utils.getProject(loc));}
				} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
			}
			for (Element imp:utils.getChildElements(utils.getFirstElementAt(wcdlRoot,"PORTS"))) {
				try {
					String loc=imp.getAttribute("location").split("/")[0];
					if (loc.length()>0 && (libNames.indexOf(loc)==-1)) {libs.add(utils.getProject(loc));}
				} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
			}
			
//			utils.log("Adding libraries");
			for (IProject p:libs) {
				if (p==null) {utils.log("NULL LIBRAY FOUND & OMITTED"); continue;}
				utils.log("Adding library: "+p.getName());
				utils.addElement(wcdlRoot, "library").setAttribute("name",p.getName());
			}
			
//			utils.dump(wcdlDoc);
//			status = utils.execDPTK(wcdlDoc,scaPattern);
//			utils.dump(status);
			status = utils.runJet(wcdlRoot, scaPatternId);
			utils.dump(status);
			utils.log("\nGenerating additional module content");
			modelRoot.setAttribute("pbe.phase", "genModule");
//			status = utils.execDPTK(modelRoot,modelRoot.getAttribute("pattern"));
//			utils.dump(status);
			status = utils.runJet(modelRoot, patternId);
			utils.dump(status);
		}
		else {
			status = utils.runJet(modelRoot, patternId);
			utils.dump(status);
		}
		} catch (Exception e) {e.printStackTrace(utils.getLogger()); return Window.CANCEL;}
		return Window.OK;
	}
	
	private void processWsdls(Element modelRoot) {
		List<Element> modelElts = utils.getChildElements(modelRoot);
		for (Element modelElt:modelElts) {
		utils.log("Processing Model Element "+modelElt.getAttribute("name"));
		List<Attr> wsdls = utils.getAttributes(modelElt, "wsdl");
		utils.log("Found wsdls in model: "+wsdls.size());
		for (Attr wsdl:wsdls) {
			Element wsdlElt = utils.addElement(modelElt,"wsdl");
			String id=wsdl.getName().substring(4);
			wsdlElt.setAttribute("id",id);
			String suffix="";
			if (id.charAt(0)=='I') {suffix=id.substring(2);} else {suffix=id.substring(3);}
			if (suffix.equals("1")) {suffix="";}
			wsdlElt.setAttribute("suffix",suffix);
			wsdlElt.setAttribute("location",wsdl.getValue());
			processWsdl(wsdlElt);
		}
		}
		utils.dump(modelRoot);
	}

	private void processWsdl(Element wsdlElt) {
		WsdlDoc wsdlDoc=new WsdlDoc(utils.getFile(wsdlElt.getAttribute("location")));
		if (wsdlDoc.hasPort()) {
		wsdlElt.setAttribute("port",wsdlDoc.getPort());
		wsdlElt.setAttribute("service",wsdlDoc.getService());
		wsdlElt.setAttribute("serviceNS",wsdlDoc.getServiceNS());
//		wsdlElt.setAttribute("binding",wsdlDoc.getBinding());
//		wsdlElt.setAttribute("bindingNS",wsdlDoc.getBindingNS());
		wsdlElt.setAttribute("address",wsdlDoc.getAddress());
		}
		if (wsdlDoc.hasPortType()) {
			wsdlElt.setAttribute("portType",wsdlDoc.getPortType());
			wsdlElt.setAttribute("portTypeNS",wsdlDoc.getPortTypeNS());
			wsdlElt.setAttribute("portType",wsdlDoc.getPortType());
		}	
		List<Operation> ops = wsdlDoc.getOperations();
		for (Operation op:ops) {
			Element opElt = utils.addElement(wsdlElt,"operation");
			opElt.setAttribute("name",op.getName());
			opElt.setAttribute("input",op.getInputMessageName());
			opElt.setAttribute("inputNS",op.getInputMessageNS());
			opElt.setAttribute("output",op.getOutputMessageName());
			opElt.setAttribute("outputNS",op.getOutputMessageNS());
		}
	}

	public Document getDefaultEditor(Element modelTarget) { 
		Element edRoot = utils.getNewDocumentRoot("properties");
		edRoot.setAttribute("title","Pattern Model Editor");
		edRoot.setAttribute("label","The following model attributes are available to edit:");
		Element tgt = modelTarget;
		NamedNodeMap atts = tgt.getAttributes();
		for (int i = 0; i < atts.getLength(); i++) {
			Attr att = (Attr)atts.item(i);
			Element prop = utils.addElement(edRoot, "property");
			prop.setAttribute("name", att.getLocalName());
			prop.setAttribute("label","Enter value for "+att.getLocalName());
			if (att.getValue().equals("true")||att.getValue().equals("false")) {prop.setAttribute("type","boolean");}
		}
		return edRoot.getOwnerDocument();
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
		String msg=null;
//		String name=fe.getPreferenceName();
//		fe.store();
//		String value=fe.getPreferenceStore().getString(name);
//		utils.log("Validating "+name);
//		
//		if ("interface".equals(name)) {
//			utils.log("Value is "+value);
//			if (!value.endsWith(".wsdl")) {msg="Please select a valid wsdl interface";}
//		}
		return msg;
	}

	public String doSelect(FieldEditor fe, Object selection) {
		String msg=null;
//		String name=fe.getPreferenceName();
//		utils.log("Validating Selection"+name);
//		if ("interface".equals(name)) {
//			utils.log("Value is "+selection);
//			if ( !(selection instanceof IFile) || !((IFile)selection).getFileExtension().equals("wsdl") ) {msg="Please select a valid wsdl interface";}
//		}
		return msg;
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }

	
}
