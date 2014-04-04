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
 * Created on 13-Dec-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.transforms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.pv.core.Utils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.pbe.datatypes.NamedNode;
import com.ibm.pbe.rsa.interfaces.Executable;

import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UML2DOMElementTransform implements Executable {
	Utils utils= Utils.getSingleton();
//	NamedElement target;
	Document doc;
	char colon=':';
	char uscore='_';
	private boolean omitEmptyAttributes=true;
	EList stereos;
	private boolean isStereotyped;
	/**
	 * @param o
	 */
	
	public UML2DOMElementTransform(Document d) {
		this.doc=d;
	}
	
	public void run() {
		
	}
	
	private String getTagName(org.eclipse.uml2.uml.Element e) {
		if (isStereotyped) {return ((Stereotype)(stereos.toArray()[0])).getName();}
		EList kw=e.getKeywords();
		if (kw.isEmpty()) {String name=e.eClass().getName();if (name.equals("Property")) {name="Attribute";}; return name;}
		return (String)kw.toArray()[0];
	}
	private String getName(org.eclipse.uml2.uml.Element e) {
		if (e instanceof NamedElement) {return ((NamedElement)e).getName();}
		return null;
	}	
	
	public List getStereoAttributes(View v) { 
		EObject eo=v.getElement();
		if (eo instanceof org.eclipse.uml2.uml.Element) {return getStereoAttributes((org.eclipse.uml2.uml.Element)eo);} else {return null;}
	}
	
	public List getStereoAttributes(org.eclipse.uml2.uml.Element e) {
//		utils.log("Entering getValues");
		List values=new ArrayList();
		if (stereos.isEmpty()) {return values;}
		Stereotype s=(Stereotype)stereos.toArray()[0];
		
		EList mbrs=s.getAttributes();
		for (Iterator it2 = mbrs.iterator(); it2.hasNext();) {
			Property p = (Property) it2.next();
			String pname=p.getName();
			if (pname.startsWith("base_")) {continue;}
			Object value=e.getValue(s,pname);
			String svalue="";
			if (value==null) {}
			else if (value instanceof String) {svalue=(String)value;} 
			else if (value instanceof EnumerationLiteral) {svalue=((NamedElement)value).getName();}
			else if (value instanceof org.eclipse.uml2.uml.Element) {svalue=((org.eclipse.uml2.uml.Element)value).eClass().getName();}
			Attr a=doc.createAttribute(pname);
			a.setValue(svalue);
			values.add(a);
		}
		return values;
	}
	

	public org.w3c.dom.Element execute(org.eclipse.gmf.runtime.notation.Node v) {
		utils.log("Entering view transform");
		EObject eo=v.getElement();
		utils.log(eo);
		if (eo instanceof org.eclipse.uml2.uml.Element) {return execute((org.eclipse.uml2.uml.Element)eo);} else {return null;}
	}
	
	public org.w3c.dom.Element execute(org.eclipse.uml2.uml.Element eIn) {
		utils.log("Entering: UML2DOMElementAdapter");
		stereos=eIn.getAppliedStereotypes();
		if (stereos.isEmpty()) {isStereotyped=false;} else {isStereotyped=true;}
		Element eOut=doc.createElement(getTagName(eIn));
		if (isStereotyped) {
			List atts=getStereoAttributes(eIn);
			for (Iterator iter = atts.iterator(); iter.hasNext();) {
				Attr a = (Attr) iter.next();
				if ( (!omitEmptyAttributes) || (a.getValue().length()>0) ) {eOut.setAttributeNode(a);}
			}
		}
		String name=getName(eIn);
		if (name!=null) {eOut.setAttribute("name",name);}
		processProperties(eIn,eOut);
		return eOut;
	}

	/**
	 * @param in
	 * @param out
	 */
	private void processProperties(org.eclipse.uml2.uml.Element eIn, Element eOut) {
		utils.log("Eneterin ELAdapetr for "+eIn);
		if (eIn instanceof org.eclipse.uml2.uml.Classifier) {
			utils.log("****Processing as classifier");
			org.eclipse.uml2.uml.Classifier c=(org.eclipse.uml2.uml.Classifier)eIn;
			Package pkg = c.getNearestPackage();
			//			if (pkg!=rootPkg) {eOut.setAttribute("package",pkg.getName());} 
			EList atts=c.getAttributes();
			for (Iterator iterator = atts.iterator(); iterator.hasNext();) {
				Property att = (Property) iterator.next();
				utils.log("****Processing att: "+att);
				Element eAtt=execute(att);
				Association assoc = att.getAssociation();
				if (assoc!=null) {
					eAtt.setAttribute("ref","true");
					String assocName=assoc.getName();
					if (assocName.length()>0) {eAtt.setAttribute("refName",assoc.getName());}
				}
				//				String typeName="String";
				//				Type type=att.getType();
				//				if (type!=null) {typeName=type.getName();}
				//				eAtt.setAttribute("type",typeName);
				String def=att.getDefault();
				utils.log("Default: "+def);
				if (def!=null) {eAtt.setAttribute("default",def);}
				//				eAtt.setAttribute("showAs","attribute");
				eOut.appendChild(eAtt);



				//				if (def!=null) {eAtt.setAttribute("default",def);}
				//				eAtt.setAttribute("length","250");
				//				eAtt.setAttribute("variable","true");
				//				Set kw=att.getKeywords();
				////				utils.log("KWords for "+att.getName()+kw);
				//				if (!kw.isEmpty()) {e.setAttribute("key",att.getName());} // BAD - must dd an iterator to check values of kws!!
			}
			utils.log("C1");
			EList ops=null;
			if (c instanceof Class) { ops=((Class)c).getOwnedOperations();}
			if (c instanceof Interface) { ops=((Interface)c).getOwnedOperations();}
			if (ops!=null) {
				utils.log("************Behaviors:");
				for (Iterator iterator= ops.iterator(); iterator.hasNext();) {
					Operation op= (Operation) iterator.next();
					utils.log(op);
					Element eOp=execute(op);
					EList parms = op.getOwnedParameters();
					for (Iterator iterator2 = parms.iterator(); iterator2.hasNext();) {
						Parameter parm = (Parameter) iterator2.next();
						Element eParm=doc.createElement("Parameter");
						eParm.setAttribute("name",parm.getName());
						Type t2=parm.getType();
						if (t2!=null) {eParm.setAttribute("type",t2.getName());}
						eParm.setAttribute("direction",parm.getDirection().getName());
//						eParm.setAttribute("default",parm.getDefault());
						eOp.appendChild(eParm);
					}

					eOut.appendChild(eOp);
				}
//				}
			}
		}
	utils.log("Leaving ELAdapter");	
	}

	public Object execute(Object o) {
		utils.log("***Error: Could not process UML element: "+o);
//		if (o instanceof org.eclipse.gmf.runtime.notation.Node) {return execute((org.eclipse.gmf.runtime.notation.Node)o);}
//		else if (o instanceof org.eclipse.uml2.uml.Element) {return execute((org.eclipse.uml2.uml.Element)o);}
		return null;
	}
	public boolean isOmitEmptyAttributes() {
		return omitEmptyAttributes;
	}
	public void setOmitEmptyAttributes(boolean omitEmptyAttributes) {
		this.omitEmptyAttributes = omitEmptyAttributes;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
