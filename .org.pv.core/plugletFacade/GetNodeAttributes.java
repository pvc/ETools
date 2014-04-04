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
import java.util.Iterator;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.NamedElement;


import org.eclipse.gmf.runtime.notation.Node;
import org.pv.core.Utils;
 
import com.ibm.xtools.umlnotation.UMLListCompartmentStyle;
import com.ibm.xtools.umlnotation.UmlnotationPackage;


public class GetNodeAttributes    {
	
	Utils utils=Utils.getSingleton();
		
	public void plugletmain(String[] args) {
		final NamedElement e;
		EObject eo=utils.getSelectedUMLElement();
		if (eo==null) {utils.log("No selection");return;}
		if (!(eo instanceof Node)) { utils.log("Please select a diagram node");return; } //allows diagrams from main tree, + views from diag
		Node v=(Node)eo;
		utils.p("**********");
		EList atts=utils.getAttributes(v);
		utils.p(atts);
	}		
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}