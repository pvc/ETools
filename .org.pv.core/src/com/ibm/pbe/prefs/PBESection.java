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
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.pv.core.Utils;


public class PBESection extends FieldEditor {
	static final Utils utils=Utils.getSingleton();
	ExpandableComposite ec;
	Composite client;
	IExpansionListener iel=new ExpansionAdapter() {
		public void expansionStateChanged(ExpansionEvent e) {
			ec.getShell().setSize(ec.getShell().computeSize(ec.getShell().getSize().x-8,SWT.DEFAULT));
			
		}};
	/**
	 * @param fieldEditorParent
	 * @param string
	 */
	public PBESection(Composite parent, String label,String tooltipText) {
		createControl(parent);
		ec.setText(label);
		ec.setFont(JFaceResources.getBannerFont());
		ec.clientVerticalSpacing=10;
		client=new Composite(ec,SWT.NULL);
//		GridLayout layout = new GridLayout();
//		layout.marginHeight = 0; 
//		layout.marginWidth = 0;
//		client.setLayout(layout);

		ec.setClient(client);
		ec.addExpansionListener(iel);
	}
	protected void createControl(Composite parent) { 
		super.createControl(parent); // calls doFillIntoGrid
		ec=new ExpandableComposite(parent,SWT.NULL,ExpandableComposite.TWISTIE);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
//		gd.grabExcessHorizontalSpace = true;
		ec.setLayoutData(gd);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData)ec.getLayoutData();
		gd.horizontalSpan = numColumns;
		((GridLayout)client.getLayout()).numColumns=numColumns;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite, int)
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
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
		// TODO Auto-generated method stub
		return 1;
	}
	/**
	 * @return
	 */
	public Composite getClientArea() {
		return client;
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
	}