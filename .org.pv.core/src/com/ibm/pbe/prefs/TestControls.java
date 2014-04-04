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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */


public class TestControls  {
//	implements IWorkbenchPreferencePage {

	static final Utils utils=Utils.getSingleton();
	Shell shell;
	 

	public TestControls() {
		shell=new Shell(utils.getShell(),SWT.SHELL_TRIM|SWT.MODELESS);
		Display d=shell.getDisplay();
		utils.log("Display bounds="+d.getBounds());
		utils.log("Client Area="+d.getClientArea());
	}
	public void run() {
		final ExpandableComposite ec = new ExpandableComposite(shell,SWT.NULL,ExpandableComposite.COMPACT|ExpandableComposite.TWISTIE);
		final Text t=new Text(shell,SWT.MULTI);
//		ec.setExpanded(true);
		IExpansionListener iel=new IExpansionListener() {
			public void expansionStateChanging(ExpansionEvent e) {
//				utils.log("Exanding:"+((Control)e.widget).getSize()); //  e.widget not set
				utils.log("Exanding:"+ec.getSize());
			}
			public void expansionStateChanged(ExpansionEvent e) {
				utils.log("Expanded:"+ec.getSize());
				utils.log("Computed:"+ec.computeSize(SWT.DEFAULT,SWT.DEFAULT, false));
				t.setVisible(!t.isVisible());
				shell.setSize(shell.computeSize(SWT.DEFAULT,SWT.DEFAULT));
				
			}};
		ControlListener cl = new ControlListener() {

			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
			}

			public void controlResized(ControlEvent e) {
				Control ctl=(Control)e.widget;
				utils.log("resize event: "+ctl+" New Size: "+ctl.getSize());
//				if (e.getSource()==ec) {utils.log("ec size:"+ec.getSize());}
//				t.setSize(t.computeSize(SWT.DEFAULT,SWT.DEFAULT,true));
//				t.setSelection(0); t.showSelection();
			}};
		shell.addControlListener(cl);
		ec.addControlListener(cl);
		ec.addExpansionListener(iel);
		Label testLabel = new Label(ec,SWT.LEFT);
		testLabel.setText("*******************************************************");
		ec.setClient(testLabel);
		ec.setText("My Twistie");
		ec.setToolTipText("My Tooltip text");
		GridData gd=new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint=150;
		ec.setLayoutData(gd);
		utils.log("Shell: "+shell.computeSize(SWT.DEFAULT,SWT.DEFAULT));
		utils.log("ec: "+ec.computeSize(SWT.DEFAULT,SWT.DEFAULT));
		utils.log(ec.getLayout());
		ec.setSize(50,100);
		shell.setText("My Title");
//		Dialog w=new Dialog(utils.getShell());
		
		GridLayout gl = new GridLayout();
		shell.setLayout(gl);
		utils.log("Setting fixed size ");
		shell.setSize(400,250);
		t.setText("*****************\njjjjjjjjjj");
		t.setSize(100,100);
		GridData ld = new GridData(GridData.FILL_HORIZONTAL);
		ld.horizontalIndent=20;
		ld.heightHint=150;
//		ld.horizontalAlignment=GridData.FILL_HORIZONTAL;
		t.setLayoutData(ld);

//		utils.log("Def="+SWT.DEFAULT);
		
	
		Label l=new Label(shell,SWT.NULL);
		l.setText("My Text");
		l.setToolTipText("My Tooltip text\nhijoj3 3ji whiuwhow hwhhwiwhwow j\nhuwhoh3ohowho3hohhohwoehoheoheohfrnhfrnhnhjofjfo");
//		Font f=l.getFont();
//		utils.log("Font:"+f);
//		l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		utils.log("Computing size");
		Point p = shell.computeSize(SWT.DEFAULT,SWT.DEFAULT);
		utils.log("Computed:"+p);
		utils.log("Setting size ");
		shell.setSize(p);
		utils.log("Opening ...");
	
		executeDialog(shell);  
	}
	
	


	public void executeDialog(Shell shell) {
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}

