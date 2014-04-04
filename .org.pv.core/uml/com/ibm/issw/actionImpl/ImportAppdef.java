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

import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ImportAppdef implements Executable {
	Utils utils=Utils.getSingleton();
//	boolean allowProfileUpdate=false;
	final String NIL="";
	boolean updateProfile=false;boolean updateModel=false;boolean reapplyProfile=false;boolean diagramDocNode=false;
	Element root;
	String fn;
	String dslName; 
	IProject proj;
	Profile dslProfile=null;
	Model model=null;
	private String modelName;
	
	public void run() {execute();}
	public void execute() {
		utils.log("****************************Beginning Appdef Import at "+new Date());
		StopWatch timer = utils.getTimer();
		//		String inputFile="NLS Exception Pattern/sample.appdef";
		boolean valid=false;
		IResource sel=utils.getSelectedResource();
		if (sel==null) {sel=utils.getEditedFile();}
		valid=false;
		if ((sel!=null)&&(sel.getType()==IResource.FILE)&& ((sel.getName().endsWith(".appdef")||sel.getName().endsWith(".xml")))) {valid=true;}
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
		dslName=root.getAttribute("dslName"); 
		if (dslName.equals(NIL)) {dslName=fn; }  //set DOM name - this does not overwrite the source file
//		else if (!fn.equalsIgnoreCase(dslName)) {renameFile(sel,dslName);} // too confusing when file renames self
		modelName=root.getAttribute("name");
		if (modelName.equals(NIL)) {modelName=root.getNodeName();}
		if (!root.getNodeName().equals("metadata")) {
			Element app=doc.createElement("metadata");
			doc.replaceChild(app,root);
			app.appendChild(root);
			app.setAttribute("pattern", root.getAttribute("pattern"));
			app.setAttribute("name", modelName+"Meta");
			root=app;
		}
		
//		root.setAttribute("xmlElementName", root.getLocalName()); // in case not "metadata"
		root.setAttribute("dslName", dslName);
		if (root.getAttribute("pattern").equals(NIL)) {root.setAttribute("pattern", "com.ibm.pbe.patterns."+dslName);}
		utils.log("Default Pattern set to: "+root.getAttribute("pattern"));
//		utils.log(root);
		
		Properties rootValues=getAppValues(root);
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
		if ("true".equals(rootValues.getProperty("diagramDocNode"))) {diagramDocNode=true;} else {diagramDocNode=false;}
//		utils.dump(doc);
		utils.log("Step completed in: "+timer +" seconds");
		timer.restart();
		
		
		if (utils.wsr.findMember(modelFile)==null) {updateModel=true; reapplyProfile=true;}
		if (utils.wsr.findMember(profileFile)==null) {updateProfile=true;} // must create
		if (updateProfile) {
			reapplyProfile=true; 
			
			utils.log("****************************Building DSL MetaModel "+new Date());
			DOM2MetamodelApplier d2m = new DOM2MetamodelApplier(doc,metamodelFile,rootValues.getProperty("targetProfile"));
			TxWrapper ad2 = new TxWrapper(d2m);
			Package profilePkg = (Package)ad2.execute(); // not saved at this point
			NamedElement profileRoot=getFirstTarget(profilePkg);
			utils.log("Step completed in: "+timer +" seconds");
			timer.restart();
			
			utils.log("****************************Building MetaModel Diagram "+new Date());
			TreeDiagramBuilder tdb = new TreeDiagramBuilder(profilePkg,profileRoot);
//			String profileRoot=((NamedElement)((Dependency)mm.getOwnedMember("DocumentRoot").getClientDependencies().get(0)).getSuppliers().get(0)).getName();
			TxWrapper ad3 = new TxWrapper(tdb);
			Diagram profileDiagram=(Diagram)ad3.execute();
			Model mm=utils.getModel(profilePkg);
			utils.save(mm);
			utils.sleep(2000); //???
			utils.log("Step completed in: "+timer +" seconds");
			timer.restart();
			utils.log("*****************************Generating UML Profile for DSL"+new Date());
			TxWrapper d2p=new TxWrapper(new Diagram2ProfileApplier(profileFile,profileDiagram));
			dslProfile=(Profile)d2p.execute();
			//		utils.saveProfile(p);
			//		UMLModeler.closeProfile(p);
			//		p=utils.getProfile(profileFile);
			utils.save(dslProfile);
			utils.sleep(2000); //???
			utils.log("Step completed in: "+timer +" seconds");
//			timer.restart();
//			utils.log("*****************************Applying Profile "+new Date());	 // now applied in step below	
//			if (model==null) {model=utils.getModel(modelFile);}
//			utils.applyProfile(model,p);
//			utils.log("Step completed in: "+timer +" seconds");
			timer.restart();
		}
		
		
		if (updateModel) {
		utils.log("****************************Generating Model "+new Date());
		model=utils.getModel(modelFile);
		
		if (reapplyProfile) {
			if (dslProfile==null) {dslProfile=utils.getProfile(profileFile);}
			utils.applyProfile(model, dslProfile);
		}
		
		TxWrapper ad4 = new TxWrapper(new DOM2ModelApplier(doc,modelFile,profileFile)); 
		Class rootClass=(Class) ad4.execute();
//		utils.log("PatternRoot="+metadata);
		utils.log("Step completed in: "+timer +" seconds");
		timer.restart();
//				utils.saveModel(model);
		
		if (rootClass!=null) { 
		utils.log("****************************Building Model Diagram "+new Date());
		//		String tempdslName=((NamedElement)((Dependency)tm.getOwnedMember("DocumentRoot").getClientDependencies().get(0)).getSuppliers().get(0)).getName();
//		TreeDiagramBuilder tdb = new TreeDiagramBuilder(dslName);
		TreeDiagramBuilder tdb = new TreeDiagramBuilder(rootClass.getNearestPackage(),rootClass);
		tdb.setdiagramDocNode(diagramDocNode);
//		tdb.setTreeRootName(dslName);
		TxWrapper ad5=new TxWrapper(tdb);
		Diagram appModel=(Diagram)ad5.execute();
		utils.udh.openDiagramEditor(appModel);
		utils.log("Step completed in: "+timer +" seconds");
		timer.restart();
		//		DiagramAnalyser da = new DiagramAnalyser(appModel);
		//		Node root=da.getRoot();
		
		//		utils.log("****************************Generating appdef from Instance Model "+new Date());
		//		diagramDocNode2DOMTransform ad6=new diagramDocNode2DOMTransform();
		//		Document appdoc=(Document)ad6.execute(root);
		//		utils.saveDoc(appdoc,"InstanceModel.appdef");
		//		utils.log("****************************Printing resultant XML:");
		//		utils.log(utils.doc2String(appdoc));
		}
//		if (metadata!=null) {model=metadata.getModel();} else {model=utils.getModel(modelFile);}
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
				utils.log("Error: file "+r.getName()+" could not be renamed to match its internal dslName: "+newName);
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
//	public boolean isAllowProfileUpdate() {
//		return allowProfileUpdate;
//	}
//	/**
//	 * @param allowProfileUpdate The allowProfileUpdate to set.
//	 */
//	public void setAllowProfileUpdate(boolean allowProfileUpdate) {
//		this.allowProfileUpdate = allowProfileUpdate;
//	}
	
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
		p.setProperty("logFile",proj.getName()+"/"+modelName+"Log.xml"); 
		p.setProperty("targetProject",proj.getName()); 
		p.setProperty("targetModel",modelName); 
		p.setProperty("targetProfile",dslName+"Profile"); 
		p.setProperty("targetType","diagram"); 
		p.setProperty("targetName",modelName); 
		p.setProperty("applyTo","model"); 
		p.setProperty("diagramRoot","false"); 
		
		return p;
		
	}

	private String untemp(String key) {
		return key.substring(4,5).toLowerCase()+key.substring(5);
	}
		
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
