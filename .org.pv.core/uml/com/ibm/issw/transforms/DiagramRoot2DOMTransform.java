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
import org.pv.core.Utils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DiagramRoot2DOMTransform implements Executable {
	Utils utils= Utils.getSingleton();
	Executable transform;
	UMLChildFinder cf;
	
	
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
		Document d=null;
		try {
			d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transform=new UML2DOMElementTransform(d);
		cf=new UMLChildFinder();
		try {
			createSubDoc(d,root);
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
		utils.log("Creating docnode from: "+((org.eclipse.gmf.runtime.notation.Node)o).getElement());
		
		if (o instanceof Collection) {
			for (Iterator iterator = ((Collection)o).iterator(); iterator.hasNext();) {
				Object element = (Object) iterator.next();
				createSubDoc(n,element);
			}
		}
		else {
			Node nn=(Element)transform.execute((org.eclipse.gmf.runtime.notation.Node)o);  // returns DOM Element
			utils.log("Transform returned XML node: "+nn);
			utils.log("Adding to XML node: "+n);
			try {
				if (nn!=null) {	n.appendChild(nn); } else {nn=n;}
				EList children=cf.getChildren(o);
				for (Iterator iter = children.iterator(); iter.hasNext();) {
					Object child =  iter.next();
					createSubDoc(nn,child);
				}
			} catch (DOMException e) {
				e.printStackTrace(utils.getLogger()); // likley to be atempt to insert multiple roots - we insert only first
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
