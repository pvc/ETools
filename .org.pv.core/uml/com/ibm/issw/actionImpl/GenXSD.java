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
 * Created on 11-Jan-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.actionImpl;

import org.eclipse.core.resources.IFile;
import org.pv.core.Utils;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenXSD {
	Utils utils=Utils.getSingleton();

	/* (non-Javadoc)
	 * @see com.ibm.issw.rsa.interfaces.Executable#execute(java.lang.Object)
	 */
	public void execute() {
		IFile f=utils.genXSD(null,null);
		if (f!=null) {utils.edit(f);} else {utils.log("Selection must be a diagram - please retry");}
	}
	
	

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
