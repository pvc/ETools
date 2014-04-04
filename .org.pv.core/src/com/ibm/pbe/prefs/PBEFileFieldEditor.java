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

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.pv.core.Utils;
import org.w3c.dom.Element;


public class PBEFileFieldEditor extends FileFieldEditor {
	
//	{
//		setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
//		utils.log("preconstructor executed");
//	}
	
	static final Utils utils=Utils.getSingleton();
	IPBEFieldValidator validator; 
	
	public PBEFileFieldEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(fieldDef.getAttribute("name"),fieldDef.getAttribute("label"),parent);
		this.validator=v;
		getLabelControl().setToolTipText(fieldDef.getAttribute("help"));
		setErrorMessage(fieldDef.getAttribute("label")+" must be a valid File");
		setChangeButtonText(JFaceResources.getString("openBrowse"));
		
	}
	
//		public PBEFileFieldEditor(String name, String labelText,String tooltipText,Composite parent, PBEValidator validator) {
//			super(name,labelText,parent);
//			this.validator=validator;
////			setErrorMessage(JFaceResources.getString("FileFieldEditor.errorMessage"));//$NON-NLS-1$
//			setErrorMessage(labelText+" must be a valid File");//$NON-NLS-1$
//			setChangeButtonText(JFaceResources.getString("openBrowse"));//$NON-NLS-1$
//			getLabelControl().setToolTipText(tooltipText);
////			getChangeControl(parent).addControlListener(new SizingListener());
////		utils.log("constructor executing");
////			super(name,labelText,StringFieldEditor.VALIDATE_ON_KEY_STROKE,parent);
////			setChangeButtonText("Browse..");
//		}
		protected void createControl(Composite parent) {
//			utils.log("createControl");
			setValidateStrategy(VALIDATE_ON_KEY_STROKE);
			super.createControl(parent);
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
		protected boolean doCheckState() {
//			utils.log("docheckstate!");
//			if (getStringValue().length()==0) {doLoadDefault();}
			return true;
		}
		protected void refreshValidState() {
//			utils.log("Refresh valid state");
			super.refreshValidState();
		}
		protected void valueChanged() {
//			utils.log("value changed");
			super.valueChanged();
		}
		/* (non-Javadoc)
		 * @see org.eclipse.jface.preference.StringButtonFieldEditor#changePressed()
		 */
		protected String changePressed() {
			return PBEDialog.utils.askForFile(getStringValue());
		}
		protected boolean checkState() {
			boolean valid=super.checkState();
//			utils.log("Setting focus on File="+valid);
//			if (!valid) {setFocus();}
//			clearErrorMessage();
			return valid;
		}
		
	static String copyright() { return Copyright.PV_COPYRIGHT; }
	}