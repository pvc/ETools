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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
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
public class UML2XSDElementTransform implements Executable {
	Utils utils= Utils.getSingleton();
//	NamedElement target;
	Document d;
	char colon=':';
	char uscore='_';
	/**
	 * @param o
	 */
	
	public UML2XSDElementTransform(Document d) {
		this.d=d;
	}
	
	public NamedNode getValues(View v) { 
		EObject eo=v.getElement();
		if (eo instanceof org.eclipse.uml2.uml.Element) {return getValues((org.eclipse.uml2.uml.Element)eo);} else {return null;}
		
	}
	
	public NamedNode getValues(org.eclipse.uml2.uml.Element e) {
		// returns att names & types
		List values=new LinkedList();
		String name="@unnamedElement";
		if (e instanceof NamedElement) {   
			name=((NamedElement)e).getName();
//			if (!"@unNamed".equals(name)) {values.add(new NamedNode("name",name));}
		} 
		utils.log("Entering getValues for: "+name);
		EList ss=((Classifier)e).getAttributes();
//		if (ss.isEmpty()) {utils.log("No attributes");return null;} // no attribs is valid!
		Property s=null;
		
		//		Stereotype[] s=(Stereotype[])ss.toArray();
		for (Iterator iter = ss.iterator(); iter.hasNext();) {
			s = (Property) iter.next();
			String pname=((NamedElement)s).getName();
			utils.log("Attribute: "+pname+" Namespace:"+s.getNamespace());
			{values.add(new NamedNode(pname,s.getType()));}  //type of s is an instance of Type! normally java.lang.String
			//		utils.log(s);
			//		NamedElement s1=m.getMember(s.getName());
			//		utils.log("AppliedStereo="+s);
			//		utils.log("Stereo="+s1);
			////		EList mbrs=((Stereotype)s1).getMembers();
			
		}
		
		utils.log("Leaving getValues for: "+name);
		return new NamedNode(name,values,false);
	}
	

	public org.w3c.dom.Element executeV(org.eclipse.gmf.runtime.notation.Node v) {
		utils.log("Entering view transform");
		EObject eo=v.getElement();
		utils.log(eo);
		if (eo instanceof org.eclipse.uml2.uml.Element) {return executeE((org.eclipse.uml2.uml.Element)eo);} else {return null;}
	}
	
	public org.w3c.dom.Element executeE(org.eclipse.uml2.uml.Element eIn) {
		utils.log("Entering: UML2XSDElementAdapter");
		NamedNode n=getValues(eIn);
//		if (n==null) {utils.log("No DOM content found in: "+eIn);return null;}
		utils.log("Creating Element "+n.getName());
		
		//Create XSD Element
		Element eOut=d.createElement("xsd:element");
		Attr a=d.createAttribute("name");
		a.setValue(n.getName());
		eOut.setAttributeNode(a);
		
//		Model m=(Model)o;
//		String mType=m.getClass().getName();
//		String mName=lc1(mType.substring(mType.lastIndexOf('.')+1));
		
		List values=(List)n.getValue();
		utils.log("Element attributes: "+values.size());
		if (values.size()>0) {
			//			Create ComplexType Element
			Element eOutCT=d.createElement("xsd:complexType");
			
			int attCount=0;
			for (Iterator iter = values.iterator(); iter.hasNext();) {
				NamedNode entry = (NamedNode) iter.next();
				String name=entry.getName().replace(uscore,colon);
				Object value=entry.getValue(); // type instance could be anything!
				utils.log("Entryname:"+name+" value:"+value);
//				if (value==null) {continue;}
//				if (value instanceof EnumerationLiteral) {value=((NamedElement)value).getName();}
//				{if (value==java.lang.String.class;} // actually we'll just use string!
				if (name.indexOf(':')==-1) {
					if (name.equalsIgnoreCase("nsURI")|name.equalsIgnoreCase("nsPrefix")) {continue;} 
					attCount++;
					Element eOutAtt=d.createElement("xsd:attribute");
					Attr att=d.createAttribute("name");
					att.setValue(name);
					eOutAtt.setAttributeNode(att);
					utils.log("Attvalue: "+att);
					att=d.createAttribute("type");
					att.setValue("xsd:string");
					eOutAtt.setAttributeNode(att);
					eOutCT.appendChild(eOutAtt);
				}
			}
			if (attCount>0) {eOut.appendChild(eOutCT);} 

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
