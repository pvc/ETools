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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.pv.core.Utils;

 


public class GetElementDetails    {
	
	Utils utils=Utils.getSingleton();
	final EClass newType=UMLPackage.eINSTANCE.getComponent();
//	final Stereotype stereo;
	String stereoName="Profile::Stereotype1";

	
	
	
	
	public void plugletmain(String[] args) {
		final NamedElement e;
		EObject eo=utils.getSelectedUMLElement();
		if (eo==null) {utils.log("No selection");return;}
		if (eo instanceof View) { EObject eo2=((View)eo).getElement(); if (eo2!=null) {eo=eo2;} } //allows diagrams from main tree, + views from diag
		utils.p("**********");
		utils.p(eo);
		utils.p("Container: "+eo.eContainer());
		utils.p("Containing Feature: "+eo.eContainingFeature());
		utils.p("Contents: "+eo.eContents());
		utils.p("Containment Feature: "+eo.eContainmentFeature());
		utils.p("*");
		EClass ec=eo.eClass();
		utils.p("ClassifierID: "+ec.getClassifierID());
		utils.p("Instance name: "+ec.getInstanceClassName());
		utils.p("EPackage: "+ec.getEPackage());
		utils.p("Features: "+ec.getEStructuralFeatures());
		
		if (true) {return;}
			
	


		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}