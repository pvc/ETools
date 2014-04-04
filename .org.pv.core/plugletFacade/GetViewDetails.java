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
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.UMLPackage;


import org.eclipse.gmf.runtime.emf.core.ResourceSetModifyOperation;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.pv.core.Utils;
 
import com.ibm.xtools.umlnotation.UMLListCompartmentStyle;
import com.ibm.xtools.umlnotation.UmlnotationPackage;


public class GetViewDetails    {
	
	Utils utils=Utils.getSingleton();
		
	public void plugletmain(String[] args) {
		final NamedElement e;
		EObject eo=utils.getSelectedUMLElement();
		if (eo==null) {utils.log("No selection");return;}
		if (!(eo instanceof Node)) { utils.log("Please select a diagram node");return; } //allows diagrams from main tree, + views from diag
		Node v=(Node)eo;
		utils.p("**********");
		dumpNode(v);
	}		
	
	public void dumpNode(Node v) {
		utils.p(v);
		utils.p("Adapters: "+v.eAdapters());
		utils.p("Content: "+v.eContents());
		EList styles=v.getStyles();
		utils.p("Styles:"+styles);
		
		
		LayoutConstraint lc = v.getLayoutConstraint();
		utils.p("LayoutConstraint:"+ lc);
		
		UMLListCompartmentStyle ls = (UMLListCompartmentStyle)v.getStyle(UmlnotationPackage.eINSTANCE.getUMLListCompartmentStyle());
		if (ls!=null) {
		EList fo = ls.getFilteredObjects(); // entries which are filtered OUT!
		for (Iterator iter = fo.iterator(); iter.hasNext();) {
			EObject e = (EObject) iter.next();
			utils.p("FilterContent: "+e);
		}
		}

		if (ls!=null) {
		EList fo = ls.getSortedObjects(); // sorted entries or none if no sorting applied
		for (Iterator iter = fo.iterator(); iter.hasNext();) {
			EObject e = (EObject) iter.next();
			utils.p("SortContent: "+e);
		}
		
		}
		
		EList children=v.getChildren();
//		utils.p("Children:"+children);
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Node n = (Node) iter.next();
			dumpNode(n);
//			utils.p("Element: "+n.getElement()); // just gets compartment owner
			
		}
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}