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
 * Created on 30-Nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.transforms;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Extension;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.pv.core.Utils;

import com.ibm.pbe.graphs.Connector;
import com.ibm.pbe.graphs.Graph;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProfileGraph2ProfileApplier {
	Profile profile;
	Utils utils=Utils.getSingleton();
	
	public ProfileGraph2ProfileApplier(Profile p){
		this.profile=p;
	}
	public Profile execute(Graph g) {
//		profile.getPackageImports().clear();
//		profile.getOwnedMembers().clear();
		EList l1=profile.getImportedPackages();
		boolean found;
		found=false;
		for (Iterator iter = l1.iterator(); iter.hasNext();) {
			Package p = (Package) iter.next();
			if (p.getName().equals("UMLPrimitiveTypes")) {found=true;break;}
		}
		if (!found) {
			utils.log("Not Found: UMLPrimitiveTypes Library");
			Model uml2Library = (Model) utils.load(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
			profile.createPackageImport(uml2Library,VisibilityKind.PUBLIC_LITERAL);
		}
		
		EList l2=profile.getReferencedMetamodels();
		found=false;
		for (Iterator iter = l2.iterator(); iter.hasNext();) {
			Package p = (Package) iter.next();
			if (p.getName().equals("uml")) {found=true;break;}
		}
		if (!found) {
			utils.log("Not Found: uml MetaModel");
			Model uml2Metamodel = (Model) utils.load(URI.createURI(UMLResource.UML_METAMODEL_URI));
			profile.createMetamodelReference(uml2Metamodel);
		}
		
        
        HashMap xref=new HashMap();
        Collection stereotypes=g.getNodes(); // collection of steros to be added
        for (Iterator iter = stereotypes.iterator(); iter.hasNext();) {
			Class c = (Class) iter.next();
//			utils.log("Processing "+c);
			Stereotype stereo=c.getAppliedStereotype("Profile::Stereotype"); // is this one a stereo
			if (null==stereo) {utils.log(c.getName()+" is not a stereotype - ignored");}
			else {
			utils.log("Processing stereotype: "+c.getName());
				Stereotype s=profile.getOwnedStereotype(c.getName()); // does the profile have onhe already?
				if (s==null) {
				utils.log("Adding new stereotype to profile: "+c.getName());
				s = (Stereotype)profile.createOwnedStereotype(c.getName(),c.isAbstract()); //true=Abstarct
//				s.setIsAbstract(c.isAbstract());
//		        s.setName(c.getName());
				}
//				utils.log("Doing atts");
		        EList cAtts=c.getOwnedAttributes();
		        EList sAtts=s.getOwnedAttributes();
		        for (Iterator it2 = cAtts.iterator(); it2.hasNext();) {
					Property att = (Property) it2.next();
					if (att.getType()==null) {att.setType((Type)profile.getMember("String"));}
					Property sAtt=s.getOwnedAttribute(att.getName(),null);
					if (null!=sAtt) {
//						utils.log("Replacing: "+sAtt);
						if (sAtt.getType()!=att.getType()) {sAtt.setType(att.getType());utils.log(att.getName()+" type changed to: "+att.getType());}
					}
					else {sAtts.add(EcoreUtil.copy(att));utils.log("Added new attribute: "+att.getName());}
					
				}
//		        Collection attributes=EcoreUtil.copyAll(c.getOwnedAttributes());
//		        for (Iterator iterator = attributes.iterator(); iterator
//						.hasNext();) {
//					Property p = (Property) iterator.next();
//				}
//		        sAtts.addAll(attributes);
//		        utils.log("Doing Extension");
//		        Extension extension = s.createExtension(profile.getMember("Usage").eClass(), ((Boolean)c.getValue(stereo,"Required")).booleanValue()); //not Required
		        String xClass="Element";Boolean required=new Boolean(false);Boolean suppressed=new Boolean(false);
		        try {
//		        	utils.log("Atts of: "+stereo.getName());
//		        	EList atts=stereo.getFeatures();
//		        	for (Iterator iterator = attributes.iterator(); iterator
//							.hasNext();) {
//						Feature p = (Feature) iterator.next();
//						utils.log(p);
//					}
//		        	Object o=c.getValue(stereo,"Extends");
//		        	utils.log("Extends: "+o+o.getClass());
		        	required=(Boolean)c.getValue(stereo,"Required");
		        	xClass=((EnumerationLiteral)c.getValue(stereo,"Extends")).getName();
		        	if ("@any".equals(xClass)) {xClass="Element";}
		        	if ("Attribute".equals(xClass)) {xClass="Property";}
//		        	required=(Boolean)c.getValue(stereo,"Required");
		        } catch (Exception e) {utils.log("Problem retrieving properties from: "+s.getName());}
		        utils.log("Set to extend: "+xClass);
		        Extension extension=null;
//		        EClass xEClass = (EClass)UMLPackage.eINSTANCE.getEClassifier(xClass);
		        Class xEClass2 = (Class)profile.getImportedMember(xClass);
		        
		        if (xEClass2==null) {utils.log("Invalid extension class ignored: "+xClass);} else { 
		        	try {
		        		extension = s.createExtension(xEClass2, required.booleanValue()); //not Required
		        	} catch (IllegalArgumentException e) {
		        		//		        	utils.log("Stereotype Extension already exists");
		        	}
		        }
		        //		        utils.log("Created extension "+extension);
		        xref.put(c,s);
			}
		}
//        utils.log("Doing inheritance");
        Collection links=g.getConnectors();
        for (Iterator iter = links.iterator(); iter.hasNext();) {
			Connector c = (Connector) iter.next();
			if (c.getValue() instanceof Generalization) {
				try {
				((Class)xref.get(c.getSource())).createGeneralization((Class)xref.get(c.getTarget()));
				} catch (Exception e) {} // allow for non-stereotypes with Gen realtions!
			}
			
		}
        utils.log("Defining profile");
        profile.define();
        utils.log("VersionId="+profile.getDefinition().getNsURI());
        return profile;
	}
	
//	public Profile oldExecute() {
//		profile.getPackageImports().clear();
//		profile.getOwnedMembers().clear();
//		
//        Model uml2Library = (Model) utils.load(URI.createURI(UML2Resource.UML2_PRIMITIVE_TYPES_LIBRARY_URI));
//        Model uml2Metamodel = (Model) utils.load(URI.createURI(UML2Resource.UML2_METAMODEL_URI));
////        EClass targetClass=UMLPackage.eINSTANCE.getComponent();
////        utils.log(targetClass);
////        org.eclipse.uml2.uml.Class metaclass = (org.eclipse.uml2.uml.Class) uml2Metamodel.getOwnedType(targetClass.getName());
////        utils.log(metaclass+" has eclass: "+metaclass.eClass());
////        PrimitiveType primitiveType = (PrimitiveType) uml2Library.getOwnedType("Boolean");
//        profile.referenceMetamodel(uml2Metamodel);
//        profile.importPackage(VisibilityKind.PUBLIC_LITERAL, uml2Library);
//        
//
////        Stereotype stereotype = profile.createOwnedStereotype("Node", true); //true=Abstarct
//        Stereotype s = (Stereotype)profile.createOwnedMember(UMLPackage.eINSTANCE.getStereotype()); 
//        s.setIsAbstract(false);
//        s.setName("TestStereo");
////        Generalization generalization = specificClassifier.createGeneralization(generalClassifier);
////        Property attribute = s.createOwnedAttribute("Att1", primitiveType,0, MultiplicityElement.UNLIMITED_UPPER_BOUND );
//        Property a = s.createOwnedAttribute(UMLPackage.eINSTANCE.getProperty());
//        a.setType((Type)profile.getMember("Boolean"));
//        a.setUpperBound(MultiplicityElement.UNLIMITED_UPPER_BOUND);
//        a.setLowerBound(0);
//        a.setName("att1");
//        
//             
//        
////        Extension extension = s.createExtension(UMLPackage.eINSTANCE.getComponent(), false); //not Required
//        Extension extension = s.createExtension(profile.getMember("Component").eClass(), false); //not Required
////        Extension extension = s.createExtension(((EClass)(Class)uml2Metamodel.getOwnedMember("Component")), false); //not Required
////        profile.define();
//        
////		m.setName("NewModel");
//		profile.define();
////        try {
////			UMLModeler.saveProfileAs(profile,utils.string2URI("TestModel/EmptyProfile.epx"));
////		} catch (IOException e1) {
////			// TODO Auto-generated catch block
////			utils.log(e1);
////		}
//		utils.log("returning profile: "+profile);
//		return profile;
//
//	}
	
	
	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
