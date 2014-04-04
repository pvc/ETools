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

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.osgi.framework.Bundle;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.pbe.graphs.Graph;
import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XML2UMLApplier implements Executable {
	
	Utils utils=Utils.getSingleton();
	
	char colon=':';
	char uscore='_';
	Bundle b=Platform.getBundle("com.ibm.pbe.umlToolkit");
	URL url=b.getEntry("/profiles/PBEMMProfile.epx"); //must include path, leading slash is optional
	URI profileURI=URI.createURI(url.toString());
	
	
	Stereotype stereotype;
	Stereotype xmlns;
	Graph g;
//	Element base;
	URI modelURI;
	String modelName;
	Model m;
	Document d;
	/**
	 * @return Returns the d.
	 */
	public Document getDoc() {
		return d;
	}
	/**
	 * @param d The d to set.
	 */
	public void setDoc(Document d) {
		this.d = d;
	}
	/**
	 * @param m
	 */

	/**
	 * @param o
	 */

	
	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		execute((Document)o);
		return null;
	}

	public void execute(Document d) {
		utils.log("Entering: "+this.getClass().getName());
		Element root=(Element)d.getFirstChild();
		String tgtPath=root.getAttribute("targetContainer");
//		String mfile=root.getAttribute("modelFile");
//		utils.p("ModelFile="+mfile);
//		if (mfile.length()==0) {utils.p("modelFile not specified");return;}
//		m=utils.getModel(mfile);
//		m=utils.getModel(tgt.split("::")[0]);
		NamedElement tgt=utils.getUMLElementByPath(tgtPath);
		if (tgt==null) {utils.p("Element not found: "+tgtPath);return;}
		
		NodeList nodes = root.getChildNodes();
		utils.p("Nodelist:"+nodes.getLength());
		utils.p(""+(nodes instanceof Element));
		processNodes(nodes,tgt);
		if (true) {return;}
		String type=root.getAttribute("type");
		
		

//		try {
//			UMLModeler.saveModel(m);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}

	/**
	 * @param nodes
	 * @param tgt
	 */
	private void processNodes(NodeList nl, NamedElement tgt) {
		if (nl==null || tgt==null) {return;}
		for (int i=0; i<nl.getLength();i++) {
			utils.p("Elt:"+i+" "+nl.item(i));
			processNode(nl.item(i),tgt);
		}
	}
	
	public void processNode(Node n,org.eclipse.uml2.uml.NamedElement tgt) {
		utils.p("Entering pn: "+n+" : "+tgt);
		if (n==null || tgt==null) {return;}
		utils.p("Node type:"+n.getNodeType());
		switch(n.getNodeType()) {
		case Node.TEXT_NODE:
			utils.p("Text"); return;
		case Node.ELEMENT_NODE:
			processElement((Element)n,tgt); return;
			
		}
	}
	public void processElement(Element n,org.eclipse.uml2.uml.NamedElement tgt) {
		EFactory umlf=UMLPackage.eINSTANCE.getEFactoryInstance();
		org.eclipse.uml2.uml.NamedElement newChild=null;
		utils.p("Processing Element"+n);
		
		utils.p("Adding "+n.getNodeName()+" to: "+tgt);
		String type=n.getTagName();
		utils.p("Type="+type);
		
		if (type.equals("Stereotype")) {
			Stereotype s=(Stereotype)tgt.getModel().getImportedMember(n.getAttribute("name"));
			if (s==null) {utils.p("Model has no such stereotype: "+n.getAttribute("name"));return;}
			tgt.applyStereotype(s);
			NodeList nl=n.getElementsByTagName("Value");
			for (int i=0;i<nl.getLength();i++) {
				Element v=(Element)nl.item(i);
				tgt.setValue(s,v.getAttribute("name"),v.getAttribute("value"));
			}
			return;
		}
		if (type.equals("Comment")) {
//			EClass c = (EClass)UMLPackage.eINSTANCE.getComment();
			Comment cm=tgt.createOwnedComment();
			NodeList nl=n.getChildNodes();
			for (int i=0;i<nl.getLength();i++) {
				Node node=nl.item(i);
				switch(node.getNodeType()) {
					case Node.TEXT_NODE: cm.setBody(node.getNodeValue()); break;
					case Node.ELEMENT_NODE: if (node.getNodeName().equals("Stereotype")) {
						Stereotype s=(Stereotype)tgt.getModel().getImportedMember(((Element)node).getAttribute("name"));
						cm.applyStereotype(s);
					} break;
				}
			}
			return;
		}
		if (type.equals("Constraint")) {return;} // Don't  handle constraints
		
		EClass c = (EClass)UMLPackage.eINSTANCE.getEClassifier(type); // could it be an edtataytype?
		if (tgt instanceof Package) {
			newChild=((Package)tgt).createOwnedClass(n.getAttribute("name"),false);
		}
		else if (tgt instanceof Class) {
			newChild=((Class)tgt).createNestedClassifier(n.getAttribute("name"),c);
		} 
//		if (newChild!=null) {newChild.setName(n.getAttribute("name"));}
		processNodes(n.getChildNodes(),newChild);
		utils.p("Result: "+newChild);
		
	}

	


	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
