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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DOM2TreeModelApplier implements Executable {
	
	Utils utils=Utils.getSingleton();
	Map profileMap;
//	long uid=System.currentTimeMillis();
	int uid=100;
//	URI profileURI= utils.string2URI("GenProfile/TestMetamodel.epx");
//	Profile profile;
//	Stereotype stereotype;
	Model m;
	char colon=':';
	char uscore='_';
	/**
	 * @param o
	 */
	public DOM2TreeModelApplier(Model m) {
		this.m=m;
	}
	
	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return (Model)execute((Document)o);
	}

	
	public Model execute(Document d) {
		utils.log("Entering: "+this.getClass().getName());
		EList profs=m.getAppliedProfiles();
		profileMap=new HashMap();
		for (Iterator iter = profs.iterator(); iter.hasNext();) {
			Profile prof = ((ProfileApplication) iter.next()).getAppliedProfile();
			profileMap.put(prof.getName(),prof);
		}
		Class root = (Class)m.getOwnedMember("Root");
		if (root==null) {root=(Class)m.createOwnedClass("Root",false);} 
		Class c=processNode(d.getFirstChild(),root); // discard doc element
		return m;
	}

	public Class processNode(Node node,Class parent) {
		Class c=null;
		String umlName=null;Node nameAtt; NamedNodeMap nodeMap=null;
		if (node.hasAttributes()) {
			nodeMap=node.getAttributes();
			nameAtt=nodeMap.getNamedItem("name");
			if (nameAtt!=null) {umlName=nameAtt.getNodeValue();}
		}
//		if (umlName!=null) {
//			c=(Class)parent.getMember(umlName);} 
//		else {umlName=getUid();}
		if (umlName==null) {umlName=getUid();}
		if (c==null) {
			c = (Class)parent.createNestedClassifier(umlName,UMLPackage.eINSTANCE.getClass_());
		}
		try {
		
		String lName=node.getLocalName();
		String nodePrefix=node.getPrefix();
		String name=nodePrefix+"_"+lName;
		utils.log("Processing doc element: "+node.getNodeName());
//		String ns=node.getNamespaceURI();  
		String ns=null;
		if (ns==null) {ns="Metamodel";}
		Profile p=(Profile)profileMap.get(ns);
		utils.log("Using Profile: "+p.getName());
		Stereotype s=p.getOwnedStereotype(name);
		//			Stereotype s=c.getApplicableStereotype("DomainLanguage::"+node.getNodeName()); // assumes only one, so must be unique!
		if (s==null) {utils.log("Error: Required Stereotype does not exist: "+node.getNodeName());}
		else {
			
			try {c.applyStereotype(s);} catch (IllegalArgumentException e) {} // ignore already applied
			if (nodeMap!=null) {
				for (int i = 0; i < nodeMap.getLength(); i++) {
					Node n = nodeMap.item(i);
					if ("xmlns".equalsIgnoreCase(n.getPrefix())) {continue;}
//					String attName=n.getNodeName().replace(colon,uscore);
					String attName=n.getLocalName();
//					utils.log("FullNodeName="+fullNodeName);
//					String attName=fullNodeName.substring(fullNodeName.indexOf(':')+1);
					
					utils.log("NodeName="+attName);
					if (!"name".equalsIgnoreCase(attName)) {
						try{
						c.setValue(s,attName,n.getNodeValue());
						} catch (IllegalArgumentException e) {utils.log(e);utils.log("SetValue error: "+s.getName()+':'+attName+"="+n.getNodeValue());}
					}
				}
			}
		}
		if (node.hasChildNodes()) {
			NodeList nodes = node.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				//				utils.log("Node: "+i+n.getNodeName()+" Type: "+n.getNodeType());
				if (n.getNodeType()!=Node.ELEMENT_NODE) {continue;}
				//				utils.log("Is Element");
				Class child=processNode(n,c);
//				child.setValue(s,"Order",String.valueOf(i));
//				if (child.getClientDependencies().isEmpty()) {
//					Dependency d = (Dependency)m.createOwnedMember(UMLPackage.eINSTANCE.getDependency());
//					d.getClients().add(child);
//					d.getSuppliers().add(c);
//				}
			}
		}
		} catch (Exception e) {e.printStackTrace(utils.getLogger());}
		return c;
	}
	/**
	 * @return
	 */
	private String getUid() {
		return ("elt"+uid++);
	}

	public void dumpNode(Node node) {
		Node p2=node.getParentNode();
		String p2Name="";
		if (p2!=null) {p2Name=p2.getNodeName();}
		utils.log(p2Name+"::"+node.getNodeName());
		if (!node.hasAttributes()) {utils.log("No attributes");} 
		else {
		NamedNodeMap map=node.getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			Node n = map.item(i);
			utils.log(n.getNodeName()+"="+n.getNodeValue());
		}
		}
		if (!node.hasChildNodes()) {utils.log("No child nodes");} 
		else {
		NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			dumpNode(n);
		}
		}
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
