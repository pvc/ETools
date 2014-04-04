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
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.pv.core.Utils;
import org.w3c.dom.Element;


public class PBEMLEEditor extends FieldEditor {
	static final Utils utils=Utils.getSingleton();
	Text textField;
	String lastValue;
	IPBEFieldValidator validator; 
	
	public PBEMLEEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(fieldDef.getAttribute("name"),fieldDef.getAttribute("label"),parent);
		this.validator=v;
		getLabelControl().setToolTipText(fieldDef.getAttribute("help"));
	}
	
//	public PBEMLEEditor(String name, String labelText,String tooltipText, Composite parent, PBEValidator validator) {
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
	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns(int numColumns) {
//		utils.log("adjustForNumColumns:"+numColumns);
		Label label =getLabelControl();
		GridData gd = new GridData();
		gd.horizontalSpan=numColumns;
		label.setLayoutData(gd);
		gd = new GridData();
		gd.horizontalSpan = numColumns;
		gd.heightHint=50;
			gd.horizontalAlignment = GridData.FILL;
			gd.grabExcessHorizontalSpace = true;
		textField.setLayoutData(gd);
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
		
		textField = new Text(parent, SWT.MULTI | SWT.BORDER |SWT.V_SCROLL | SWT.H_SCROLL);
//		gd = new GridData();
//		gd.horizontalSpan = numColumns+2;
//		gd.heightHint=50;
//			gd.horizontalAlignment = GridData.FILL;
//			gd.grabExcessHorizontalSpace = true;
//		textField.setLayoutData(gd);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	protected void doLoad() {
		if (textField != null) {
			String value = getPreferenceStore().getString(getPreferenceName());
			textField.setText(value);
			lastValue = value;
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault() {
		if (textField != null) {
			String value = getPreferenceStore().getDefaultString(getPreferenceName());
			textField.setText(value);
		}
//		valueChanged();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore() {
		getPreferenceStore().setValue(getPreferenceName(), textField.getText());
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {
		return 3;
	}
	
//	public Composite getButtonBoxControl(Composite parent) {
//		if (buttonBox == null) {
//			buttonBox = new Composite(parent, SWT.NULL);
//			GridLayout layout = new GridLayout();
//			layout.marginWidth = 0;
//			buttonBox.setLayout(layout);
//			createButtons(buttonBox);
//			buttonBox.addDisposeListener(new DisposeListener() {
//				public void widgetDisposed(DisposeEvent event) {
//					addButton = null;
//					removeButton = null;
//					upButton = null;
//					downButton = null;
//					buttonBox = null;
//				}
//			});
//
//		} else {
//			checkParent(buttonBox, parent);
//		}
//
//		selectionChanged();
//		return buttonBox;
//  }
}	