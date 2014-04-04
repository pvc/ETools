package com.ibm.pbe.prefs;
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
////*****************************************************************************
////* Licensed Materials - Property of IBM
////*
////* com.ibm.sal.rapidsat
////*
////* (C) Copyright IBM Corp. 2007
////*
////* US Government Users Restricted Rights - Use, duplication or
////* disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
////*****************************************************************************
///*
// * Created on 16-Jul-2007
// *
// * TODO To change the template for this generated file go to
// * Window - Preferences - Java - Code Style - Code Templates
// */
//package com.ibm.pbe.core;
//
//import org.eclipse.core.resources.IFile;
//import org.eclipse.core.resources.IResource;
//import org.eclipse.core.runtime.IAdaptable;
//import org.eclipse.jface.dialogs.IMessageProvider;
//import org.eclipse.jface.preference.FieldEditor;
//import org.eclipse.jface.preference.FileFieldEditor;
//import org.eclipse.jface.preference.StringButtonFieldEditor;
//import org.eclipse.jface.preference.StringFieldEditor;
//import org.eclipse.jface.resource.JFaceResources;
//import org.eclipse.jface.viewers.ISelection;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.swt.events.DisposeEvent;
//import org.eclipse.swt.events.DisposeListener;
//import org.eclipse.swt.events.FocusAdapter;
//import org.eclipse.swt.events.FocusEvent;
//import org.eclipse.swt.events.FocusListener;
//import org.eclipse.swt.events.ShellEvent;
//import org.eclipse.swt.events.ShellListener;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.List;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.ui.ISelectionListener;
//import org.eclipse.ui.IWorkbenchPart;
//
////import com.ibm.pbe.core.P4ebRSA7Utils;
//import com.ibm.pbe.core.P4ebRSA7Utils;
//import com.ibm.pbe.core.PBEResourceListEditor.Listener;
//
//public class PBEResourceEditor2 extends PBEStringFieldEditor {
//	static final String SELECTVALID="Please select a valid workspace resource";
//	static final String SELECTFILE="Please select a workspace FILE";
//	static final String SELECTCONTAINER="Please select a workspace FOLDER or PROJECT";
//	static final String SELECTPROJECT="Please select a workspace PROJECT";
//	static final String SELECTFOLDERNOTPROJECT="Please select a workspace Folder (NOT a Project)";
//	static final String MUSTEXIST="This resource does not exist - Please create it or correct your entry";
//	static final String MUSTNOTEXIST="This resource already exists - Please delete it or correct your entry";
//	
//	static final int ANY=0;
//	static final int FILE=1;
//	static final int CONTAINER=2; //includes Project
//	static final int PROJECT=3; 
//	static final int FOLDERNOTPROJECT=4; 
//	int mustBeResourceType=ANY;
//	
//	boolean mustExist=true;
//	boolean mustNotExist=false;
//	
//	Listener listener;
//	Text textField; 
//	String errorMessage="Please select a valid workspace resource";
//	String labelText;
////	{
////		setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
////		utils.log("preconstructor executed");
////	}
//	
//	static final P4ebRSA7Utils utils=P4ebRSA7Utils.getSingleton();
//		public PBEResourceEditor2(String name, String labelText,String tooltipText,Composite parent) {
//			super(name,labelText,tooltipText,parent);
//			this.labelText=labelText;
//			listener=new Listener(this);
//			textField.addFocusListener(listener);
//			utils.getActivePage().addSelectionListener(listener);
//		}
//
//		public Text getTextControl(Composite parent) {
//			textField = super.getTextControl(parent);
//			textField.addDisposeListener(new DisposeListener() {
//				public void widgetDisposed(DisposeEvent event) {
////					utils.log("Killing Listener");
//					utils.getActivePage().removeSelectionListener(listener);
//					listener=null;
//				}
//			});
//			return textField;
//		}
//		public void activateHotSelect() {
//			this.showMessage("HOT SELECT IS ACTIVE FOR:   "+getLabelText());
//		}
//		public void deactivateHotSelect() {
//			this.showMessage(null);
//		}
//		protected void showMessage(String msg) {
//			getPreferencePage().setMessage(msg,IMessageProvider.INFORMATION);
//		}
//
//		protected boolean doCheckState() {
//			String s=getStringValue();
//			utils.log("docheckstate for: "+s);
//			if (s.length()==0) {setErrorMessage(SELECTVALID);return false;}
//			IResource r=utils.wsr.findMember(s);
//			boolean exists=(r!=null);
//			if (mustExist && !exists ) {setErrorMessage(MUSTEXIST);return false;}
//			if (exists && !validSelection(r)) {return false;} // this method already set error message
//			if (mustNotExist && exists ) {setErrorMessage(MUSTNOTEXIST);return false;}
////			if (!getStringValue().endsWith(".appdef")) {return false;}
//			utils.log("docheckstate returning true");
//			return true;
//		}
//
//		public boolean validSelection(IResource res) {
//			utils.log("Validating: "+res+ utils.isFile(res)+" "+res.getName());
//			if ( (mustBeResourceType==FILE) && !(utils.isFile(res)) ) {setErrorMessage(SELECTFILE);return false;}
//			if ( (mustBeResourceType==CONTAINER) && !(utils.isContainer(res)) ) {setErrorMessage(SELECTCONTAINER);return false;}
//			if ( (mustBeResourceType==PROJECT) && !(utils.isProject(res)) ) {setErrorMessage(SELECTPROJECT);return false;}
//			if ( (mustBeResourceType==FOLDERNOTPROJECT) && !(utils.isFolder(res)) ) {setErrorMessage(SELECTFOLDERNOTPROJECT);return false;}
////			if (res!=null) {return true;}
//			return true;
//		}
//		protected void refreshValidState() {
//			utils.log("Refresh valid state");
//			super.refreshValidState();
//			utils.log("Field state now: "+isValid());
//		}
//		protected void valueChanged() {
//			utils.log("value changed");
//			super.valueChanged();
//		}
//		/* (non-Javadoc)
//		 * @see org.eclipse.jface.preference.StringButtonFieldEditor#changePressed()
//		 */
//		protected String changePressed() {
//			IResource res = utils.getSelectedResource();
//			String s=stringify(res);
//			if (s==null) {showErrorMessage(getErrorMessage());}
//			textField.setFocus();
//			return s;
//		}
//		public String stringify(IResource res) {
//			String result=null;
//			
//			if ((res!=null) && validSelection(res)) {  //validSelection may be overwritten to allow null
//				result=res.getFullPath().toString();
//			}
//			return result;
//		}
//
//		protected boolean checkState() {
//			utils.log("Checking fieldstate - before="+isValid());
//			boolean valid=super.checkState();
//			utils.log("Fieldstate checked - setting to:"+valid);
//			
////			if (!valid) {setFocus();}
////			if (valid) {clearErrorMessage();}
//			return valid;
//		}
//		
//		public int getMustBeResourceType() {
//			return mustBeResourceType;
//		}
//		public void setMustBeResourceType(int mustBeResourceType) {
//			this.mustBeResourceType = mustBeResourceType;
//		}
//		public boolean isMustExist() {
//		return mustExist;
//		}
//		public void setMustExist(boolean mustExist) {
//		this.mustExist = mustExist;
//		}
//		public boolean isMustNotExist() {
//			return mustNotExist;
//		}
//		public void setMustNotExist(boolean mustNotExist) {
//			this.mustNotExist = mustNotExist;
//		}
//		
//		public class Listener implements ISelectionListener, FocusListener, ShellListener {
//			PBEResourceEditor fe;
//			private IWorkbenchPart lastPart;
//			private boolean isActive=true;
//			private boolean shellActive;
//			private String labelText;
//			
//			public Listener(PBEResourceEditor fe) {
//				textField.getShell().addShellListener(this);
//				this.fe=fe;
//				labelText=fe.getLabelText();
//				} 
//			/* (non-Javadoc)
//			 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
//			 */
//			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
////				utils.log("Selection changed");
//				if (!isActive ) {return;}
////				if (lastPart!=part) {lastPart=part; return;} //get 2 events when change pane
//				if (selection instanceof IStructuredSelection) {
//					IStructuredSelection isel = (IStructuredSelection)selection;
//					Object sel = isel.getFirstElement();
////					utils.log("Selection: "+sel.getClass());
////					utils.dumpClass(sel);
//					IResource r=null;
//					if (sel instanceof IAdaptable){
//					r=(IResource)((IAdaptable)sel).getAdapter(IResource.class); //returns null if not resource (eg error log entry)
////					utils.dump("Resource: "+r);
//					}
//					String result=stringify(r);  // must allow nulls 
//					if (result!=null) {
//						utils.log("Stringify result: "+result);
//						textField.setText(result);
////						clearErrorMessage();
//						utils.log("Firing value change ...");
//						fe.valueChanged();
//						utils.log("Fired!");
//					} else {showErrorMessage(getErrorMessage());}
//					
//				}
//				
//			}
//
//			public void focusGained(FocusEvent e) {
////				utils.log("on Focus");
//				isActive=true;
//				((PBEResourceEditor) fe).activateHotSelect();
//			}
//			public void focusLost(FocusEvent e) {
////				utils.log("lost Focus - in shell="+shellActive);
//				boolean leftShell=!shellActive;
//				if (!leftShell) {isActive=false; clearErrorMessage();clearMessage();}
//			}
//			/* (non-Javadoc)
//			 * @see org.eclipse.swt.events.ShellListener#shellActivated(org.eclipse.swt.events.ShellEvent)
//			 */
//			public void shellActivated(ShellEvent e) {
////				utils.log("Activated - control deactivated");
//				shellActive=true;
//				isActive=false;
//				clearErrorMessage();
//			}
//			/* (non-Javadoc)
//			 * @see org.eclipse.swt.events.ShellListener#shellClosed(org.eclipse.swt.events.ShellEvent)
//			 */
//			public void shellClosed(ShellEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			/* (non-Javadoc)
//			 * @see org.eclipse.swt.events.ShellListener#shellDeactivated(org.eclipse.swt.events.ShellEvent)
//			 */
//			public void shellDeactivated(ShellEvent e) {
////				utils.log("Deactivated");
//				shellActive=false;
//				
//			}
//			/* (non-Javadoc)
//			 * @see org.eclipse.swt.events.ShellListener#shellDeiconified(org.eclipse.swt.events.ShellEvent)
//			 */
//			public void shellDeiconified(ShellEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			/* (non-Javadoc)
//			 * @see org.eclipse.swt.events.ShellListener#shellIconified(org.eclipse.swt.events.ShellEvent)
//			 */
//			public void shellIconified(ShellEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//		}
//		
//		
//		static String copyright() { return Copyright.PV_COPYRIGHT; }
//		
//	}