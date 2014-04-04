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
 * Created on 02-Jan-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.transforms;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

//import com.ibm.issw.transforms.Diagram2ProfileGraphTransform;
import com.ibm.pbe.graphs.Graph;
import com.ibm.pbe.rsa.interfaces.Executable;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.pv.core.Utils;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Diagram2ProfileApplier implements Executable {
	Utils utils=Utils.getSingleton() ;
	
	Model m;
	URI profileURI;
	Profile p;
	String profileName;
	Diagram d;

	public Diagram2ProfileApplier(String profilePath, Diagram profileDiagram) {
	profileURI=utils.string2URI(profilePath);
	this.d=profileDiagram;
	}
	
	
	public Diagram2ProfileApplier(URI uri){
		profileURI=uri;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected Profile execute() {
		Diagram2ProfileGraphTransform ga=new Diagram2ProfileGraphTransform(d);
		Graph g=ga.execute();
		m=utils.getModel(d);
		if (profileURI==null) {profileURI= utils.changeExtension(utils.getURI(m),"epx");}
		p=utils.getProfile(profileURI);
		p.addKeyword("DSL");
		
		
//		String pName=d.getName();
//		utils.log("Diagram name: "+pName);
//		if ("".equals(pName)|"Diagram1".equals(pName)) {pName="PrimaryDomain";}
//		int index=pName.indexOf("Metamodel");
//		if (index>=0) {pName=pName.substring(0,index);}
		profileName=utils.getName(profileURI);
		p.setName(profileName);
		utils.log("Profile Name set to: "+profileName);
		ProfileGraph2ProfileApplier g2p=new ProfileGraph2ProfileApplier(p);
		p=g2p.execute(g);
		return p;
	}

	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		return execute();
	}	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
