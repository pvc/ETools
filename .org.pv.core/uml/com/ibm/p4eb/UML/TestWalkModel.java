package com.ibm.p4eb.UML;
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
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.pv.core.Utils;

 


public class TestWalkModel    {
	
	Utils utils=Utils.getSingleton();
	final EClass newType=UMLPackage.eINSTANCE.getComponent();
//	final Stereotype stereo;
	String stereoName="Profile::Stereotype1";

	public boolean processItem(EObject o, int level) throws CoreException {
		if (o==null) {utils.log("Null object passed to ProcessItem - ignored");return false;}
//		utils.log(o);
		char[] tarray=new char[level];
		Arrays.fill(tarray, '\t');
		String indent= new String(tarray);
		boolean isne=true;
		String name="";
//		NamedElement ne = null;
		if (o instanceof NamedElement) {name=((NamedElement)o).getName();}
		else if (o instanceof Diagram) {name=((Diagram)o).getName();}
//		try {ne=(NamedElement)o;name=ne.getName();} catch(ClassCastException ex) {};
		if ((name==null) || name.length()==0) {name="<noName>";}
		String type=" ("+o.eClass().getName()+')';
		utils.log(indent+name+type);
		if (o instanceof Diagram) {
//			CopyToImageUtil cpy = new CopyToImageUtil();
//			cpy.copyToImage((Diagram)o, new Path("C:/temp/"+name+".png") , ImageFileFormat.PNG, new NullProgressMonitor(),null);
//			utils.log("Diagram saved to image");
			return false;}
		
		return true;
	}
	
	public boolean processTree(EObject o, int level) {
//		utils.log(o);
		boolean rsp;
		try {
			rsp = processItem(o, level);
			if (!rsp) {return true;} // prune this item but not the subtree it is contained within 
		} catch (CoreException e) {e.printStackTrace(utils.getLogger());return false;}
		
		EList oelist=o.eContents(); // getOwnedElements is unreliable (misses attributes of components, +activities)
//		utils.log("Elements="+oelist.size());
		for (Iterator iter = oelist.iterator(); iter.hasNext();) {
			EObject o2 = (EObject)iter.next();
			rsp=processTree(o2,level+1);
			if (!rsp) {break;} // prune the rest of this subtree
		}
		return true; // note this does not allow any upward pruning
	}
	
	public void run() {
		final NamedElement e;
		EObject eo=utils.getSelectedUMLElement();
		if (eo==null) {utils.log("No selection");return;}
		if (eo instanceof View) { EObject eo2=((View)eo).getElement(); if (eo2!=null) {eo=eo2;} } //allows diagrams from main tree, + views from diag
//		e=(NamedElement)eo;
//		final Stereotype stereo=e.getApplicableStereotype(stereoName);
		processTree(eo,0);
//		utils.log(e.eContainer());
//		utils.log(e.eClass().getName());
//		utils.log(newType);
//		utils.log("Is equal: "+(newType==e.eClass())); // returns true
//		utils.log(e.eContents()); // includes annotations (stereos + diagrams)
//		EList oelist=e.getOwnedElements();
		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}