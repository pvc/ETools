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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;
 
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.issw.transforms.List2PackageApplier;


import org.eclipse.gmf.runtime.notation.Diagram;
import org.pv.core.Utils;

public class AddClasses  {
	Utils utils=Utils.getSingleton();
	
	Package p=null;
	URI profileURI; 
	Diagram d=null;
	
	public AddClasses(String profilePath) {
		profileURI=utils.string2URI(profilePath);
	}
	public AddClasses() { // take all defaults
	}
	
	public void execute() {
		IResource infile = utils.getSelectedResource();
		utils.log("***************************************");
		if (null==infile||!(infile instanceof IFile)) {
			utils.log("Please select an input Namelist file");
			return;
		}
		LineNumberReader is;
		try {
			is = new LineNumberReader(new InputStreamReader(((IFile)infile).getContents()));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
			return;
		}
		List names=new LinkedList();
		String line=null;
		for (int i=0; ;i++) {
			try {
				line = is.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace(utils.getLogger());
			}
			if (line==null) {break;}
			names.add(line);
		}
	
		Model m=utils.getModel(utils.changeFileName(utils.getURI(infile),"InstanceModel.emx"));
		Package pkg=m.getNestedPackage("JustAdded");
		TxWrapper t=new TxWrapper(new List2PackageApplier(pkg));
		t.execute(names);

		utils.save(m);
				
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
