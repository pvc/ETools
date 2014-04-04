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
 * Created on 16-Jul-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.pbe.prefs;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

public class PBEWSProjectEditor extends PBEResourceEditor {

//	static final P4ebRSA7Utils utils=P4ebRSA7Utils.getSingleton();
	public PBEWSProjectEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {	
		super(parent,fieldDef,v,init);
			setMustBeResourceType(PROJECT);
//			setMustExist(true);
		}

	static String copyright() { return Copyright.PV_COPYRIGHT; }

}