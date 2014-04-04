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

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.pv.core.Utils;
import org.w3c.dom.Element;


public class FieldEditorProvider implements IFieldEditorProvider, IPBEFieldValidator, IPBEFieldInitialiser {
	static final Utils utils=Utils.getSingleton();
	Composite parent=null;
	IPBEFieldValidator validator=null;
	IPBEFieldInitialiser initialiser=null;
	
	public FieldEditorProvider(IPBEFieldValidator v, IPBEFieldInitialiser ini) {
		this.validator=v; this.initialiser=ini;
		utils.log("Validator set to: "+v);
	}

	/* (non-Javadoc)
	 * @see com.ibm.pbe.core.IFieldEditorProvider#doField(org.eclipse.jface.preference.FieldEditor)
	 */
	public String doField(FieldEditor fe) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibm.pbe.core.IFieldEditorProvider#doSelect(org.eclipse.jface.preference.FieldEditor, java.lang.Object)
	 */
	public String doSelect(FieldEditor fe, Object selection) {
		return null;
	}

	public FieldEditor getFieldEditor(Element fieldDef, Composite parent) {
		fieldDef.setAttribute("help", fieldDef.getAttribute("help").replaceAll("\\\\n","\n"));
//		if (this.parent==null) {this.parent=parent;}
		String type=fieldDef.getAttribute("type");
		if (type.equals("boolean")) {return(new PBECheckBoxEditor(parent,fieldDef, getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.length()==0) {return(new PBEStringFieldEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("folder")) {return(new PBEDirectoryFieldEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("stringList")) {return(new PBEStringListEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("combo")) {return(new PBEComboEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("dropDown")) {return(new PBEDropDownEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("file")) {return(new PBEFileFieldEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("wsResource")) {return(new PBEResourceEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("wsFile")) {return(new PBEWSFileEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("wsFolder")) {return(new PBEWSFolderEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("wsProject")) {return(new PBEWSProjectEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("string")) {return(new PBEStringFieldEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("boolean")) {return(new PBECheckBoxEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("pathList")) {return(new PBEPathListEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("resourceList")) {return(new PBEResourceListEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("separator")) {return(new PBESeparator(parent,fieldDef));}
		if (type.equals("label")) {return(new PBESeparator(parent,fieldDef,false));}
		if (type.equals("text")) {return(new PBEMLEEditor(parent,fieldDef,getValidator(fieldDef),getInitialiser(fieldDef)));}
		if (type.equals("section")) {
			PBESection section=new PBESection(parent,fieldDef.getAttribute("label"),fieldDef.getAttribute("help").replaceAll("\\\\n","\n"));
			return(section);
			}
		return null;
	}

	private IPBEFieldInitialiser getInitialiser(Element fieldDef) {
		return (initialiser==null)?this:initialiser;
	}

	public IPBEFieldValidator getValidator(Element field) {
		return (validator==null)?this:validator;
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }

}
