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
package com.ibm.pbe.prefs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Control;
import org.pv.core.Utils;




public class SizingListener extends ControlAdapter {
//	implements IWorkbenchPreferencePage {

	static final Utils utils=Utils.getSingleton();
	public void controlResized(ControlEvent e) {
		Control ctl=(Control)e.widget;
		utils.log("Resize event on: "+ctl+" New Size: "+ctl.getSize()+" Computed Size: "+ctl.computeSize(SWT.DEFAULT,SWT.DEFAULT,true));
	} 
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}

