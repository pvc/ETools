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


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenAppdef  {
	Utils utils=Utils.getSingleton();
	
	public void run() {execute();}
	public void execute() {
		utils.log("**************Starting Generation of Appdef");
		IFile f=utils.genAppdefFile();
		if (f==null) {utils.log("Please select a diagram and retry"); return;}
		utils.edit(f);
	}
		
	static String copyright() { return Copyright.PV_COPYRIGHT; }
	}
