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
import org.eclipse.jface.dialogs.DialogMessageArea;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
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


public class CopyOfPBEDialog2 extends TitleAreaDialog implements IPreferencePageContainer{
//	implements IWorkbenchPreferencePage {
	static final Utils utils=Utils.getSingleton();
	PreferenceStore prefs = new PreferenceStore();
	List labels=new ArrayList();
	List names=new ArrayList();
	List types=new ArrayList();
	String title;
	String description="My Sample Text";
//	Properties labels=new Properties();
	PBEPage page;
	NodeList props;
	Label label;
	DialogMessageArea dma;
	private boolean pageError=false;
	private Label errText;
	private FieldEditor errField;
	
	public CopyOfPBEDialog2() {super(utils.getShell());setShellStyle(SWT.SHELL_TRIM|SWT.MODELESS);}
	
/**
 * Sets the default values of the preferences.
 */

	
	public void run() {
//		boolean valid=false;IFile docFile=null;Element root=null;Document doc=null;
		IResource res=utils.getSelectedResource();
		execute(res);
		if (true) {return;}
		utils.log("Starting run");
//		DialogSettings settings = new DialogSettings("root"); 
//		settings.put("Boolean1",true); 
//		settings.put("Long1",100); 
//		settings.put("Array1",new String[]{"aaaa1","bbbb1","cccc1"}); 
//		DialogSettings section = new DialogSettings("sectionName"); 
//		settings.addSection(section); 
//		section.put("Int2",200); 
//		section.put("Float2",1.1); 
//		section.put("Array2",new String[]{"aaaa2","bbbb2","cccc2"}); 
//		try {
//			String fn="C:/MyWorkspaces/MyToolkits/PVTools/src/com/ibm/pv/prefs/Dialog.xml";
//			settings.save(fn);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace(utils.getLogger());
//		} 
		
//		prefs=new PreferenceStore();super.setPreferenceStore(prefs);

//		Composite ct = (Composite)this.createContents(getShell());
//		prefs.setFilename("C:/MyWorkspaces/MyToolkits/PVTools/prefs.txt");
//		utils.getPluginFileInputStream();
//		try {
//			prefs.load();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace(utils.getLogger());
//		}
		
		this.open();
		int rc=getReturnCode();
		utils.log("Dialog complete, rc="+rc);
		utils.dump(prefs);
//		try {
//			prefs.save();
//		} catch (IOException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace(utils.getLogger());
//		}
	}
//	protected void configureShell(Shell shell) {
//		utils.log("Configuring shell");
//		super.configureShell(shell);
//		if (title != null)
//			shell.setText(title);
//	}
	
	public Control createContents(Composite parent) {
		utils.log("creating contents");
		Control c = super.createContents(parent);
		updateButtons();  // workaround - FEPP cannot invoke during widget tree creation
		return c;
	}
	public void createButtonsForButtonBar(Composite parent){
		Button db = createButton(parent,98,"Defaults",false);
		Button ab = createButton(parent,99,"Apply",false);
		super.createButtonsForButtonBar(parent);
	}
	public void buttonPressed(int id) {
		utils.log("Button pressed="+id);
		super.buttonPressed(id);
		page.perform(id);
	}
	public Control createDialogArea(Composite parent) {
		Composite da = (Composite) super.createDialogArea(parent);
		page=new PBEPage();
		page.setContainer(this);
		page.createControl(da);
		return da;
	}
	public Document execute(IResource res) {
		boolean valid=false;IFile docFile=null;Document doc=null;
		if (res!=null && utils.isFile(res)) {valid=true;}
		if (!valid) {utils.log("Please select a valid input dialog file"); return null;}
		docFile=(IFile)res;
		doc = utils.getDoc(docFile);
		doc=execute(doc);
		utils.save(doc,docFile);
		return doc;
	}
	
