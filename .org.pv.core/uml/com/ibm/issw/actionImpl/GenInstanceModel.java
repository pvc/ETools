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

//Uses adapter to gen profile from diagram
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.issw.transforms.Diagram2ProfileApplier;
//import com.ibm.issw.transforms.Diagram2ProfileApplier;

import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.pv.core.Utils;

public class GenInstanceModel  {
	Utils utils=Utils.getSingleton();
	
	Model m=null;
	Profile p;
	URI profileURI;
	Diagram d=null;
	
	public GenInstanceModel(String profilePath) {
		profileURI=utils.string2URI(profilePath);
	}
	public GenInstanceModel() { // take all defaults
	}
	
	public void execute() {
		final List elements = UMLModeler.getUMLUIHelper().getSelectedElements();
		utils.log("***************************************");
		if (elements.isEmpty()) {
			utils.log("Please select a Diagram");
			return;
		}
		Object o = elements.get(0);
		utils.log("Object is:"+o.getClass());
		
		if (!(o instanceof View)) {
			utils.log("Please select a Diagram");
			return;
		}
		d=((View)o).getDiagram();
		profileURI=utils.changeExtension(utils.getURI(d),"epx");
		TxWrapper d2p=new TxWrapper(new Diagram2ProfileApplier(profileURI));
		p=(Profile)d2p.execute(d);
		utils.save(p);
		
		Model tm=utils.getModel(utils.changeFileName(utils.getURI(d),"InstanceModel.emx"));
		utils.applyProfile(tm,p);
		utils.getDiagram(tm);
		utils.save(tm);
				
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
