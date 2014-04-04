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
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.pv.core.Utils;
import org.w3c.dom.Element;


public class PBEStringListEditor extends ListEditor {
	static final Utils utils=Utils.getSingleton();
//	Listener listener=new Listener(this);
	List list;
	String separator=";";
	IPBEFieldValidator validator; 
	
	public PBEStringListEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(fieldDef.getAttribute("name"),fieldDef.getAttribute("label"),parent);
		this.validator=v;
		getLabelControl().setToolTipText(fieldDef.getAttribute("help"));
		list = getListControl(parent);
		((GridData)list.getLayoutData()).grabExcessVerticalSpace=true;
	}
		
//	public PBEStringListEditor(String name, String labelText, String tooltipText, Composite parent, PBEValidator validator) {
//			super(name,labelText,parent);
//			getLabelControl().setToolTipText(tooltipText);
//			this.validator=validator;
//			list = getListControl(parent);
//			((GridData)list.getLayoutData()).grabExcessVerticalSpace=true;
////			list.addDisposeListener(new DisposeListener() {
////				public void widgetDisposed(DisposeEvent event) {
////					utils.log("Killing Listener");
////					utils.getActivePage().removeSelectionListener(listener);
////					listener=null;
////				}
////			});
////			utils.getActivePage().addSelectionListener(listener);
////			setChangeButtonText("Browse..");
//		}
////		protected boolean doCheckState() {
////			if (getStringValue().length()==0) {doLoadDefault();}
////			return true;
////		}
//		/* (non-Javadoc)
//		 * @see org.eclipse.jface.preference.StringButtonFieldEditor#changePressed()
//		 */
////		protected String changePressed() {
////			return PBEDialog.utils.askForFile(getStringValue());
////		}
/* (non-Javadoc)
 * @see org.eclipse.jface.preference.ListEditor#createList(java.lang.String[])
 */
protected String createList(String[] items) {
	utils.log("Creating list");
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < items.length; i++) {
		sb.append(items[i]).append(separator);
	}
	return sb.toString();
}
/* (non-Javadoc)
 * @see org.eclipse.jface.preference.ListEditor#getNewInputObject()
 */
protected String getNewInputObject() {
	return utils.askForInput("Add New Homonym", "Enter variable name", "strVersion");
}
/* (non-Javadoc)
 * @see org.eclipse.jface.preference.ListEditor#parseString(java.lang.String)
 */
protected String[] parseString(String stringList) {
	return stringList.split(separator);
}

//public void dispose() {
//	utils.log("Disposing");
//	utils.getActivePage().removeSelectionListener(listener);
//	listener=null;
//	super.dispose();
//}

public class Listener implements ISelectionListener {
	FieldEditor fe;
	private IWorkbenchPart lastPart;
	public Listener(FieldEditor fe) {this.fe=fe;} 
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		utils.log("Selection now: "+part);
		if (lastPart!=part) {lastPart=part; return;}
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection isel = (IStructuredSelection)selection;
			Object sel = isel.getFirstElement();
			utils.log(sel.getClass());
			IResource r=(IResource)((IAdaptable)sel).getAdapter(IResource.class);
			if (r!=null) {
				utils.log(r.getFullPath().toString());
				list.add(r.getFullPath().makeRelative().toString());
				list.setSelection(list.getItemCount()-1);
			}
		}
		
	}
	
}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
	}