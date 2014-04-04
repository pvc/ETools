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


public class PBEPathListEditor extends PathEditor {
	static final Utils utils=Utils.getSingleton();
//	Listener listener=new Listener(this);
	List list;
	IPBEFieldValidator validator; 
	String separator=";";
	
	public PBEPathListEditor(Composite parent, Element fieldDef, IPBEFieldValidator v, IPBEFieldInitialiser init) {
		super(fieldDef.getAttribute("name"),fieldDef.getAttribute("label"),"Select a folder to be added to the list",parent);
		this.validator=v;
		getLabelControl().setToolTipText(fieldDef.getAttribute("help"));
		list = getListControl(parent);
		((GridData)list.getLayoutData()).grabExcessVerticalSpace=true;
	}
		
//	public PBEPathListEditor(String name, String labelText, String tooltipText, String pathDialogText, Composite parent, PBEValidator validator) {
//		super(name,labelText,pathDialogText,parent);
//		getLabelControl().setToolTipText(tooltipText);
//		this.validator=validator;
//		list = getListControl(parent);
//		((GridData)list.getLayoutData()).grabExcessVerticalSpace=true;
//	}		
	
	protected String createList(String[] items) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < items.length; i++) {
			sb.append(items[i]).append(separator);
		}
		return sb.toString();
	}
	
	protected String[] parseString(String stringList) {
		return stringList.split(separator);
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}