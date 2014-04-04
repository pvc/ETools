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
 * Created on 29-Dec-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.p4eb.UML;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Element;

import com.ibm.pbe.rsa.interfaces.Executable;

import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.pv.core.Utils;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UMLChildSupplier implements Executable{
	
	Utils utils=Utils.getSingleton();
	Set filter=null;
		
	public Object execute(Object o) {
		EList l=new BasicEList();
		if (o instanceof Node) {
			Node n=(Node)o;
			EList edges=n.getSourceEdges();
			for (Iterator iter = edges.iterator(); iter.hasNext();) {
				Edge edge = (Edge) iter.next();
				l.add(edge.getTarget());
			}
		}
		else if (o instanceof Element) {
			l=((Element)o).getOwnedElements();
		}
		
		if (filter!=null) {
			EList l2=new BasicEList();
			for (Iterator iter = l.iterator(); iter.hasNext();) {
				Element e = (Element) iter.next();
				if (filter.contains(e.eClass())) {l2.add(e);}
			}
			return l2;
		}
		return l;
	}

	public Set getFilter() {
		return filter;
	}
	public void setFilter(Set filter) {
		this.filter = filter;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
