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
 * Created on 11-Jan-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.p4eb.UML;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.NamedElement;
import org.pv.core.Utils;
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
public class UML2XML {
	Utils utils=Utils.getSingleton();
	Executable UML2XMLTransform;
	Executable UMLChildSupplier;
	Document doc;
	IFile f;

	/* (non-Javadoc)
	 * @see com.ibm.issw.rsa.interfaces.Executable#execute(java.lang.Object)
	 */
	public void execute() {
		doc = utils.getNewDocument("UMLXMLContent");
		execute((Element)doc.getFirstChild());
		if (f==null) {utils.p("No UML Elements selected");return;}
		utils.save(doc,f);
		utils.edit(f);
	}
	
	public void execute(Element n) {
		
		if (UML2XMLTransform==null) {UML2XMLTransform=new SimpleTransform(doc);}
		if (UMLChildSupplier==null) {UMLChildSupplier=new UMLChildSupplier();}
		List elts=utils.getSelectedUMLElements();
		if (elts.isEmpty()) {return;}
		org.eclipse.uml2.uml.Element e0 = (org.eclipse.uml2.uml.Element)elts.get(0);
		f=utils.getFileFor(e0);
		org.eclipse.uml2.uml.Element eowner=e0.getOwner();
		String qn;
		if (eowner==null) {eowner=e0;} //model
		if (eowner instanceof NamedElement) {qn=((NamedElement)eowner).getQualifiedName();} else {qn=eowner.getNearestPackage().getQualifiedName();}
		n.setAttribute("targetContainer",qn);
		n.setAttribute("type",eowner.eClass().getName());
		n.setAttribute("modelFile",utils.getPath(eowner).toString());
		try {
			createSubDoc(n,elts);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @param doc
	 * @param root
	 */
	public void createSubDoc(Node n,Object o) throws ParserConfigurationException, FactoryConfigurationError, TransformerFactoryConfigurationError, TransformerException {
		// should refactor to general subtree - need to lose DOM specific stuff
//		utils.log("Creating docnode from: "+o);
		
		if (o instanceof Collection) {
			for (Iterator iterator = ((Collection)o).iterator(); iterator.hasNext();) {
				Object element = (Object) iterator.next();
				createSubDoc(n,element);
			}
		}
		else {
			Element nn=(Element)UML2XMLTransform.execute(o);  // returns DOM Element
//			utils.log("Transform returned: "+nn);
			if (nn!=null) {
				n.appendChild(nn);   // DOM verb
				EList children=(EList)UMLChildSupplier.execute(o);
				createSubDoc(nn,children);
			}
		}
//		utils.log("SubDoc returning");
	}
	
	

	public Executable getUML2XMLTransform() {
		return UML2XMLTransform;
	}
	public void setUML2XMLTransform(Executable transform) {
		UML2XMLTransform = transform;
	}
	public Executable getUMLChildSupplier() {
		return UMLChildSupplier;
	}
	public void setUMLChildSupplier(Executable childSupplier) {
		UMLChildSupplier = childSupplier;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
