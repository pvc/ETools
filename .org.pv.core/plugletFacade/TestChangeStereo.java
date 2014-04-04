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
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;


import org.eclipse.gmf.runtime.emf.core.ResourceSetModifyOperation;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.View;
import org.pv.core.Utils;
 


public class TestChangeStereo    {
	
	Utils utils=Utils.getSingleton();


	
	public void plugletmain(String[] args) {
		final Element e;
		EObject eo=utils.getSelectedUMLElement();
		if (eo==null) {utils.log("No selection");return;}
		if (eo instanceof View) { eo=((View)eo).getElement(); }
		 e=(Element)eo;
		EList ss=e.getAppliedStereotypes();
		if (ss.isEmpty()) {utils.log("No Stereotypes applied"); return;}
		final Stereotype s=(Stereotype)ss.toArray()[0];
		utils.log("FQName="+s.getQualifiedName());
		if (true) {return;}
		
		

//		EList atts=st.getAttributes();
//		Property p=(Property)atts.get(0);
//		utils.log("StAtt:"+p.getName()+" Default:"+p.getDefault()+" Actual:"+m.getValue(st,"nsPrefix"));
		
		ResourceSetModifyOperation op=new ResourceSetModifyOperation("ResetApplyProfile") {
			protected void execute(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				e.unapplyStereotype(s);
				EList as = e.getApplicableStereotypes();
				if (as.isEmpty()) {utils.log("No applicable stereotypes");return;}
				utils.log(as);
				Stereotype snew = (Stereotype)as.toArray()[0];
				e.applyStereotype(snew);
				return;
				
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