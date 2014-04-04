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
import org.eclipse.core.resources.IResource;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Package;
import org.pv.core.Utils;
import org.w3c.dom.Document;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.issw.UMLDiagrams.DiagramAnalyser;
import com.ibm.issw.UMLDiagrams.PackageTreeDiagramBuilder;
import com.ibm.issw.transforms.DOM2MetamodelApplier;
import com.ibm.issw.transforms.DOM2ModelApplier;
import com.ibm.issw.transforms.Diagram2ProfileApplier;
import com.ibm.issw.transforms.DiagramRoot2DOMTransform;
import com.ibm.pbe.transforms.XML2DOMTransform;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ImportXML {
	Utils utils=Utils.getSingleton();
	
	
	public void execute() {
		utils.log("****************************Beginning Test Run");
//		String inputFile="NLS Exception Pattern/sample.appdef";
		IResource sel=utils.getSelectedResource();
		boolean valid=false;
		if ((sel==null)||(sel.getType()!=1)||!(sel.getName().endsWith(".xml"))) {} else {valid=true;}
		if (!valid) {utils.log("Selection must be an xml file - please try again");return;}
		
		String inputFile=sel.getFullPath().toString();
		String xsdName=sel.getProjectRelativePath().removeFileExtension().lastSegment();
//		String inputFile="TestDPTKInvoke/ldif.appdef";
		
		String project=utils.getProjectName(inputFile);
		utils.log("Project="+project);
//		String domain="Exception";
		String metamodelFile=project+"/"+"Metamodel.emx";
		String profileFile=project+"/"+"Metamodel.epx";
		String testModelFile=project+"/"+"InstanceModel.emx";
		
		
		
		
		utils.log("Project="+project);
		
		utils.log("****************************Loading Doc");
		XML2DOMTransform ad1 = new XML2DOMTransform();
		Document doc=ad1.execute(inputFile);
		
		utils.log("****************************Generating MetaModel");
		TxWrapper ad2 = new TxWrapper(new DOM2MetamodelApplier(doc,metamodelFile,xsdName));
		Package mmProfile = (Package)ad2.execute(doc); // not saved at this point
		Model mm=mmProfile.getModel();
		
		utils.log("****************************Building MetaModel Diagram");
		PackageTreeDiagramBuilder pdb = new PackageTreeDiagramBuilder("Metamodel");
		TxWrapper ad3 = new TxWrapper(pdb);
		Diagram profileDiagram=(Diagram)ad3.execute(mm);
		utils.save(mm);
		
		utils.log("*****************************Generating profile");
		TxWrapper d2p=new TxWrapper(new Diagram2ProfileApplier(profileFile,profileDiagram));
		Profile p=(Profile)d2p.execute();
		utils.save(p);
//		UMLModeler.closeProfile(p);
//		p=utils.getProfile(profileFile);
		
		utils.log("*****************************Applying Profile");		
		Model tm=utils.getModel(testModelFile);
		utils.applyProfile(tm,p);
		
		utils.log("****************************Generating Instance Model");
		TxWrapper ad4 = new TxWrapper(new DOM2ModelApplier(doc,testModelFile,profileFile));
		ad4.execute();
		
		utils.log("****************************Building Instance Model Diagram");
		pdb.setDiagramName(null); // allow name to default
		Diagram appModel=(Diagram)ad3.execute(tm);
		utils.save(tm);
		
		utils.log("****************************Generating appdef from Instance Model");
		DiagramAnalyser da = new DiagramAnalyser(appModel);
		Node root=da.getRoot();
		DiagramRoot2DOMTransform ad5=new DiagramRoot2DOMTransform();
		Document appdoc=(Document)ad5.execute(root);
		utils.log("****************************Printing resultant XML:");
		utils.log(utils.doc2String(appdoc));
		
		utils.log("****************************Saving Models ...");
		utils.log("****************************Test Run Complete");
	}	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
