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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.uml2.uml.UMLPackage;


import org.eclipse.gmf.runtime.emf.core.ResourceSetModifyOperation;
import org.pv.core.Utils;

import com.ibm.xtools.modeler.ui.UMLModeler;
 


public class RunEMFOperation    {
	
	Utils utils=Utils.getSingleton();
	
	ResourceSetModifyOperation op=new DeleteProfiles("Deleting Profiles!");
//	ResourceSetModifyOperation op=new CreateInternalProfile("CreateInternalProfile");

	public void showAllUMLClassifiers() {
	EList l=UMLPackage.eINSTANCE.getEClassifiers();
	for (Iterator iter = l.iterator(); iter.hasNext();) {
		EClassifier c = (EClassifier) iter.next();
		if (c instanceof EClass && !((EClass)c).isAbstract()) {	utils.log(c.getName());}
	}
}
	
	public void plugletmain(String[] args) {
		
		try {
			UMLModeler.getEditingDomain().run( op ,new NullProgressMonitor());
		} catch (InvocationTargetException e) {
			utils.log("An unexpected exception prevented the operation from completing successfully. See Error Log View for details."); //$NON-NLS-1$
			utils.log(e); //produces nothing ?pluglet
			throw new RuntimeException(e.getCause());
		} catch (InterruptedException e) {
			utils.log("The operation was interrupted"); //$NON-NLS-1$
		}

		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}