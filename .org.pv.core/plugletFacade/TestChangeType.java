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
import org.eclipse.uml2.uml.Class;


import org.eclipse.gmf.runtime.emf.core.ResourceSetModifyOperation;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.pv.core.Utils;
 


public class TestChangeType    {
	
	Utils utils=Utils.getSingleton();
	final EClass newType=UMLPackage.eINSTANCE.getComponent();
	
//	final Stereotype stereo;
	String stereoName="Profile::Stereotype1";
	Class container;
	Class oldContainer;
	Class newItem;
	EClass oldType;
	

	public boolean processItem(EObject o, int level) {
		//Processing condition 
		if (o.eClass()!=oldType) {utils.log("Not processed: "+o);return false;}
		// Processing
		String name=((NamedElement)o).getName();
	    newItem =(Class)container.createNestedClassifier(name,newType);
//		newItem.setName(name);
//		Package p=e.getNearestPackage();
//		PackageableElement c = p.createOwnedMember(newType);
//		c.applyProfile(stereo);
		return true;
	}
	
	public boolean processTree(EObject o, int level) {
//		utils.log(o);
		boolean rsp=processItem(o, level);
		if (!rsp) {return rsp;} // allow pruning
		
		EList oelist=o.eContents(); // getOwnedElements is unreliable (misses attributes of components, +activities)
		oldContainer=container; container=newItem;
		for (Iterator iter = oelist.iterator(); iter.hasNext();) {
			EObject o2 = (EObject)iter.next();
			rsp=processTree(o2,level+1);
			if (!rsp) {continue;} // prune this object
		}
		container=oldContainer;
		return true; // note this does not allow any upward pruning
	}
	
	public void plugletmain(String[] args) {
		final NamedElement e;
		EObject eo=utils.getSelectedUMLElement();
		if (eo==null) {utils.log("No selection");return;}
		if (eo instanceof View) { EObject eo2=((View)eo).getElement(); if (eo2!=null) {eo=eo2;} } 
		if (!(eo instanceof NamedElement)) {utils.log("Must be a named Element");return;}
		e=(NamedElement)eo;
	    oldType=e.eClass();
		
//		final Stereotype stereo=e.getApplicableStereotype(stereoName);
		
//		utils.log(e.eContainer());
//		utils.log(e.eClass().getName());
//		utils.log(newType);
//		utils.log("Is equal: "+(newType==e.eClass())); // returns true
//		utils.log(e.eContents()); // includes annotations (stereos + diagrams)
//		EList oelist=e.getOwnedElements();
		
		
//		if (true) {return;}
			
		ResourceSetModifyOperation op=new ResourceSetModifyOperation("ResetApplyProfile") {
			protected void execute(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				utils.log("Processing: "+e);
				container=(Class)e.getModel().createPackagedElement(e.getName()+"Copyroot",newType);
//				container.setName(e.getName()+"Copyroot");
				processTree(e,0);
				utils.log("Returning");
				if (true) {return;}
				
//				EClass u=UMLPackage.eINSTANCE.getUsage();
//				utils.log(u);
//				EClassifier u2=(EClassifier)UMLPackage.eINSTANCE.getEClassifier("Usage");
//				utils.log(u2);
//				Stereotype s=(Stereotype)m.getMember("Stereotype");
//				NamedElement ne=m.getMember("Class1");
//				utils.log(ne);
//				Stereotype s=ne.getAppliedStereotype("Profile::Stereotype");
//				EListss=ne.getAppliedStereotypes();
//				Stereotype s=(Stereotype)ss.toArray()[0];
//				for (Iterator iter = ss.iterator(); iter.hasNext();) {
//					Stereotype s = (Stereotype) iter.next();
//					utils.log(s);
//				}
//				utils.log(s);
//				NamedElement s1=m.getMember(s.getName());
//				utils.log("AppliedStereo="+s);
//				utils.log("Stereo="+s1);
//				EList mbrs=((Stereotype)s1).getMembers();
//				EList mbrs=s.getAttributes();
//				for (Iterator iter = mbrs.iterator(); iter.hasNext();) {
//					Element e = (Element) iter.next();
////					utils.log(e.getNearestPackage().getName()+"\t"+((NamedElement)e).getName());
//					String sname=((NamedElement)e).getName();
////					if (!sname.startsWith("base$")) {utils.log(sname+":"+ne.getValue(s,sname));}
//				}
				
	
//				Stereotype s = (Stereotype)m.createOwnedMember(UMLPackage.eINSTANCE.getStereotype()); 
//		        Extension extension = s.createExtension(UMLPackage.eINSTANCE.getComponent(), false); //not Required
//		        Extension extension = s.createExtension(m.getMember("Component").eClass(), false); //not Required
//		        Extension extension = s.createExtension(((EClass)(Class)uml2Metamodel.getOwnedMember("Component")), false); //not Required
//				try {
//					UMLModeler.saveProfile(m);
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					utils.log(e1);
//				}
			}
		};	
		
		try {
			UMLModeler.getEditingDomain().run( op ,new NullProgressMonitor());
		} catch (InvocationTargetException ex) {
			utils.log("An unexpected exception prevented the operation from completing successfully. See Error Log View for details."); //$NON-NLS-1$
			utils.log(e); //produces nothing ?pluglet
			throw new RuntimeException(ex.getCause());
		} catch (InterruptedException ex) {
			utils.log("The operation was interrupted"); //$NON-NLS-1$
		}

		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}