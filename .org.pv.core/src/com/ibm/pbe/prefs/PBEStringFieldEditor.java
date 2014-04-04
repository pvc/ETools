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

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.pv.core.Utils;
import org.w3c.dom.Element;


public class PBEStringFieldEditor extends StringFieldEditor {
	static final Utils utils=Utils.getSingleton();
	protected IPBEFieldValidator validator; 
	boolean mandatory=true;
	String msg=null;
	private static final String MANDATORY = "This entry cannot be empty";
	
	public PBEStringFieldEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(fieldDef.getAttribute("name"),fieldDef.getAttribute("label"),parent);
		if ("true".equals(fieldDef.getAttribute("optional"))) {mandatory=false;}
		this.validator=v;
		getLabelControl().setToolTipText(fieldDef.getAttribute("help"));
		getTextControl().addFocusListener(new FocusAdapter() {
             public void focusGained(FocusEvent e) {
                 refreshValidState();
             }
		});     
	}
	protected boolean doCheckState() {
//		utils.log("Checking String state");
		msg=null;
		String val=getStringValue();
		if (mandatory && val.length()==0) {msg=MANDATORY;}
		if (msg==null) {return true;} else {setErrorMessage(msg); return false;}
	}
//	public PBEStringFieldEditor(String name, String labelText,String tooltipText, Composite parent, PBEValidator validator) {
//		super(name,labelText,parent);
//		getLabelControl().setToolTipText(tooltipText);
//		this.validator=validator;
//	}
//	protected void adjustForNumColumns(int numColumns) {
//		utils.log("adjusting cols to:"+numColumns);
//		super.adjustForNumColumns(numColumns);
//		utils.log("Set grab to:"+((GridData)getTextControl().getLayoutData()).grabExcessHorizontalSpace);
//	}
//	protected void doFillIntoGrid(Composite parent, int numColumns) {
//		utils.log("FillIntoGrid with:"+numColumns);
//		super.doFillIntoGrid(parent,numColumns);
//	}
	protected void doStore() {
		Text txt = getTextControl();
		if (txt!=null) {getPreferenceStore().setValue(getPreferenceName(), txt.getText());} //avoid call after dispose -- Eclipse 3.2 bug?
//		try {
//		utils.log("Stringfield dostore ..");
//		utils.log("Prefstore:"+getPreferenceStore());
//		utils.log(getPreferenceName());
//		utils.log(getTextControl().getText());
//		
//        getPreferenceStore().setValue(getPreferenceName(), getTextControl().getText());
//        utils.log("Stringfield dostore complete");
//		} catch (Exception e) {e.printStackTrace(utils.getLogger());}
    }

		
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}