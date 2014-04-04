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
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
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


public class DialogPage extends FieldEditorPreferencePage {
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

	public DialogPage() {
		super(GRID);
//		setPreferenceStore(P4ebRSAToolkitPlugin.getDefault().getPreferenceStore());
		setPreferenceStore(prefs);
//		setDescription("A demonstration of a preference page implementation");
//		initializeDefaults();
	}
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
		boolean valid=false;IFile docFile=null;Element root=null;Document doc=null;
		IResource res=utils.getSelectedResource();
		execute(res);
	}
	
	public Document execute(IResource res) {
		boolean valid=false;IFile docFile=null;Document doc=null;
		utils.log("Passed: "+res);
		if (res!=null && utils.isFile(res)) {valid=true;}
		if (!valid) {utils.log("Please select a valid input dialog file"); return null;}
		docFile=(IFile)res;
		doc = utils.getDoc(docFile);
		doc=execute(doc);
		if (doc!=null) {utils.save(doc,docFile);}
		return doc;
	}
	
	public Document execute(Document doc)  {
		boolean valid=false;Element root=null;
		if (doc!=null) {
			root=doc.getDocumentElement();
			if (root.getTagName().equals("properties")) {valid=true;}
		}
		if (!valid) {utils.log("Please provide a valid input dialog document"); return null;}
		
		NodeList props = root.getElementsByTagName("property");
		for (int i = 0; i < props.getLength(); i++) {
			Element prop = (Element)props.item(i);
			String name=prop.getAttribute("name");
			names.add(name);
			types.add(prop.getAttribute("type"));
			prefs.setDefault(name,prop.getAttribute("default"));
			if (prop.getAttributeNode("value")!=null) {prefs.setValue(name,prop.getAttribute("value"));}
			if (prop.getAttribute("label").length()>0) {labels.add(prop.getAttribute("label"));} else {labels.add(name);}
		}
		
		Shell wbShell=utils.getShell();
		Shell mainShell=new Shell(wbShell, SWT.SHELL_TRIM|SWT.APPLICATION_MODAL);  // = CLOSE, RESIZE, etc
		mainShell.setText(root.getAttribute("title"));
		this.setDescription(root.getAttribute("label"));
		FillLayout fl=new FillLayout();
		fl.marginHeight=10; fl.marginWidth=10;
		mainShell.setLayout(fl);
//		mainShell.setSize(400,250);
//		utils.log("Def="+SWT.DEFAULT);
		this.createControl(mainShell);
		Point p = mainShell.computeSize(SWT.DEFAULT,SWT.DEFAULT);
		mainShell.setSize(p);
		
		executeDialog(mainShell);  
//		prefs.list(utils.getLogger());
		for (int i = 0; i < names.size(); i++) {
			Element prop = (Element)props.item(i);
//			String name=prop.getAttribute("name");
			prop.setAttribute("value",prefs.getString((String)names.get(i)));
		}
		
		return doc;
	}

/**
 * Creates the field editors. Field editors are abstractions of
 * the common GUI blocks needed to manipulate various types
 * of preferences. Each field editor knows how to save and
 * restore itself.
 */

	public void createFieldEditors() {
//		Composite canvas = getFieldEditorParent();
//		String[] names=prefs.preferenceNames();
		for (int i = 0; i < names.size(); i++) {
//			String value = prefs.getString(names[i]);
			String type=(String)types.get(i);
			if (type.length()==0) {addField(new StringFieldEditor((String)names.get(i),(String)labels.get(i), getFieldEditorParent()));}
			if (type.equals("folder")) {addField(new DirectoryFieldEditor((String)names.get(i),(String)labels.get(i), getFieldEditorParent()));}
			if (type.equals("file")) {addField(new FileFieldEditor((String)names.get(i),(String)labels.get(i), getFieldEditorParent()));}
			if (type.equals("string")) {addField(new StringFieldEditor((String)names.get(i),(String)labels.get(i), getFieldEditorParent()));}
			if (type.equals("pathList")) {addField(new PathEditor((String)names.get(i),(String)labels.get(i), "Select a folder to be added to the list",getFieldEditorParent()));}
		}
		
	}
	
	public void init(IWorkbench workbench) {
	}
	
	public void executeDialog(Shell shell) {
	shell.open();
	Display display = shell.getDisplay();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	}
	/**
	 * @return Returns the prefs.
	 */
	public PreferenceStore getPrefs() {
		return prefs;
	}
	/**
	 * @param prefs The prefs to set.
	 */
	public void setPrefs(PreferenceStore prefs) {
		this.prefs = prefs;
		setPreferenceStore(prefs);
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
