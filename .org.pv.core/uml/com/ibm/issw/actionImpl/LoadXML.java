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
 * Created on 27-Oct-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.actionImpl;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Class;
import org.pv.core.StopWatch;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.issw.UMLDiagrams.TreeDiagramBuilder;
import com.ibm.issw.transforms.DOM2MetamodelApplier;
import com.ibm.issw.transforms.DOM2ModelApplier;
import com.ibm.issw.transforms.Diagram2ProfileApplier;
import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.pbe.transforms.XML2DOMTransform;
import com.ibm.xtools.modeler.ui.UMLModeler;

import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoadXML implements Executable {
	Utils utils=Utils.getSingleton();
	boolean allowProfileUpdate=false;
	final String NIL="";
	boolean updateProfile=false;boolean updateModel=false;boolean diagramRoot=true;
	Element root;
	String fn;
	String appName; 
	IProject proj;
	
	public void run() {execute();}
	public void execute() {
		Collection models = UMLModeler.getOpenedModels();
		utils.dump(models);
		if(true) {return;}
		
		
		utils.log("****************************Beginning Appdef Import at "+new Date());
		StopWatch timer = utils.getTimer();
		//		String inputFile="NLS Exception Pattern/sample.appdef";
		boolean valid=false;
		IResource sel=utils.getSelectedResource();
		if (sel==null) {sel=utils.getEditedFile();}
		if ((sel==null)||(sel.getType()!=IResource.FILE)||!(sel.getName().endsWith(".appdef"))) {} else {valid=true;}
		if (!valid) {utils.log("Selection must be an appdef file - please try again");return;}
		utils.save();
		
		
		proj=sel.getProject();
		
		fn=sel.getProjectRelativePath().removeFileExtension().lastSegment();
		String appPath=sel.getFullPath().toString();
		
		utils.log("Loading XML model: "+appPath);
		XML2DOMTransform ad1 = new XML2DOMTransform();
		Document doc=ad1.execute(appPath);
		if (doc==null) {utils.log("Error: empty document loaded from: "+appPath);return;}
		
		root=doc.getDocumentElement();
		appName=root.getAttribute("name"); 
		if (appName.equals(NIL)) {appName=fn; root.setAttribute("name", appName);}  //set DOM name - this does not overwrite the source file
		else if (!fn.equalsIgnoreCase(appName)) {renameFile(sel,appName);}
		
		if (!root.getNodeName().equals("patternApp")) {
			Element app=doc.createElement("patternApp");
			doc.replaceChild(app,root);
			app.appendChild(root);
			app.setAttribute("name", appName+"App");
			app.setAttribute("pattern", root.getAttribute("pattern"));
			root=app;
		}
		
//		root.setAttribute("xmlElementName", root.getLocalName()); // in case not "patternApp"
		if (root.getAttribute("pattern").equals(NIL)) {root.setAttribute("pattern", "com.ibm.pbe.patterns."+appName);}
		utils.log("Pattern="+root.getAttribute("pattern"));
		utils.log(root);
		
		Properties rootValues=getAppValues(root);
		if (rootValues.getProperty("targetProfile").equals(NIL)) {rootValues.setProperty("targetProfile",rootValues.getProperty("targetModel")+"Profile");}
		utils.log("Using parameters:");
		utils.log(rootValues);

		String projPath=rootValues.getProperty("targetProject")+"/";
		String modelFile=projPath+rootValues.getProperty("targetModel")+".emx";
		String profileFile=projPath+rootValues.getProperty("targetProfile")+".epx";
		String metamodelFile=projPath+rootValues.getProperty("targetProfile")+".emx";
		rootValues.setProperty("profileFile", profileFile);
		rootValues.setProperty("modelFile", modelFile);
		rootValues.setProperty("metamodelFile", metamodelFile);
		rootValues.put("inputDocument",doc);  // no longer serialisable at this point
		
		String applyTo=rootValues.getProperty("applyTo");
//		utils.log(applyTo);
		if (applyTo.equals("model,profile")) {updateProfile=true;updateModel=true;}
		else if (applyTo.equals("profile")) {updateProfile=true;}
		else if (applyTo.equals("model")) {updateModel=true;}
//		utils.log("flags:"+updateModel+updateProfile);
		if ("false".equals(rootValues.getProperty("diagramRoot"))) {diagramRoot=false;}
		utils.log("Step completed in: "+timer +" seconds");
		timer.restart();
		
		Model model=null;
		if (updateProfile||(utils.wsr.findMember(profileFile)==null)) {
			
			utils.log("****************************Building MetaModel "+new Date());
			DOM2MetamodelApplier d2m = new DOM2MetamodelApplier(doc,metamodelFile,rootValues.getProperty("targetProfile"));
			TxWrapper ad2 = new TxWrapper(d2m);
			Package profilePkg = (Package)ad2.execute(); // not saved at this point
			NamedElement profileRoot=getFirstTarget(profilePkg);
			Model mm=utils.getModel(profilePkg);
			utils.log("Step completed in: "+timer +" seconds");
			timer.restart();
			
			utils.log("****************************Building MetaModel Diagram "+new Date());
			TreeDiagramBuilder tdb = new TreeDiagramBuilder(profilePkg,profileRoot);
//			String profileRoot=((NamedElement)((Dependency)mm.getOwnedMember("DocumentRoot").getClientDependencies().get(0)).getSuppliers().get(0)).getName();
			TxWrapper ad3 = new TxWrapper(tdb);
			Diagram profileDiagram=(Diagram)ad3.execute();
			utils.save(mm);
			utils.log("Step completed in: "+timer +" seconds");
			timer.restart();
			utils.log("*****************************Generating profile "+new Date());
			TxWrapper d2p=new TxWrapper(new Diagram2ProfileApplier(profileFile,profileDiagram));
			Profile p=(Profile)d2p.execute();
			//		utils.saveProfile(p);
			//		UMLModeler.closeProfile(p);
			//		p=utils.getProfile(profileFile);
			utils.save(p);
			utils.sleep(2000);
			utils.log("Step completed in: "+timer +" seconds");
			timer.restart();
//			utils.log("*****************************Applying Profile "+new Date());		
//			if (model==null) {model=utils.getModel(modelFile);}
//			utils.applyProfile(model,p);
			utils.log("Step completed in: "+timer +" seconds");
			timer.restart();
		}
		
		if (updateModel) {
		utils.log("****************************Generating Instance Model "+new Date());
		Class patternApp;
		TxWrapper ad4 = new TxWrapper(new DOM2ModelApplier(doc,modelFile,profileFile));
		patternApp=(Class) ad4.execute();
//		utils.log("PatternRoot="+patternApp);
		utils.log("Step completed in: "+timer +" seconds");
		timer.restart();
//				utils.saveModel(model);
		
		if (patternApp!=null) { 
		utils.log("****************************Building Instance Model Diagram "+new Date());
		//		String tempAppName=((NamedElement)((Dependency)tm.getOwnedMember("DocumentRoot").getClientDependencies().get(0)).getSuppliers().get(0)).getName();
//		TreeDiagramBuilder tdb = new TreeDiagramBuilder(appName);
		TreeDiagramBuilder tdb = new TreeDiagramBuilder(patternApp.getNearestPackage(),patternApp);
		tdb.setdiagramDocNode(diagramRoot);
//		tdb.setTreeRootName(appName);
		TxWrapper ad5=new TxWrapper(tdb);
		Diagram appModel=(Diagram)ad5.execute();
		utils.udh.openDiagramEditor(appModel);
		utils.log("Step completed in: "+timer +" seconds");
		timer.restart();
		//		DiagramAnalyser da = new DiagramAnalyser(appModel);
		//		Node root=da.getRoot();
		
		//		utils.log("****************************Generating appdef from Instance Model "+new Date());
		//		DiagramRoot2DOMTransform ad6=new DiagramRoot2DOMTransform();
		//		Document appdoc=(Document)ad6.execute(root);
		//		utils.saveDoc(appdoc,"InstanceModel.appdef");
		//		utils.log("****************************Printing resultant XML:");
		//		utils.log(utils.doc2String(appdoc));
		}
		if (patternApp!=null) {model=patternApp.getModel();} else {model=utils.getModel(modelFile);}
		if (model!=null) {utils.save(model);}
		}
		utils.log("****************************Run Complete "+new Date()+" -- Total elapsed="+timer.getTotalElapsed());
	}
	
	
	
	/**
	 * @param profilePkg
	 * @return
	 */
	private NamedElement getFirstTarget(NamedElement ne) {
		return (NamedElement)((Dependency)ne.getClientDependencies().get(0)).getSuppliers().get(0);
	}



	private void renameFile(IResource r, String newName) {
		/* Rename the file to the modelname */
//		utils.log(r);
//		utils.log(newName);
			try {
				r.move(r.getProjectRelativePath().removeLastSegments(1).append(newName+".appdef"),true,null);
			} catch (CoreException e) {
				utils.log("Error: file "+r.getName()+" could not be renamed to match its internal appname: "+newName);
				e.printStackTrace(utils.getLogger());
			}
	}


	/* (non-Javadoc)
	 * @see com.ibm.issw.rsa.interfaces.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		execute();
		return null;
	}	
	/**
	 * @return Returns the allowProfileUpdate.
	 */
	public boolean isAllowProfileUpdate() {
		return allowProfileUpdate;
	}
	/**
	 * @param allowProfileUpdate The allowProfileUpdate to set.
	 */
	public void setAllowProfileUpdate(boolean allowProfileUpdate) {
		this.allowProfileUpdate = allowProfileUpdate;
	}
	
	//	public String[] getRootValuePair(String attName) {
	//		String[] vp=new String[3];
	//		String tempAttName="temp"+uc1(attName); 
	//		vp[0]=rootValues.getProperty(tempAttName);  //TEMP COMES FIRST
	//		vp[1]=rootValues.getProperty(attName);
	//		vp[2]=tempAttName;
	//		return vp;
	//	}
	//
	//	public String uc1(String s) {
	//		if (s==null||s.equals(NIL) {return s;}
	//		return s.substring(0,1).toUpperCase()+s.substring(1);
	//	}
	
	public Properties getAppValues(Element e) {
		Properties p=getDefaults();
//		p.setProperty("@tagName", e.getLocalName());
		NamedNodeMap atts = e.getAttributes();
		// Process base values
		for (int i = 0; i < atts.getLength(); i++) {
			org.w3c.dom.Node att = atts.item(i);
			if ( (!att.getNodeName().startsWith("temp")) && (att.getNodeValue()!=NIL) ){
				p.setProperty(att.getNodeName(),att.getNodeValue());
			}
		}
		// process override values
		for (int i = 0; i < atts.getLength(); i++) {
			org.w3c.dom.Node att = atts.item(i);
			if (att.getNodeName().startsWith("temp")) {
				String untemp=untemp(att.getNodeName());
				String val=att.getNodeValue();
				att.setNodeValue("");
				if (val.equals(NIL)) {val=e.getAttribute(untemp);}
				if (!val.equals(NIL)) {p.setProperty(untemp,val );}
			}
		}
		return p;
	}



	private Properties getDefaults() {
		Properties p=new Properties();
		p.setProperty("logFile",proj.getName()+"/"+appName+"Log. xml"); 
		p.setProperty("targetProject",proj.getName()); 
		p.setProperty("targetModel",appName); 
		p.setProperty("targetProfile",appName+"Profile"); 
		p.setProperty("targetType","diagram"); 
		p.setProperty("targetName",appName); 
		p.setProperty("applyTo","model"); 
		p.setProperty("dagramRoot","true"); 
		
		return p;
		
	}

	private String untemp(String key) {
		return key.substring(4,5).toLowerCase()+key.substring(5);
	}
		
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
