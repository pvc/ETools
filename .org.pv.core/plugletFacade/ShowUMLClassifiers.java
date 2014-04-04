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
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;


import org.eclipse.gmf.runtime.emf.core.ResourceSetModifyOperation;
import org.pv.core.Utils;

import com.ibm.xtools.modeler.ui.UMLModeler;
 


public class ShowUMLClassifiers    {
	
	Utils utils=Utils.getSingleton();

	
	
	
	public void plugletmain(String[] args) {
		EList l=UMLPackage.eINSTANCE.getEClassifiers();
		int count=0;
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			EClassifier c = (EClassifier) iter.next();
			if (c instanceof EClass && !((EClass)c).isAbstract()) {	utils.log(c.getName());} 
			else {utils.log("***"+c.getName()+"::"+c.eClass().getName());}
//			else {continue;}
			count++;
		}
		utils.log("Count: "+count);
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}