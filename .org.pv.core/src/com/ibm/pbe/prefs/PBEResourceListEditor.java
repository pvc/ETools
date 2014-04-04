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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.pv.core.Utils;
import org.w3c.dom.Element;


public class PBEResourceListEditor extends ListEditor {
	static final Utils utils=Utils.getSingleton();
	Listener listener;
	List list;
	String errorMessage="Please select a valid Application SDA to add";
	Color hilite=JFaceColors.getErrorBorder(this.getShell().getDisplay());
	IPBEFieldValidator validator; 
	
	public PBEResourceListEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(fieldDef.getAttribute("name"),fieldDef.getAttribute("label"),parent);
		this.validator=v;
		getLabelControl().setToolTipText(fieldDef.getAttribute("help"));
		list = getListControl(parent);
		list.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
//				utils.log("Killing Listener");
				utils.getActivePage().removeSelectionListener(listener);
				listener=null;
			}
		});
		listener=new Listener(this);
//		parent.addMouseMoveListener(new TestListener());
		list.addFocusListener(listener);
//		list.setBackground(JFaceColors.getErrorBorder(this.getShell().getDisplay()));
//		list.setBackground(JFaceColors.getErrorBorder(this.getShell().getDisplay()));
		utils.getActivePage().addSelectionListener(listener);
	}
	
//	public PBEResourceListEditor(String name, String labelText,String tooltipText, Composite parent, PBEValidator validator) {
//		super(name,labelText,parent);
//		getLabelControl().setToolTipText(tooltipText);
//		this.validator=validator;
//		list = getListControl(parent);
//		list.addDisposeListener(new DisposeListener() {
//			public void widgetDisposed(DisposeEvent event) {
////				utils.log("Killing Listener");
//				utils.getActivePage().removeSelectionListener(listener);
//				listener=null;
//			}
//		});
//		listener=new Listener(this);
////		parent.addMouseMoveListener(new TestListener());
//		list.addFocusListener(listener);
////		list.setBackground(JFaceColors.getErrorBorder(this.getShell().getDisplay()));
////		list.setBackground(JFaceColors.getErrorBorder(this.getShell().getDisplay()));
//		utils.getActivePage().addSelectionListener(listener);
//		
//		
//	}
	public Composite getButtonBoxControl(Composite parent) {
		Composite ctl = super.getButtonBoxControl(parent);
		Control[] buttons = ctl.getChildren();
		for (int i = 0; i < buttons.length; i++) {
			Button b = (Button)buttons[i];
			if (b.getText().equals(JFaceResources.getString("ListEditor.add"))) {b.setText("Add"); break;}
		}
		return ctl;
	}
	public void activateHotSelect() {
		this.showMessage("HOT SELECT IS ACTIVE FOR:   "+getLabelText());
	}
	public void deactivateHotSelect() {
		this.showMessage(null);
	}
	protected void showMessage(String msg) {
		getPreferencePage().setMessage(msg,IMessageProvider.INFORMATION);
	}
	
	
	protected String createList(String[] items) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < items.length; i++) {
			sb.append(items[i]).append('?');
		}
		return sb.toString();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.ListEditor#getNewInputObject()
	 */
	protected String getNewInputObject() {
		IResource res = utils.getSelectedResource();
		String s=stringify(res);
		if (s==null) {showErrorMessage(getErrorMessage());}
		list.setFocus();
		return s;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.ListEditor#parseString(java.lang.String)
	 */
	protected String[] parseString(String stringList) {
		if (stringList.length()==0) {return new String[] {};} 
		return stringList.split("\\?");
	}
	
	public void dispose() { //never called
		utils.log("Disposing");
		utils.getActivePage().removeSelectionListener(listener);
		listener=null;
		super.dispose();
	}
//	public class TestListener implements MouseMoveListener{
//
//		/* (non-Javadoc)
//		 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
//		 */
//		public void mouseEnter(MouseEvent e) {
//			utils.log("Control entered");
//			
//		}
//
//		/* (non-Javadoc)
//		 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
//		 */
//		public void mouseExit(MouseEvent e) {
//			utils.log("Control exit");
//			
//		}
//
//		/* (non-Javadoc)
//		 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
//		 */
//		public void mouseHover(MouseEvent e) {
//			utils.log("Control hover");
//			
//		}
//
//		/* (non-Javadoc)
//		 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
//		 */
//		public void mouseMove(MouseEvent e) {
//			utils.log("Mouse event: "+e);
//			
//		}
//		
//	}
	public class Listener implements ISelectionListener, FocusListener, ShellListener {
		FieldEditor fe;
		private IWorkbenchPart lastPart;
		private boolean isActive=true;
		private boolean shellActive;
		private String labelText;
		
		public Listener(FieldEditor fe) {
			list.getShell().addShellListener(this);
			this.fe=fe;
			labelText=fe.getLabelText();
			} 
		/* (non-Javadoc)
		 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
		 */
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
//			utils.log("Selection changed");
			if (!isActive ) {return;}
//			utils.log("Selection now: "+part);
//			utils.log("Processing selection");
			if (lastPart!=part) {lastPart=part; return;} //get 2 events when change pane
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection isel = (IStructuredSelection)selection;
				Object sel = isel.getFirstElement();
				utils.log(sel.getClass());
				IResource r=(IResource)((IAdaptable)sel).getAdapter(IResource.class);
				String result=stringify(r);
				if (result!=null) {
//					utils.log(""+result);
					list.add(result);
					list.setSelection(list.getItemCount()-1);
					clearErrorMessage();
				} else {showErrorMessage(getErrorMessage());}
				
			}
			
		}
	
		public void focusGained(FocusEvent e) {
//			utils.log("on Focus");
			isActive=true;
			((PBEResourceListEditor) fe).activateHotSelect();
		}
		public void focusLost(FocusEvent e) {
//			utils.log("lost Focus - in shell="+shellActive);
			boolean leftShell=!shellActive;
			if (!leftShell) {isActive=false; clearErrorMessage();clearMessage();}
		}
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.ShellListener#shellActivated(org.eclipse.swt.events.ShellEvent)
		 */
		public void shellActivated(ShellEvent e) {
//			utils.log("Activated - control deactivated");
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
//			utils.log("Deactivated");
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
	/**
	 * @param res
	 * @return
	 */
	public String stringify(IResource res) {
		String result=null;
		if (res!=null) {
			result=res.getFullPath().makeRelative().toString();
			if ( (!result.startsWith("IRU")) || result.endsWith("_SW")) {result=null;}
		}
		return result;
	}
	/**
	 * @return Returns the errorMessage.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage The errorMessage to set.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}