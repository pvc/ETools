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
package com.ibm.p4eb.UML;

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleTransform implements Executable {
	Utils utils= Utils.getSingleton();
//	NamedElement target;
	Document d;
	char colon=':';
	char uscore='_';
	/**
	 * @param o
	 */
	
	public SimpleTransform(Document d) {
		this.d=d;
	}
	
	public org.w3c.dom.Element executeV(org.eclipse.gmf.runtime.notation.Node v) {
//		utils.log("Entering view transform");
		EObject eo=v.getElement();
		utils.log(eo);
		if (eo instanceof org.eclipse.uml2.uml.Element) {return executeE((org.eclipse.uml2.uml.Element)eo);} else {return null;}
	}
	
	public org.w3c.dom.Element executeE(org.eclipse.uml2.uml.Element eIn) {
		
//		utils.log("Entering: UML2DOMElementAdapter");
		String type=eIn.eClass().getName();
		
		String name="";String qn="";
		if (eIn instanceof NamedElement) {name=((NamedElement)eIn).getName(); qn=((NamedElement)eIn).getQualifiedName();}
//		utils.log(type+":"+name);
		Element eOut=d.createElement(type);
		
		if (name.length()>0) {eOut.setAttribute("name",name);
//		eOut.setAttribute("qn",qn);
		}
//		eOut.setAttribute("eid",utils.eoh.getID(eIn));
//		eOut.setAttribute("qn2",utils.eoh.getQName(eIn,false));
		
		EList sts = eIn.getAppliedStereotypes();
		for (Iterator iter = sts.iterator(); iter.hasNext();) {
			Stereotype st = (Stereotype) iter.next();
//		utils.p("URIFragement:"+((InternalEObject)eIn).eURIFragmentSegment(null,st));
			Element ste=d.createElement(st.eClass().getName());
			ste.setAttribute("name",st.getName());
			eOut.appendChild(ste);
			EList satts = st.getAttributes();
			for (Iterator iterator = satts.iterator(); iterator.hasNext();) {
				Property p = (Property) iterator.next();
				if (p.getName().startsWith("base$")) {continue;}
				Object val = eIn.getValue(st,p.getName());
				if (val!=null) {
					Element vale=null;
					if (val instanceof EnumerationLiteral) {
						vale=d.createElement("EnumValue");
						vale.setAttribute("value",((EnumerationLiteral)val).getName());
						vale.setAttribute("enumeration",((EnumerationLiteral)val).getEnumeration().getName());
					} else {
						vale=d.createElement("Value");
						vale.setAttribute("value",val.toString());
					}
					vale.setAttribute("name",p.getName());
					ste.appendChild(vale);
				}
			}
			
		}
		
		EList kwds=eIn.getKeywords();
		for (Iterator iter = kwds.iterator(); iter.hasNext();) {
			String kwd = (String) iter.next();
			Element kwde=d.createElement("Keyword");
			kwde.setAttribute("name",kwd);
			eOut.appendChild(kwde);
		}
		if (type.equals("Comment")) {
			Text txt=d.createTextNode(((Comment)eIn).getBody());
			eOut.appendChild(txt);;
//			eOut.setAttribute("body",((Comment)eIn).getBody());
		}
		
		return eOut;
	}

	
	public Object execute(Object o) {
		if (o instanceof org.eclipse.gmf.runtime.notation.Node) {return executeV((org.eclipse.gmf.runtime.notation.Node)o);}
		else if (o instanceof org.eclipse.uml2.uml.Element) {return executeE((org.eclipse.uml2.uml.Element)o);}
		return null;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
