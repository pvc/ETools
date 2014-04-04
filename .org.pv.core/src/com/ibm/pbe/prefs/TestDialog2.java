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
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.SaveAsDialog;
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


public class TestDialog2 extends SaveAsDialog {
/**
	 * @param parentShell
	 */
	public TestDialog2() {
		super(utils.getShell());setShellStyle(SWT.SHELL_TRIM|SWT.MODELESS);
	}
//	implements IWorkbenchPreferencePage {
	public static final String P_PATH = "pathPreference";
	public static final String P_BOOLEAN = "booleanPreference";
	public static final String P_CHOICE = "choicePreference";
	public static final String P_STRING = "stringPreference";
	static final Utils utils=Utils.getSingleton();
	PreferenceStore prefs = new PreferenceStore();
	List labels=new ArrayList();
	List names=new ArrayList();
	List types=new ArrayList();
//	Properties labels=new Properties();


/**
 * Sets the default values of the preferences.
 */
	private void initializeDefaults() {
//		IPreferenceStore store = getPreferenceStore();
//		store.setDefault(P_BOOLEAN, true);
//		store.setDefault(P_CHOICE, "choice2");
//		store.setDefault(P_STRING, "Default value");
	}
	
	public void run() {
		
//		Shell wbShell=utils.getShell();
//		TitleAreaDialog dlg = new TitleAreaDialog(wbShell);
//		SaveAsDialog dlg = new SaveAsDialog(wbShell);
//		dlg.setTitle("MyTitle*******************************");
		
//		dlg.setTitleAreaColor(new RGB(50,50,50));
//		dlg.setMessage("Message",1);
//		dlg.setErrorMessage("new Error");
		int result=this.open();
		utils.log("Result="+result);
		utils.log(this.getResult());
		if (result==Window.OK) {}
		if (true) {return ;}
		
		boolean valid=false;IFile docFile=null;Element root=null;Document doc=null;
		IResource res=utils.getSelectedResource();
	}
	
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
//		setTitle("My Title");
//		this.getShell().setText("My Shell");
//		this.setMessage("my Message",2);
//		this.setErrorMessage("My Error Message");
		return contents;
	}
	protected Control createDialogArea(Composite parent) {
		Composite da=(Composite)super.createDialogArea(parent);
		utils.log("Children="+da.getChildren().length);
		Composite main =(Composite) ((Composite)da.getChildren()[1]).getChildren()[0];
		utils.log(main.getFont());
		Text t = new Text(main,SWT.BORDER);
		t.setText("****************************");
		t.setFont(main.getFont());
		GridData ld = new GridData();
//		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
//		ld.horizontalIndent=20;
		ld.horizontalAlignment=GridData.FILL_HORIZONTAL;
		ld.widthHint=250;
		t.setLayoutData(data);
		utils.log("Marginwidth="+((GridLayout)main.getParent().getLayout()).marginWidth);
		return da;
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
