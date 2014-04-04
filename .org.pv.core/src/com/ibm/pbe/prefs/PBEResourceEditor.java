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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IContributorResourceAdapter;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.pv.core.Utils;
import org.w3c.dom.Element;

//import com.ibm.pbe.core.P4ebRSAUtils;
import com.ibm.pbe.prefs.PBEResourceListEditor.Listener;

public class PBEResourceEditor extends PBEStringFieldEditor {
	static final String SELECTVALID="Please select a valid workspace resource";
	static final String SELECTFILE="Please select a workspace FILE";
	static final String SELECTCONTAINER="Please select a workspace FOLDER or PROJECT";
	static final String SELECTPROJECT="Please select a workspace PROJECT";
	static final String SELECTFOLDERNOTPROJECT="Please select a workspace Folder (NOT a Project)";
	static final String MUSTEXIST="This resource does not exist - Please create it or correct your entry";
	static final String MUSTNOTEXIST="This resource already exists - Please delete it or correct your entry";
	boolean mustExist=false;
	boolean mustNotExist=false;
	
	static final int ANY=0;
	static final int FILE=1;
	static final int CONTAINER=2; //includes Project
	static final int PROJECT=3; 
	static final int FOLDERNOTPROJECT=4; 
	int mustBeResourceType=ANY;
	
	Listener listener;
	Text textField; 
	String errorMessage="Please select a valid workspace resource";
//	String labelText;
//	{
//		setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
//		utils.log("preconstructor executed");
//	}
	
	static final Utils utils=Utils.getSingleton();
//	IPBEFieldValidator validator; 
	
	public PBEResourceEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(parent,fieldDef,v,init);
		if ("true".equals(fieldDef.getAttribute("mustExist"))) {mustExist=true;}
		if ("true".equals(fieldDef.getAttribute("mustNotExist"))) {mustNotExist=true;}
		listener=new Listener(this);
		textField.addFocusListener(listener);
		utils.getActivePage().addSelectionListener(listener);
	}
//	public PBEResourceEditor(String name, String labelText,String tooltipText,Composite parent, PBEValidator validator) {
//			super(name,labelText,tooltipText,parent,validator);
//			this.validator=validator;
//			this.labelText=labelText;
//			listener=new Listener(this);
//			textField.addFocusListener(listener);
//			utils.getActivePage().addSelectionListener(listener);
//		}
		// This check determines whether the selected resource will be allowed into the field
//		private boolean validSelection(IResource res) {
////			utils.log("Validating: "+res+ utils.isFile(res)+" "+res.getName());
////			if ( (!utils.isFile(res)) || (!"appdef".equals(((IFile)res).getFileExtension())) ) {return false;}
//			String msg=validator.doSelect(this, res);
//			if (msg==null) {return true;} else {setErrorMessage(msg); return false;}
//		}
		// This check is on the content of the field (user may be typing in)
		protected boolean doCheckState() {
			boolean accept=super.doCheckState();
			if (accept) {
				String val=getStringValue();
				if (val.length()==0) {return true;}
				else {
					if (val.charAt(0)=='/') {val=val.substring(1);setStringValue(val);}
					//			if (!getStringValue().endsWith(".appdef")) {return false;}
					//			utils.log("Validating to true");
					if (mustNotExist || mustExist && val.length()>0) {
						IPath path=new Path(val).makeAbsolute();
						boolean exists=utils.wsr.exists(path);
						utils.log("exists="+exists+": mustExist="+mustExist);
						if (mustExist && !exists) {msg= MUSTEXIST; }
						else if (mustNotExist && exists) {msg= MUSTNOTEXIST;}
					}
					if ((msg==null) && validator!=null) {msg=validator.doField(this);};
					//			utils.log("docheckstate returning msg: "+msg);
					if (msg==null) {return true;} else {setErrorMessage(msg); return false;}
				}
			}
			else {return false;}
		}
//		public boolean isValid() {
//			boolean v=super.isValid();
//	        utils.log("Isvalid called - returning:"+v);
//			return v;
//	    }

		public Text getTextControl(Composite parent) {
			textField = super.getTextControl(parent);
			textField.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
//					utils.log("Killing Listener");
					utils.getActivePage().removeSelectionListener(listener);
					listener=null;
				}
			});
			return textField;
		}
		public void activateHotSelect() {
			this.showMessage("HOT SELECT ACTIVATED - USE THE MOUSE TO SELECT INPUT");
		}
		public void deactivateHotSelect() {
			this.showMessage(null);
		}
		protected void showMessage(String msg) {
			getPreferencePage().setMessage(msg,IMessageProvider.INFORMATION);
		}
		
		public boolean validSelection(IResource res) {
			utils.log("Validating Selection: "+res.getFullPath().toString().substring(1));
			if ( (mustBeResourceType==FILE) && !(utils.isFile(res)) ) {setErrorMessage(SELECTFILE);return false;}
			if ( (mustBeResourceType==CONTAINER) && !(utils.isContainer(res)) ) {setErrorMessage(SELECTCONTAINER);return false;}
			if ( (mustBeResourceType==PROJECT) && !(utils.isProject(res)) ) {setErrorMessage(SELECTPROJECT);return false;}
			if ( (mustBeResourceType==FOLDERNOTPROJECT) && !(utils.isFolder(res)) ) {setErrorMessage(SELECTFOLDERNOTPROJECT);return false;}
			String msg=validator.doSelect(this, res);
			if (msg==null) {return true;} else {setErrorMessage(msg); return false;}
		}
