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

import org.eclipse.uml2.uml.NamedElement;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Name2ElementApplier implements Executable {
	NamedElement e;
	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Name2ElementApplier(NamedElement e) {
		this.e=e;
	}
	public Object execute(Object o) {
		if (o==null) {o="(NoName)";}
		return execute(o.toString());
	}
	public Object execute(String name) {
		e.setName(name);
		return e;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
