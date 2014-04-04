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
package com.ibm.issw.actionImpl;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.osgi.framework.Bundle;
import org.pv.core.Utils;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.issw.UMLDiagrams.DiagramInNamespaceCreator;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Diagram;


public class GenMetamodel {
	
	Utils utils=Utils.getSingleton();
	GenProfile gp=new  GenProfile();	
	
	public void execute() {
		utils.log("****************************");
		IResource target=utils.getSelectedResource();
		boolean valid=false;
		if (target!=null) {if ((target.getType()==IResource.PROJECT)||(target.getType()==IResource.FOLDER)) {valid=true;} }
		if (!valid) {utils.log("Selection must be a Project or Folder - please retry");return;}
		
		Bundle b=Platform.getBundle("com.ibm.pbe.umlToolkit");
		URL url=b.getEntry("/profiles/PBEMMProfile.epx"); //must include path, leading slash is optional
		URI profileURI=URI.createURI(url.toString());
		Profile p=null;
		try {
			p=UMLModeler.openProfile(profileURI);
//			utils.log("Profile opened: " +p.getName());
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace(utils.getLogger());
		}
		
			
				String mm="Metamodel";
				Model m=utils.getModel( ((IContainer)target).getFullPath().append(mm+".emx").toString());
//				m.setName(mm);
				utils.log("Model: "+m);
				utils.applyProfile(m,p);
				DiagramInNamespaceCreator d2m=new DiagramInNamespaceCreator();
				TxWrapper txd2m=new TxWrapper(d2m);
				Diagram d=(Diagram)txd2m.execute(m);
				utils.log("Diagram: "+d);
//				d.setName(mm);
				utils.save(m);
				
				
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}