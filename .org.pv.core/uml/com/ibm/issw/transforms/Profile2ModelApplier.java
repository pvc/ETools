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

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.pv.core.Utils;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Profile2ModelApplier implements Executable {
	Utils utils= Utils.getSingleton();
	Model m;
	
	public Profile2ModelApplier(Model m) {
		this.m=m;
	}
	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		return execute((Profile)o);
	}

	public Model execute(Profile p) {
//		utils.log("**********************************************Entering: "+this.getClass().getName());
//		utils.log("Trying Profile apply of "+p+" to: "+m);
//		utils.log("Currently Applied version: "+m.getAppliedVersion(p));
		try {m.applyProfile(p);} catch (Exception e) {utils.log("Prfofile apply problem: "+e);}
		utils.log("Applied Profile "+p.getName()+ " (VersionId "+p.getDefinition().getNsURI()+") to Model "+m.getName());
		return m;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
