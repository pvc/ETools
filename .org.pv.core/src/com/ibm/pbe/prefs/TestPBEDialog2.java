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
import java.util.Iterator;
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
import org.w3c.dom.Node;
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


public class TestPBEDialog2  {
//	implements IWorkbenchPreferencePage {

	static final Utils utils=Utils.getSingleton();
	Shell shell;
	 

	public TestPBEDialog2() {
		
	}
	public void run() {
//		IFile viewFile=utils.getFile("/PVTools/src/com/ibm/pv/prefs/TestDialog2.xml");
		IFile modelFile=utils.getFile(".EnvSelectorPattern/EnvSelectorModel.appdef");
		IFile viewFile=utils.getFile(".EnvSelectorPattern/EnvSelectorDlg.xml");
		Document view; Document model;
		Element viewRoot=(view = utils.getDoc(viewFile)).getDocumentElement();
//		Element x = utils.getFirstElementAt(viewRoot,"");
//		utils.log("Found: "+x);
//		if (true) {return;}
		
		Element modelRoot=(model = utils.getDoc(modelFile)).getDocumentElement();
		List<Element> props = utils.getChildElements(viewRoot,"property");
		for (Iterator<Element> iter = props.iterator(); iter.hasNext();) {
			Element prop=  iter.next();
			utils.log("Processing: "+prop);
			String path = prop.getAttribute("modelPath");
			Element me=utils.getFirstElementAt(modelRoot, path);
			if (me==null) {utils.log("Error - no model element found at: "+path);continue;}
			prop.setAttribute("value",me.getAttribute(prop.getAttribute("modelAttr")));
		}
//		Document d = utils.getNewDocument("properties");
//		Element root = d.getDocumentElement();
//		root.setAttribute("title", "Pattern Input Dialog");
//		root.setAttribute("label", "Please complete the fields below");
//		Element prop = utils.addElement(root, "property");
//		prop.setAttribute("name", "fred/hhh/ddd/@fred");
//		prop.setAttribute("label", "Fred Value");
//		prop.setAttribute("value", "initValue");
		
		PBEDialog dlg = new PBEDialog();
		if (Window.CANCEL==dlg.execute(view)) {return;}
		
		props = utils.getChildElements(viewRoot,"property");
		for (Iterator<Element> iter = props.iterator(); iter.hasNext();) {
			Element prop=  iter.next();
			String path = prop.getAttribute("modelPath");
			Element me=utils.getFirstElementAt(modelRoot, path);
			if (me==null) {utils.log("Error - no model element found at: "+path);continue;}
			me.setAttribute(prop.getAttribute("modelAttr"),prop.getAttribute("value"));
		}
		utils.save(model,modelFile);
//		utils.dump(view);
		utils.dump(model);
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}

