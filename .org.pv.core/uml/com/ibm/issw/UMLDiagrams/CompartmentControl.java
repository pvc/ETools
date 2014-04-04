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
 * Created on 23-Dec-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.UMLDiagrams;

import java.awt.Dimension;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;


import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode;
import org.pv.core.Utils;

import com.ibm.xtools.uml.ui.diagram.IUMLDiagramHelper;
import com.ibm.xtools.umlnotation.UMLListCompartmentStyle;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CompartmentControl implements Executable{
	Utils utils=Utils.getSingleton();
	int x=0;int y=0;
	
	public Object execute(Node n) {
		IUMLDiagramHelper dh = UMLModeler.getUMLDiagramHelper();
		Node part=(Node)dh.getChildView(n,"AttributeCompartment");
		part.setVisible(true);
		EList styles=part.getStyles();
		for (Iterator iter = styles.iterator(); iter.hasNext();) {
			Style s = (Style) iter.next();
			utils.log("Style: "+s);
			UMLListCompartmentStyle s1=(UMLListCompartmentStyle)s;
//			s1.setCollapsed(true);
		}
//		part.setVisible(false);
		ILayoutNode ln = dh.getLayoutNode(n);
		utils.log("Size="+ln.getWidth()+'x'+ln.getHeight());

	return null;
	}

	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return execute((Node) o);
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
