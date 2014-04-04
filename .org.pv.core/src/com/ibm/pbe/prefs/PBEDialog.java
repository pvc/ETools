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
//*****************************************************************************
//* Licensed Materials - property of IBM
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
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class PBEDialog extends TitleAreaDialog implements IPreferencePageContainer{
	static final Utils utils=Utils.getSingleton();
	protected IPreferenceStore prefs = new PreferenceStore();
//	List labels=new ArrayList();
//	List<String> names=new ArrayList<String>();
//	List types=new ArrayList();
//	List tooltips=new ArrayList();
	PBEPage page;
	List<Element> fields;
	String title; String description;
	Label testLabel;
	IPBEFieldValidator validator;
	public IFieldEditorProvider fieldEditorProvider;
	
	public PBEDialog() {super(utils.getShell());setShellStyle(SWT.SHELL_TRIM|SWT.MODELESS);}
	
	public void run() {
		IResource res=utils.getSelectedResource();
		Document doc=execute(res);
		if (true) {return;}
	}
	public Document execute(IResource res) {
		boolean valid=false;IFile docFile=null;Document doc=null;
//		res=utils.getFile(new Path("PVTools/src/com/ibm/pv/prefs/AWDialog.xml"));
		res=utils.getFile(new Path("PVTools/NewSWDialog.xml"));
		if (res!=null && utils.isFile(res)) {valid=true;}
		if (!valid) {utils.log("Please select a valid input dialog file"); return null;}
		docFile=(IFile)res;
		doc = utils.getDoc(docFile);
		int rc=execute(doc);
		if ( (doc!=null) && (rc==Window.OK) ) {utils.save(doc,docFile);}
		return doc;
	}
	
	public int execute(Element viewRoot,Element modelRoot, IPBEFieldValidator v)  {
		this.validator=v;
		fields = utils.getChildElements(viewRoot,"property");
		for (Iterator<Element> iter = fields.iterator(); iter.hasNext();) {
			Element field=  iter.next();
			utils.log("Processing: "+field.getAttribute("name"));
			String path = field.getAttribute("modelPath");
			if (path.length()==0) {path="//";}
			Element me=utils.getFirstElementAt(modelRoot, path);
			utils.log("Model elt="+me.getNodeName());
//			utils.log(me.getLocalName()+":"+field.getAttribute("modelAttr"));
//			utils.log(me.getLocalName()+":"+me.getAttribute("interface"));
			if (me==null) {utils.log("Error - no model element found at: "+path);continue;}
			String modelAttr=field.getAttribute("modelAttr");
			if (modelAttr.length()==0) {modelAttr=field.getAttribute("name");}
			utils.log("Retrieving Model value: "+me.getAttribute(modelAttr));
			field.setAttribute("value",me.getAttribute(modelAttr));
		}
		
		int result=execute(viewRoot.getOwnerDocument());
		
		for (Iterator<Element> iter = fields.iterator(); iter.hasNext();) {
			Element field=  iter.next();
			String path = field.getAttribute("modelPath");
			if (path.length()==0) {path="//";}
			Element me=utils.getFirstElementAt(modelRoot, path);
			if (me==null) {utils.log("Error - no model element found at: "+path);continue;}
			String modelAttr=field.getAttribute("modelAttr");
			if (modelAttr.length()==0) {modelAttr=field.getAttribute("name");}
			me.setAttribute(modelAttr,field.getAttribute("value"));
		}
		return result;
	}
	
	public int execute(Document doc)  {
		boolean valid=false;Element root=null;
		if (doc!=null) {
			root=doc.getDocumentElement();
			if (root.getTagName().equals("properties")) {valid=true;}
		}
		if (!valid) {utils.log("Please provide a valid input dialog document"); return Window.CANCEL;}
		
		title=root.getAttribute("title");
		description=root.getAttribute("label");
		fieldEditorProvider=new FieldEditorProvider(this.validator,null); //default
		fields = utils.getChildElements(root, "property");
		for (Element field : fields) {
			String name=field.getAttribute("name");
//			names.add(name);
//			types.add(field.getAttribute("type"));
			prefs.setDefault(name,field.getAttribute("default"));
			if (field.getAttributeNode("value")!=null) {prefs.setValue(name,field.getAttribute("value"));}
//			if (field.getAttribute("label")!=null) {labels.add(field.getAttribute("label"));} else {labels.add(name);}
			if (field.getAttributeNode("label")==null) {field.setAttribute("label","Enter value for "+field.getAttribute("name"));}
//			tooltips.add(field.getAttribute("help").replaceAll("\\\\n","\n"));
		}
//		utils.log("Opening");
		int rc = this.open();
//		utils.log("Returned from open");
//		prefs.list(utils.getLogger());
		if (rc==Window.OK){
			for (Element field : fields) {
//				String name=field.getAttribute("name");
				field.setAttribute("value",prefs.getString(field.getAttribute("name")));
			}
//			doc.getDocumentElement().setAttribute("continue", "true");
		} 
//		else {doc.getDocumentElement().setAttribute("continue", "false");}
		return rc;
	}

/* Dialog Layout Methods */ 
	public void createButtonsForButtonBar(Composite parent){
//		Button db = createButton(parent,98,"Defaults",false);
//		Button ab = createButton(parent,99,"Apply",false);
		super.createButtonsForButtonBar(parent);
	}
	public void buttonPressed(int id) {
//		utils.log("Button pressed="+id);
		super.buttonPressed(id);
		page.perform(id);
	}
	public void okPressed() {
//		utils.log("ok Button pressed");
		page.performOk();
		super.okPressed();
	}
	public Control createContents(Composite parent) {
//		utils.log("creating contents");
		Control c = super.createContents(parent);
		page=new PBEPage();
		page.setContainer(this);
		page.createControl((Composite)this.getDialogArea());
		((GridLayout)((Composite)page.getControl()).getLayout()).marginWidth=10;
		Composite main = (Composite)page.getControl();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalIndent=10;
//		gd.marginWidth=20;
		main.setLayoutData(gd);
//		updateButtons();  // workaround - FEPP cannot invoke during widget tree creation
		page.setVisible(true);
		return c;
	}
	public Control createDialogArea(Composite parent) {
//		utils.log("DialogArea...");
		Composite da = (Composite) super.createDialogArea(parent);
//		GridData gd = (GridData)da.getLayoutData();
//		utils.log("Grab="+gd.grabExcessHorizontalSpace);
		
//		GridData ld = new GridData();
//		ld.horizontalIndent=20;
//		ld.horizontalAlignment=GridData.FILL_HORIZONTAL;
//		da.setLayoutData(ld);
//		utils.log("Bg="+da.getBackground());
//		Composite da2=new Composite(da,0); 
//		GridLayout gd = new GridLayout();
//		da2.setLayout(gd);
//		gd.marginWidth=20;
		
		
//		ld.horizontalIndent=20;
//		Text t = new Text(da,SWT.BORDER);
//		t.setText("****************************");
//		
//		GridData data = new GridData(GridData.FILL_HORIZONTAL);
//		t.setLayoutData(data);
		
		return da;
	}

	
	public class PBEPage extends FieldEditorPreferencePage {
		FieldEditor invalidFieldEditor=null;
//		List fields = null;
		Composite fieldEditorParent;
		
		public PBEPage() {
			super(GRID); 
			noDefaultAndApplyButton();
		}
		protected Composite getFieldEditorParent() {
			if (fieldEditorParent==null) {return super.getFieldEditorParent();} else {return fieldEditorParent;}
		}
		/**
		 * @param id
		 */
		public void perform(int id) {
//			utils.log("Page button id pressed:"+id);
			if (id==IDialogConstants.OK_ID) {performOk();}
			if (id==IDialogConstants.CANCEL_ID) {performCancel();}
			if (id==98) {performDefaults();}
			if (id==99) {performApply();}
			
		}
		

//		protected void checkState() {
//			boolean valid = true;
//			invalidFieldEditor = null;
//			utils.log("Checvking state - fields="+fields);
//			// The state can only be set to true if all
//			// field editors contain a valid value. So we must check them all
//			if (fields != null) {
//				int size = fields.size();
//				for (int i = 0; i < size; i++) {
//					FieldEditor editor = (FieldEditor) fields.get(i);
//					valid = valid && editor.isValid();
//					if (!valid) {
//						invalidFieldEditor = editor;
//						break;
//					}
//				}
//			}
//			setValid(valid);
//		}

		public void initialize() {
			setDescription(description);
			setTitle(title);
			super.initialize();
		}

		protected void createFieldEditors() {
			
//			utils.log("starting createcontrols");
//			Composite fep = getFieldEditorParent();
//			fep.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//			PBEStringFieldEditor sfe = new PBEStringFieldEditor("fred","fredLabel",getFieldEditorParent());
//			addField(sfe);
//			Text tc = sfe.getTextControl(getFieldEditorParent());
//			GridData gd = (GridData)tc.getLayoutData();
//			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//			utils.log("GrabText="+gd.grabExcessHorizontalSpace);
//			tc.setLayoutData(gd);
//			utils.log("Done createcontrols");
//			if (true) {return;}
//			utils.log("FEP:"+getFieldEditorParent().getLayoutData());
//			GridData gd = (GridData)getFieldEditorParent().getLayoutData();
//			utils.log("GrabFEP="+gd);
//			GridData gd=(GridData)getFieldEditorParent().getLayoutData();
//			getFieldEditorParent().setLayoutData(gd);
			for (Element field : fields) {
				FieldEditor fe=fieldEditorProvider.getFieldEditor(field,getFieldEditorParent());
				if (fe!=null) {
					addField(fe);
					if (fe instanceof PBESection) {
						fieldEditorParent=((PBESection)fe).getClientArea();
					}
				}
			}
//			for (int i = 0; i < names.size(); i++) {
//				String type=(String)types.get(i);
//				if (type.length()==0) {addField(new PBEStringFieldEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i),getFieldEditorParent(),validator));}
//				if (type.equals("folder")) {addField(new PBEDirectoryFieldEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i), getFieldEditorParent(),validator));}
//				if (type.equals("stringList")) {addField(new PBEStringListEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i), getFieldEditorParent(),validator));}
//				if (type.equals("combo")) {addField(new PBEComboEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i), getFieldEditorParent(),validator));}
//				if (type.equals("dropDown")) {addField(new PBEDropDownEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i), getFieldEditorParent(),validator));}
//				if (type.equals("file")) {addField(new PBEFileFieldEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i), getFieldEditorParent(),validator));}
//				if (type.equals("wsResource")) {addField(new PBEResourceEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i), getFieldEditorParent(),validator));}
//				if (type.equals("string")) {addField(new PBEStringFieldEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i), getFieldEditorParent(),validator));}
//				if (type.equals("boolean")) {addField(new PBECheckBoxEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i), getFieldEditorParent(),validator));}
//				if (type.equals("pathList")) {addField(new PBEPathListEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i), "Select a folder to be added to the list",getFieldEditorParent(),validator));}
//				if (type.equals("resourceList")) {addField(new PBEResourceListEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i),getFieldEditorParent(),validator));}
//				if (type.equals("separator")) {addField(new PBESeparator(getFieldEditorParent(),(String)labels.get(i),(String)tooltips.get(i)));}
//				if (type.equals("label")) {addField(new PBESeparator(getFieldEditorParent(),(String)labels.get(i),(String)tooltips.get(i),false));}
//				if (type.equals("text")) {addField(new PBEMLEEditor((String)names.get(i),(String)labels.get(i),(String)tooltips.get(i),getFieldEditorParent(),validator));}
//				if (type.equals("section")) {
//					PBESection section=new PBESection(getFieldEditorParent(),(String)labels.get(i),(String)tooltips.get(i));
//					addField(section);
//					fieldEditorParent=section.getClientArea();
//					}
//			}
		}
		protected void showMessage(String msg) {
			if (page != null) 
				showMessage(msg);
		}
		
	}
	
/* IPreferencePageContainer Methods */
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		return prefs;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateButtons()
	 */
	public void updateButtons() {
//		utils.log("Button is:"+getButton(IDialogConstants.FINISH_ID));
//		utils.log("Updating buttons with Page valid:"+page.isValid());
		Button ok = getButton(IDialogConstants.OK_ID);
//		utils.log("ok button="+ok);
		if (ok!=null) {ok.setEnabled(page.isValid());}
//		page.setVisible(true);
//		getButton(99).setEnabled(page.isValid());  //Change literal id

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateMessage()
	 */
	public void updateMessage() {
		String msg=page.getErrorMessage();
//		utils.log("Update message with:"+msg);
		setErrorMessage(msg);
		if (msg==null) {
//			page.checkState();
//			utils.log("Setting visible");
//			page.setVisible(true);
		}
		msg=page.getMessage();
		if (msg!=null) {setMessage(msg,page.getMessageType());} else {setMessage(description);}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateTitle()
	 */
	public void updateTitle() {
		setTitle(page.getTitle());
		getShell().setText(page.getTitle());
		setMessage(page.getDescription());
				
	}
	public IPreferenceStore getPrefs() {
		return prefs;
	}
	/**
	 * @param prefs The prefs to set.
	 */
	public void setPrefs(PreferenceStore prefs) {
		this.prefs = prefs;
//		setPreferenceStore(prefs);
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }

	public IPBEFieldValidator getValidator() {
		return validator;
	}

	public void setValidator(IPBEFieldValidator validator) {
		this.validator = validator;
	}
}