//		public String getErrorMessage() {
//			utils.log("In get errmsg");
//			if (errorMessage == null) {
////				errorMessage = getResource("LIST_BOX_ERROR_MSG");
//				errorMessage="Field \""+labelText+"\" contains an invalid selection";//$NON-NLS-1$
//			}
//			return errorMessage;
//		}

//		protected void refreshValidState() {
//			utils.log("Refresh valid state");
//			super.refreshValidState();
//		}
//		protected void valueChanged() {
//			utils.log("value changed");
//			super.valueChanged();
//		}
		/* (non-Javadoc)
		 * @see org.eclipse.jface.preference.StringButtonFieldEditor#changePressed()
		 */
		protected String changePressed() {
			IResource res = utils.getSelectedResource();
			String s=stringify(res);
			if (s==null) {showErrorMessage(getErrorMessage());}
			textField.setFocus();
			return s;
		}
		private String stringify(IResource res) {
			String result=null;
			
			if (validSelection(res)) {
				result=res.getFullPath().toString().substring(1);
			}
			return result;
		}

//		protected boolean checkState() {
//			boolean valid=super.checkState();
//			
////			utils.log("Checking state - is:"+valid);
////			if (!valid) {setFocus();}
////			if (valid) {clearErrorMessage();}
//			return valid;
//		}
		
		
		public int getMustBeResourceType() {
			return mustBeResourceType;
		}
		public void setMustBeResourceType(int mustBeResourceType) {
			this.mustBeResourceType = mustBeResourceType;
		}
		public boolean isMustExist() {
		return mustExist;
		}
		public void setMustExist(boolean mustExist) {
		this.mustExist = mustExist;
		}
		public boolean isMustNotExist() {
			return mustNotExist;
		}
		public void setMustNotExist(boolean mustNotExist) {
			this.mustNotExist = mustNotExist;
		}
	
		public class Listener implements ISelectionListener, FocusListener, ShellListener {
			FieldEditor fe;
			private IWorkbenchPart lastPart;
			private boolean isActive=true;
			private boolean shellActive;
			private String labelText;
			
			public Listener(PBEResourceEditor fe) {
				textField.getShell().addShellListener(this);
				this.fe=fe;
				labelText=fe.getLabelText();
				} 
			/* (non-Javadoc)
			 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
			 */
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
//				utils.log("Selection changed");
				if (!isActive ) {return;}
				String result=null;
//				if (lastPart!=part) {lastPart=part; return;} //get 2 events when change pane
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection isel = (IStructuredSelection)selection;
					Object sel = isel.getFirstElement();
					if ( (sel!=null) && sel instanceof IAdaptable) {
						IResource r=(IResource)((IAdaptable)sel).getAdapter(IResource.class);
						IContributorResourceAdapter icr = (IContributorResourceAdapter)((IAdaptable)sel).getAdapter(IContributorResourceAdapter.class);
						if (icr!=null) {r = icr.getAdaptedResource((IAdaptable)sel);}
						if (r==null) {utils.log("Unadaptable class - "+sel.getClass().getName());}
						else {result=stringify(r);}
					}
				}
				if (result!=null) {
//					utils.log(""+result);
					textField.setText(result);
//					clearErrorMessage();
					((PBEResourceEditor) fe).valueChanged();
					setFocus();
				} else {showErrorMessage(getErrorMessage());}

			}


			public void focusGained(FocusEvent e) {
//				utils.log("on Focus");
				isActive=true;
				((PBEResourceEditor) fe).activateHotSelect();
//				((PBEResourceEditor) fe).valueChanged();
			}
			public void focusLost(FocusEvent e) {
//				utils.log("lost Focus - in shell="+shellActive);
				boolean leftShell=!shellActive;
				if (!leftShell) {isActive=false; }
			}
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.ShellListener#shellActivated(org.eclipse.swt.events.ShellEvent)
			 */
			public void shellActivated(ShellEvent e) {
//				utils.log("Activated - control deactivated");
				shellActive=true;
				isActive=false;
				clearErrorMessage();
			}
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.ShellListener#shellClosed(org.eclipse.swt.events.ShellEvent)
			 */
			public void shellClosed(ShellEvent e) {
				// TODO Auto-generated method stub
				
			}
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.ShellListener#shellDeactivated(org.eclipse.swt.events.ShellEvent)
			 */
			public void shellDeactivated(ShellEvent e) {
//				utils.log("Deactivated");
				shellActive=false;
				
			}
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.ShellListener#shellDeiconified(org.eclipse.swt.events.ShellEvent)
			 */
			public void shellDeiconified(ShellEvent e) {
				// TODO Auto-generated method stub
				
			}
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.ShellListener#shellIconified(org.eclipse.swt.events.ShellEvent)
			 */
			public void shellIconified(ShellEvent e) {
				// TODO Auto-generated method stub
				
			}
		}

		static String copyright() { return Copyright.PV_COPYRIGHT; }
		
	}