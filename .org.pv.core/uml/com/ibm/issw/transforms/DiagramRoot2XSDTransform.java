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
 * Created on 27-Oct-2005
 *
 * Creates a document from a root UML view object
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.transforms;

import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.pv.core.Utils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xslt4j.bcel.generic.GETFIELD;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DiagramRoot2XSDTransform implements Executable {
	Utils utils= Utils.getSingleton();
	Executable transform;
	UMLChildFinder cf;
	Document d;
	String uid=""+System.currentTimeMillis();
	String nsURI="http://www.ibm.com/issw/DSL"+uid;
	
	
	/**
	 * Translates a DOM document to an XML string 
	 * @param d
	 * @return
	 */
	

	/**
	 * Lowercases first char of String
	 * @param s
	 * @return
	 */
	public static String lc1(String s){
		return s.substring(0,1).toLowerCase() + s.substring(1);
	}
	
	/**
	 * Creates doc root & invokes recursive doc build
	 * @param m
	 * @return
	 */
	public Document execute(org.eclipse.gmf.runtime.notation.Node root) {
		d=null;
		try {
			d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element def=d.createElementNS("http://www.w3.org/2001/XMLSchema","xsd:schema");
		d.appendChild(def);
		String rootName=((NamedElement)root.getElement()).getName();
		int index=rootName.indexOf(':');
		if (index>0) { //can't be first!
			String prefix=rootName.substring(0,index);
			utils.log("Rootprefix="+prefix);
		}
//		Attr att=d.createAttribute("xmlns:"+prefix);
		Attr att=d.createAttribute("xmlns:dsl");
//		att.setValue("http://www.ibeclipse.org/emf/2002/Ecore");
		att.setValue(nsURI);
		def.setAttributeNode(att);
		att=d.createAttribute("targetNamespace");
		att.setValue(nsURI);
		def.setAttributeNode(att);
//		Element def2=d.createElementNS("www.pv.org2","pv2:app2");
//		def.appendChild(def2);
		transform=new UML2XSDElementTransform(d);
		cf=new UMLChildFinder();
		try {
			createSubDoc(def,root);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerFactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return d;
		

	}
	
	/**
	 * Main workhorse - processes each tree node 
	 * @param d
	 * @param n
	 * @param o
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public void createSubDoc(Node n,Object o) throws ParserConfigurationException, FactoryConfigurationError, TransformerFactoryConfigurationError, TransformerException {
		// should refactor to general subtree - need to lose DOM specific stuff
		utils.log("Creating docnode from: "+o);
		
		if (o instanceof Collection) {
			for (Iterator iterator = ((Collection)o).iterator(); iterator.hasNext();) {
				Object element = (Object) iterator.next();
				createSubDoc(n,element);
			}
		}
		else {
			Element nn=(Element)transform.execute((org.eclipse.gmf.runtime.notation.Node)o);  // returns DOM Element
			utils.log("Transform returned: "+nn);
			if (nn!=null) {
				n.appendChild(nn);
				EList children=cf.getChildren(o);
				if (children.size()>0) {
					Element ct=(Element)nn.getFirstChild(); //if there are children at this point they will be attributes enclosed in a Complextype
					if (ct==null){ct=d.createElement("xsd:complexType");nn.appendChild(ct);}
					Element firstAtt=(Element)ct.getFirstChild(); 
					Element seq=d.createElement("xsd:sequence");
					ct.insertBefore(seq,firstAtt);
					
				for (Iterator iter = children.iterator(); iter.hasNext();) {
					Object child =  iter.next();
					createSubDoc(seq,child);
				}
				}
			}
		}
//		utils.log("SubDoc returning");
	}


	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		return execute((org.eclipse.gmf.runtime.notation.Node)o);
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
