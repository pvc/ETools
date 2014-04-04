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

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.pbe.graphs.Graph;
import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Metamodel2ProfileApplier implements Executable {
	
	Utils utils=Utils.getSingleton();
	URI profileURI= utils.string2URI("GenProfile/Profile.epx");  //Hardcoded!! must update
	Stereotype stereotype;
	Graph g;
	Model m;
	URI modelURI;
	String modelName;
	/**
	 * @param m
	 */
	public Metamodel2ProfileApplier(Model m) {
		this.m = m;
	}
	public Metamodel2ProfileApplier(String filepath) {
		modelURI=utils.string2URI(filepath);
	}
	public Metamodel2ProfileApplier(URI uri) {
		modelURI=uri;
	}
	/**
	 * @param o
	 */

	
	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return (Profile)execute((Diagram)o);
	}

	public Model execute(Document d) {
		utils.log("Entering: DOM2MetaModel");
//		URI testModelURI= utils.string2URI("GenProfile/TestMetamodel.emx");
		if (m==null) {
			try {
				m= UMLModeler.openModel(modelURI);
				utils.log("Model opened: "+m);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				utils.log(e);
				m= UMLModeler.createModel(modelURI);
				if (modelName==null) {modelName=modelURI.trimFileExtension().lastSegment();}
				m.setName(modelName);
				utils.log("Model created: "+m);
				Profile p=null;
				try {
					p = UMLModeler.openProfile(profileURI);
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					utils.log(e3);
				}
				utils.log("Loaded Profile: "+p);
				utils.log("Trying apply of "+p);
				try {m.applyProfile(p);} catch (Exception e2) {utils.log("Profile apply problem: "+e2);} 
				utils.log("Done!");
			}
		}
		Enumeration types=(Enumeration)m.getMember("Types"); // Not a good name + these literals need externalising
		stereotype=(Stereotype)m.getMember("Stereotype");  // is there a problem here with possible duplicates?
		
		Class c=processNode(d.getFirstChild()); // discard doc element
		EList mbrs = m.getOwnedMembers();
		for (Iterator iter = mbrs.iterator(); iter.hasNext();) {
			PackageableElement e = (PackageableElement) iter.next();
			if (e instanceof Class) {
				utils.log("Applying stereotype: "+stereotype);
				try {((Class)e).applyStereotype(stereotype);} catch (Exception e2) {utils.log("Already applied: "+e2);} 
				utils.log("Setting extension");
				e.setValue(stereotype,"Extends",types.getOwnedLiteral("Class"));
				utils.log("Set!");
			}
			
		}
		
//		try {
//			UMLModeler.saveModel(m);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		return m;
	}

	public Class processNode(Node node) {
		Class c=(Class)m.getOwnedMember(node.getNodeName());
		if (c==null) {
			c = (Class)m.createOwnedClass(node.getNodeName(),false);
			if (node.hasAttributes()) {
				NamedNodeMap map=node.getAttributes();
				for (int i = 0; i < map.getLength(); i++) {
					Node n = map.item(i);
					Property att=c.getAttribute(n.getNodeName(),null);
					if (att==null) {
					att = c.createOwnedAttribute(n.getNodeName(),null);
					}
					att.setType((Type)m.getMember("String"));
					utils.log(att);
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
				Class child=processNode(n);
				if (child.getClientDependencies().isEmpty()) {
					Dependency d = child.createDependency(c);
				}
			}
		}
		return c;
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
