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
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.pv.core.Utils;
import org.w3c.dom.Element;


public class PBEDirectoryFieldEditor extends DirectoryFieldEditor {
	static final Utils utils=Utils.getSingleton();
	IPBEFieldValidator validator; 
	
	public PBEDirectoryFieldEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(fieldDef.getAttribute("name"),fieldDef.getAttribute("label"),parent);
		this.validator=v;
		getLabelControl().setToolTipText(fieldDef.getAttribute("help"));
		setErrorMessage(fieldDef.getAttribute("label")+" must be a Valid Directory");
	}
//		public PBEDirectoryFieldEditor(String name, String labelText, String tooltipText, Composite parent, PBEValidator validator) {
//			super(name,labelText,parent);
//			setErrorMessage(labelText+" must be a Valid Directory");
//			getLabelControl().setToolTipText(tooltipText);
//			this.validator=validator;
////			setChangeButtonText("Browse..");
//		}
		protected void createControl(Composite parent) {
//			utils.log("createControl");
			setValidateStrategy(VALIDATE_ON_KEY_STROKE);
			super.createControl(parent);
		}
		protected boolean doCheckState() {
			boolean valid=true;
			if (this.presentsDefaultValue()) {}
//			else if (getStringValue().length()==0) {doLoadDefault();}
			else {valid=super.doCheckState(); if (false & !valid) {setFocus();}; }
//			utils.log("Setting focus on Dir="+valid);
			return super.doCheckState();
		}
		public Text getTextControl(Composite parent) {
			Text textField = super.getTextControl(parent);
			textField.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					refreshValidState();
				}
				public void focusLost(FocusEvent e) {
//					valueChanged();
//					clearErrorMessage();
				}
			});
			return textField;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.jface.preference.StringButtonFieldEditor#changePressed()
		 */
		protected String changePressed() {
			return PBEDialog.utils.askForDirectory(getStringValue());
		}
		
		
	static String copyright() { return Copyright.PV_COPYRIGHT; }
	}