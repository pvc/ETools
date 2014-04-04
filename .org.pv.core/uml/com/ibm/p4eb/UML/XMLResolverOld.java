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

import java.util.Properties;
import java.util.Stack;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.pv.core.Utils;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO 
 * 1) type=literal is bad parctice - check for ns/localname instead
 * 2) wsdl imports need to pool schemas into the (single) types node 
 * 3) imports/includes at 2 levels or more of nesting will  not be deduplicated ??
 * 
 */
public class XMLResolverOld implements Executable {
	
	Utils utils=Utils.getSingleton();
	Document doc;
	URI uri;
	private IPath fileDir;
	Stack stack=new Stack();
	Properties includeList=new Properties();
	/**
	 * @return Returns the d.
	 */
	public Document getDoc() {
		return doc;
	}
	/**
	 * @param d The d to set.
	 */
	public void setDoc(Document d) {
		this.doc = d;
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
		utils.p("Entering via Interface");
		Object o2=execute((IFile)o);
		utils.p("Returning");
		return o2;
	}

	public Document execute(IFile file) {
//		utils.log("Entering: "+this.getClass().getName()+stack.size());
		Document d=utils.getDoc(file);
		if (d==null) {return null;}
		stack.push(fileDir=file.getFullPath().removeLastSegments(1));
//		utils.p("Path="+fileDir);
//		Element root=(Element)d.getFirstChild();
//		NodeList nodes = root.getChildNodes();
		NodeList incNodes = d.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema","include");
		NodeList impNodes = d.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema","import");
		NodeList wsdlImpNodes = d.getElementsByTagNameNS("http://schemas.xmlsoap.org/wsdl/","import");
		
//		utils.p("Nodelist:"+nodes.getLength());
//		utils.p(""+(nodes instanceof Element));
		processNodes(incNodes);
		processNodes(impNodes);
		processNodes(wsdlImpNodes);
		stack.pop();
//		utils.p("Leaving ..."+stack.size());
		return d;
	}

	/**
	 * @param nodes
	 * @param tgt
	 */
	private void processNodes(NodeList nl) {
		if (nl==null ) {return;}
		for (int i=0; i<nl.getLength();i++) {
//			utils.p("Elt:"+i+" "+nl.item(i));
			processNode(nl.item(i));
		}
		for (;nl.getLength()>0;) {
//			utils.p("Length: "+nl.getLength());
			nl.item(0).getParentNode().removeChild(nl.item(0));
		}
	}
	
	public void processNode(Node n) {
		if (n==null) {return;}
//		utils.p("Node type:"+n.getNodeType());
		switch(n.getNodeType()) {
		case Node.TEXT_NODE:
//			utils.p("Text"); return;
		case Node.ELEMENT_NODE:
//			utils.p("Element");
			processElement((Element)n); return;
		case 5:
			
		}
	}
	public void processElement(Element n) {
		String loc = n.getAttribute("schemaLocation");
		if (loc.length()==0) {loc = n.getAttribute("location");}
//		utils.p("Processing Element: "+loc);
		String type=n.getTagName();
//		utils.p("Type="+type);
		
		if (type.equals("xsd:include")||type.equals("xsd:import")||type.equals("import")) {
//			utils.p("Include: "+loc);
			IPath p2=((IPath)stack.peek()).append(loc);
			String p2s=p2.toString();
//			utils.p("Processing Definition File: "+p2s);
			if (includeList.getProperty(p2s)!=null) {/*utils.p("Already processed!");*/ return;}
			includeList.setProperty(p2s,type);
//			utils.p(p2.toString());
			Document d2 = execute(utils.getFile(p2));
			Node parent=n.getParentNode();
			Node n2=n.getOwnerDocument().importNode(d2.getFirstChild(),true);
			NamedNodeMap atts = n2.getAttributes();
			for (int i=0;i<atts.getLength();i++) {
				Attr att = (Attr)atts.item(i);
				if (!"xmlns".equals(att.getPrefix())) {continue;} 
//				utils.p(att.getPrefix()+"::"+att.getLocalName()+"::"+att.getNamespaceURI());
				try {((Element)n.getOwnerDocument().getFirstChild()).setAttributeNS(att.getNamespaceURI(),att.getName(),att.getValue()); }
				catch (DOMException e) {utils.p("Couldn't add:"+att.getLocalName()+e);} 
			}
			NodeList nl2 = n2.getChildNodes();
//			utils.p("No of nodes="+nl2.getLength());
			
			for (;nl2.getLength()>0;) {
//				utils.p("Adding:"+nl2.item(0)+nl2.getLength());
				parent.appendChild(nl2.item(0));
			}
			return;
		}
	}

	


	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
