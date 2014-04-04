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


public class PBEComboEditor extends FieldEditor {
	static final Utils utils=Utils.getSingleton();
	Combo field;
	int comboStyle=SWT.NULL;
	String lastValue;
	IPBEFieldValidator validator;
	
	public PBEComboEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(fieldDef.getAttribute("name"),fieldDef.getAttribute("label"),parent);
		this.validator=v;
		getLabelControl().setToolTipText(fieldDef.getAttribute("help"));
	}
//	public PBEComboEditor(String name, String labelText,String tooltipText, Composite parent, PBEValidator validator) {
//		super(name,labelText,parent);
//		getLabelControl().setToolTipText(tooltipText);
//		this.validator=validator;
//	}

	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns(int numColumns) {
//		utils.log("adjustForNumColumns:"+numColumns);
		Label label =getLabelControl();
		GridData gd = new GridData();
		gd.horizontalSpan=1;
		label.setLayoutData(gd);
		gd = new GridData();
		gd.horizontalSpan = numColumns-1;
//		gd.heightHint=50;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		field.setLayoutData(gd);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite, int)
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		utils.log("doFillIntoGrid:"+numColumns);
		Label label =getLabelControl(parent);
//		GridData gd = new GridData();
//		gd.horizontalSpan=numColumns+2;
//		label.setLayoutData(gd);
		
		field = getCombo(parent);
//		gd = new GridData();
//		gd.horizontalSpan = numColumns+2;
//		gd.heightHint=50;
//			gd.horizontalAlignment = GridData.FILL;
//			gd.grabExcessHorizontalSpace = true;
//		field.setLayoutData(gd);
	}
	public Combo getCombo(Composite parent) {
		return new Combo(parent,SWT.NULL);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	protected void doLoad() {
		if (field != null) {
			doLoadDefault();
			String value = getPreferenceStore().getString(getPreferenceName());
			lastValue = value;
			field.setText(value);
//			field.setText("qwerty");
//			utils.log("Text is:"+field.getText());
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault() {
		if (field != null) {
			String[] values = getPreferenceStore().getDefaultString(getPreferenceName()).split(";");
			if (values.length==0) {values=new String[]{""};}
			lastValue = values[0];
			field.setItems(values);
			field.select(0);
		}
//		valueChanged();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore() {
		getPreferenceStore().setValue(getPreferenceName(), field.getText());
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {
		return 2;
	}
	

	/**
	 * @return Returns the comboStyle.
	 */
	public int getComboStyle() {
		return comboStyle;
	}
	/**
	 * @param comboStyle The comboStyle to set.
	 */
	public void setComboStyle(int comboStyle) {
		this.comboStyle = comboStyle;
	}
}	