	public Document execute(Document doc)  {
		boolean valid=false;Element root=null;
		if (doc!=null) {
			root=doc.getDocumentElement();
			if (root.getTagName().equals("properties")) {valid=true;}
		}
		if (!valid) {utils.log("Please provide a valid input dialog document"); return null;}
		
		title=root.getAttribute("title");
		description=root.getAttribute("label");
		props = root.getElementsByTagName("property");
		for (int i = 0; i < props.getLength(); i++) {
			Element prop = (Element)props.item(i);
			String name=prop.getAttribute("name");
			names.add(name);
			types.add(prop.getAttribute("type"));
			prefs.setDefault(name,prop.getAttribute("default"));
			if (prop.getAttributeNode("value")!=null) {prefs.setValue(name,prop.getAttribute("value"));}
			if (prop.getAttributeNode("label")!=null) {labels.add(prop.getAttribute("label"));} else {labels.add(name);}
		}
		utils.log("Opening");
		this.open();
		utils.log("Returned from open");
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


	
//	public void init(IWorkbench workbench) {
//	}
//	
//	public void executeDialog(Shell shell) {
//	shell.open();
//	Display display = shell.getDisplay();
//	while (!shell.isDisposed()) {
//		if (!display.readAndDispatch()) display.sleep();
//	}
//	}
//	/**
//	 * @return Returns the prefs.
//	 */
//	public PreferenceStore getPrefs() {
//		return prefs;
//	}
//	/**
//	 * @param prefs The prefs to set.
//	 */
//	public void setPrefs(PreferenceStore prefs) {
//		this.prefs = prefs;
//		setPreferenceStore(prefs);
//	}
	
	public class PBEPage extends FieldEditorPreferencePage {
		DirectoryFieldEditor de;
		public PBEPage() {
			super(GRID); 
//			setPreferenceStore(prefs);
			noDefaultAndApplyButton();

		}
		/**
		 * @param id
		 */
		public void perform(int id) {
			if (id==98) {performDefaults();}
			if (id==99) {performApply();}
		}
		public void updateApplyButton() {
			utils.log("Applybutton called with page: "+isValid());
			super.updateApplyButton();
		}
		public void checkState() {
			utils.log("page Checkstate called");
			super.checkState();
		}
		public void initialize() {
			utils.log("Initalising");
			setDescription("Page description");
			setTitle("Page Title");
			super.initialize();
		}
		public void setValid(boolean b) {
			utils.log("Page setvalid clled with: "+b);
			super.setValid(b);
		}
		public void setErrorMessage(String msg) {
			utils.log("Page SetErrorMsg called with: "+msg);
			super.setErrorMessage(msg);
		}
		public void setMessage(String msg, int type) {
			utils.log("Page Set	Msg called with: "+msg+" type:"+type);
			super.setMessage(msg,type);
		}
		public void propertyChange(PropertyChangeEvent event) {
//			utils.log(event.getProperty());
//			utils.log(FieldEditor.IS_VALID);
			Object osrc=event.getSource();
//			FieldEditor errField=null; 
			if (event.getProperty().equals(FieldEditor.IS_VALID)) {
			if (osrc instanceof FieldEditor) {errField=(FieldEditor)osrc;}
//			errText.setText(errField.getLabelText());
			utils.log("Property Change on: "+errField.getLabelText());
			}
			super.propertyChange(event);
		}
		protected void createFieldEditors() {
			for (int i = 0; i < names.size(); i++) {
				String type=(String)types.get(i);
				if (type.length()==0) {addField(new StringFieldEditor((String)names.get(i),(String)labels.get(i), getFieldEditorParent()));}
				if (type.equals("folder")) {addField(new DirectoryFieldEditor((String)names.get(i),(String)labels.get(i), getFieldEditorParent()));}
				if (type.equals("file")) {addField(new FileFieldEditor((String)names.get(i),(String)labels.get(i), getFieldEditorParent()));}
				if (type.equals("string")) {addField(new StringFieldEditor((String)names.get(i),(String)labels.get(i), getFieldEditorParent()));}
				if (type.equals("pathList")) {addField(new PathEditor((String)names.get(i),(String)labels.get(i), "Select a folder to be added to the list",getFieldEditorParent()));}
			}
			de=new DirectoryFieldEditor("fred","Test entry          ", getFieldEditorParent());
			addField(de);
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
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		// TODO Auto-generated method stub
		return prefs;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateButtons()
	 */
	public void updateButtons() {
//		utils.log("Called Update Buttons with page: "+page.isValid());
		getButton(IDialogConstants.OK_ID).setEnabled(page.isValid());
		getButton(99).setEnabled(page.isValid());
//		if (page.isValid()) {errText.setVisible(false);}
//		page.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateMessage()
	 */
	public void updateMessage() {
		String msg=page.getErrorMessage();
		if (msg!=null) {setErrorMessage(msg);} 
		else {if (page.isValid()) {setErrorMessage(null);}}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateTitle()
	 */
	public void updateTitle() {
		setTitle(page.getTitle());
		getShell().setText(page.getTitle());
		setMessage(page.getDescription());
				
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
