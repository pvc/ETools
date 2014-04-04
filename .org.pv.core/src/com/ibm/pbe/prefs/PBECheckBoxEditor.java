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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.pv.core.Utils;
import org.w3c.dom.Element;


public class PBECheckBoxEditor extends BooleanFieldEditor {
	static final Utils utils=Utils.getSingleton();
	IPBEFieldValidator validator;
	IPBEFieldInitialiser init;
	private IFieldEditorProvider fep;
	
	public PBECheckBoxEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(fieldDef.getAttribute("name"),fieldDef.getAttribute("label"),BooleanFieldEditor.SEPARATE_LABEL,parent);
		this.validator=v;
	}
	public PBECheckBoxEditor(String name, String labelText,String tooltipText, Composite parent, IPBEFieldValidator validator) {
		super(name,labelText,BooleanFieldEditor.SEPARATE_LABEL,parent);
		this.validator=validator;
//		Label lc = getLabelControl(); // null - not yet set
//		utils.log(lc);
//		getLabelControl().setToolTipText(tooltipText);
	}
//	protected void adjustForNumColumns(int numColumns) {
//		utils.log("adjusting cols to:"+numColumns);
//		super.adjustForNumColumns(numColumns);
//		utils.log("Set grab to:"+((GridData)getTextControl().getLayoutData()).grabExcessHorizontalSpace);
//	}
//	protected void doFillIntoGrid(Composite parent, int numColumns) {
//		utils.log("FillIntoGrid with:"+numColumns);
//		super.doFillIntoGrid(parent,numColumns);
//	}

		
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}