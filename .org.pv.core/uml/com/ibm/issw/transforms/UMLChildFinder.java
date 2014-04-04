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
package com.ibm.issw.transforms;

import java.util.Iterator;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;


import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.pv.core.Utils;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UMLChildFinder {
	
	Utils utils=Utils.getSingleton();
	
	public EList getChildren(Object o) {
		EList l=new BasicEList();
		if (o instanceof Node) {
			Node n=(Node)o;
			EList edges=n.getTargetEdges();
			for (Iterator iter = edges.iterator(); iter.hasNext();) {
				Edge edge = (Edge) iter.next();
				l.add(edge.getSource());
				utils.log("Child found: " +edge.getSource().getElement());
			}
		}
		return l;
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
