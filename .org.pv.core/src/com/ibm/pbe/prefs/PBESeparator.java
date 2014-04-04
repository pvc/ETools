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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.pv.core.Utils;
import org.w3c.dom.Element;


public class PBESeparator extends FieldEditor {
	static final Utils utils=Utils.getSingleton();
	Label line;
	Label label;
	String labelText; boolean drawLine;
	public PBESeparator(Composite parent) {
		createControl(parent);
	}
	/**
	 * @param fieldEditorParent
	 * @param string
	 */
	public PBESeparator(Composite parent, Element fieldDef) {
		this(parent,fieldDef,true);
//		this.validator=v;
	}
	/**
	 * @param fieldEditorParent
	 * @param string
	 * @param b
	 */
	public PBESeparator(Composite parent, Element fieldDef, boolean drawLine) {
		super(fieldDef.getAttribute("name"),fieldDef.getAttribute("label"),parent);
		createControl(parent);
		getLabelControl().setToolTipText(fieldDef.getAttribute("help"));
		
//		this.labelText=fieldDef.getAttribute("label");this.drawLine=drawLine;
		if ( (labelText!=null) && (labelText.length()>0)) {
			label=new Label(parent,SWT.NULL);
			label.setText(labelText);
			label.setToolTipText(fieldDef.getAttribute("help"));
			GridData gd = new GridData(GridData.VERTICAL_ALIGN_END); 
//			gd.verticalAlignment=GridData.END;  // equivalent to above - neither works!
			gd.heightHint=20;
			label.setLayoutData(gd);
			label.setFont(JFaceResources.getBannerFont());
		}
	}
	protected void createControl(Composite parent) {
		super.createControl(parent);
		if (drawLine) {
		line = new Label(parent,SWT.SEPARATOR|SWT.HORIZONTAL);
//		GridData gd = new GridData();
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
//		gd.grabExcessHorizontalSpace = true;
		line.setLayoutData(gd);
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns(int numColumns) {
		GridData gd;
		if (line!=null) {
			gd= (GridData)line.getLayoutData();
			gd.horizontalSpan = numColumns;
		}
		if (label!=null) {
			gd = (GridData)label.getLayoutData();
			gd.horizontalSpan = numColumns;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite, int)
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	protected void doLoad() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {
		utils.log("Controls to 5");
		return 5;
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
	}