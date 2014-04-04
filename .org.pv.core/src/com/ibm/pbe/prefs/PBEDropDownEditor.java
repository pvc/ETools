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

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.pv.core.Utils;
import org.w3c.dom.Element;


public class PBEDropDownEditor extends PBEComboEditor {
	
	IPBEFieldValidator validator; 
	
	public PBEDropDownEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(parent,fieldDef,v,init);
//		setComboStyle(SWT.READ_ONLY);
	}
//	public PBEDropDownEditor(String name, String labelText,String tooltipText, Composite parent, PBEValidator validator) {
//		super(name,labelText,tooltipText,parent,validator);
//		setComboStyle(SWT.READ_ONLY);
//		utils.log("Style set to:"+getComboStyle());
//	}
	public Combo getCombo(Composite parent) {
		return new Combo(parent,SWT.READ_ONLY);
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
	

}	