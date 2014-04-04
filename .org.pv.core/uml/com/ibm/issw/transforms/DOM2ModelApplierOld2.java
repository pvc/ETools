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
 * Created on 13-Dec-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.transforms;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.osgi.framework.Bundle;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DOM2ModelApplierOld2 implements Executable {
	
	Utils utils=Utils.getSingleton();
	Map profileMap;
	//	long uid=System.currentTimeMillis();
	String uidString=null;
	//	URI profileURI= utils.string2URI("GenProfile/TestMetamodel.epx");
	//	Profile profile;
	//	Stereotype stereotype;
	Model m;
	Document d;
	Profile dslProfile=null;
	Class rootClass;
	EMap properties;
	URI modelURI=null;
	Bundle b=Platform.getBundle("com.ibm.pbe.umlToolkit");
	URL url=b.getEntry("/profiles/PBEProfile.epx"); //must include path, leading slash is optional
//	URI profileURI=URI.createURI(url.toString());
	URI profileURI=URI.createURI("pathmap://PBEProfiles/PBEProfile.epx");
	private URI dslProfileURI;
	
	
	char colon=':';
	char uscore='_';
	private String modelName;
	private String patternName;
	String defaultProfile;
	List collisions=new ArrayList();
	private Package root;
	/**
	 * @param o
	 */
	public DOM2ModelApplierOld2() {
		this.m=UMLFactory.eINSTANCE.createModel();
	}
	public DOM2ModelApplierOld2(Document doc, Model targetModel) {
		this.m=targetModel;
		this.d=doc;
	}
	
//	public DOM2ModelApplier(Document doc, Model targetModel, Profile dslProfile){
////		dslProfileURI=utils.string2URI(profilePath);
////		modelURI=utils.string2URI(modelPath);
//		this.m=targetModel;
//		this.dslProfile=dslProfile;
//		this.d=doc;
//	}
	
	
	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return execute();
	}
	public void run() {execute(null);}
	
	public NamedElement execute() {
		utils.log("Entering: "+this.getClass().getName());
//		try {
//			m=utils.getModel(modelURI);
//		} catch (IllegalArgumentException e) { // ignore
//		} catch (Exception e) {
//			e.printStackTrace(utils.getLogger());
//		}
//		if (dslProfile!=null) {m.applyProfile(dslProfile);}
		EList profs=m.getAppliedProfiles();
		
		profileMap=new HashMap();
		boolean hasPBEProfile=false;
		for (Iterator iter = profs.iterator(); iter.hasNext();) {
			Profile prof = (Profile)iter.next();
			profileMap.put(prof.getName(),prof);
			if ((prof.hasKeyword("DSL")) && !(prof.getName().equals("PBEProfile")) ) {defaultProfile=prof.getName(); utils.log("Metamodel found:"+defaultProfile);}
			if ((prof.hasKeyword("DSL")) && (prof.getName().equals("PBEProfile")) ) {hasPBEProfile=true;}
		}
		int step=1;
		try {
			if (defaultProfile==null) {
				utils.log("DSL Profile: "+dslProfileURI+" not found -- cannot contine");
				return null;
//				Profile	p;
//				p = UMLModeler.openProfile(dslProfileURI);
//				m.applyProfile(p);
//				profileMap.put(p.getName(),p);
//				defaultProfile=p.getName();
//				utils.log("Applied "+p);
			}
			step++;
			if (!hasPBEProfile) {
				Profile	p = UMLModeler.openProfile(profileURI);
				m.applyProfile(p);
				profileMap.put(p.getName(),p);
				utils.log("Applied "+p);
			}
		} catch (Exception e1) {
			//			e1.printStackTrace(utils.getLogger());
			if (step==1) {utils.log("DSL Profile: "+dslProfileURI+" not found -- cannot contine");}
			if (step==2) {utils.log("PBE Profile not found - cannot continue");}
			return null;
		}
		if (defaultProfile==null) {utils.log("***Error - model "+m.getName()+" has no associated DSL Profile");}
		processNode(d.getDocumentElement(),null); // discard doc element
		if (collisions.size()>0) {
			utils.log("The following elements were renamed due to naming collisions:");
			utils.dump(collisions);
		}
		return rootClass; // set in processNode by element with null parent
	}
	
	public void processNode(Node node,NamedElement parent) {
		boolean match=false;
		utils.log("***Processing Type: <"+node.getNodeName()+"> with parent: "+parent);
//		utils.log("Parent="+parent);
		//		utils.log(node.toString());
		//		utils.log(node.getNodeValue());
		//		utils.log("Type:"+node.getNodeType());
		NamedElement c=null; Property cp=null;NamedNodeMap nodeMap=null;
		Stereotype s=null;
		String name=null;
		if (node==d) {
			//			name="DocumentRoot";
			//			Element root=d.getDocumentElement();
			//			modelName=root.getAttribute("name");
			//			patternName=root.getAttribute("pattern");
			//			if (patternName.equals("")) {patternName=modelName;} else {patternName=patternName.substring(patternName.lastIndexOf('.')+1);}
			
		}
		else {
			String lName=node.getNodeName();
			String nodePrefix=node.getPrefix();
			if (nodePrefix==null) {name=lName;} else {name=nodePrefix+"_"+lName;}
			//			utils.log("Name set to:"+name);
		}
		
		
		if (node==d) {
		} else {
			String umlName=null;Node nameAtt; 
			if (node.hasAttributes()) {
				nodeMap=node.getAttributes();
				utils.log("attributes found:"+nodeMap.getLength());
				nameAtt=nodeMap.getNamedItem("name");
				if (nameAtt!=null) {umlName=nameAtt.getNodeValue();}
			}
			//			if ((umlName!=null)  &&  (parent!=null)) {
			if ((umlName!=null)) {
				//			c=(Class)m.getOwnedMember(umlName);
				//				EList cds=parent.getClientDependencies();
				//				utils.log("* Suppliers of "+parent.getName());
				//				for (Iterator iter = cds.iterator(); iter.hasNext();) {
				//					Dependency cd = (Dependency) iter.next();
				//					utils.log(cd.getSuppliers().get(0));
				//				} 
				//				utils.log("*");
			} else {umlName=node.getLocalName()+'_'+getUid();}
			//			utils.log("Processing doc element: "+node.getNodeName());
			//		String ns=node.getNamespaceURI();  
			utils.log("Name="+umlName);
			
			String ns=null;
//			utils.log(name);
			if ("metadata".equals(name)) {
				ns="PBEProfile";
				NamedElement temp = m.getOwnedMember(umlName);
//				utils.log(temp);
				if (temp==null) {root=m.createNestedPackage(umlName);}
				else {if (! (temp instanceof Package) ) {utils.log("Error - Model already contains an element named "+name+": delete and retry"); return;}
				else {root=(Package)temp;}
				} 
//				utils.log("Root package is:"+root);
				if (root==null) {utils.log("Problem creating root package - please close & reopen project to clear UML cache"); return;}
			}
			boolean uml=false;
			if ( ("Attribute".equals(name)) || ("Class".equals(name)) ) {uml=true;}
			if (!uml) {
				if (ns==null) {ns=defaultProfile;}
//				utils.log("Getting profile: "+ns);
				Profile p=(Profile)profileMap.get(ns);
				if (p==null) {utils.log("Error - Required Profile not found: "+ns);}
				else {s=p.getOwnedStereotype(name);}
				//			utils.log("Using Profile: "+p.getName());
				if (s==null) {utils.log("Error: Required Stereotype does not exist: "+name);}
				//			Stereotype s=c.getApplicableStereotype("DomainLanguage::"+node.getNodeName()); // assumes only one, so must be unique!
			}
			String target = "Class";
			//			utils.log(s);
			if (s==null) {if ("Attribute".equals(name)) {target = "Property";} } else {
				EList exts = s.getAllExtendedMetaclasses();
				if (exts.isEmpty()) {utils.log("Error - No target defined on stereotype");} else {
					NamedElement ext=(NamedElement)exts.toArray()[0];
					target = ext.getName();
					//					utils.log("Metatype set to "+target.getName());
				}
			}
			
			utils.log("Metatype="+target);
			boolean showAsProperty=false;
			if (target=="Property") {
				showAsProperty=true;
				Class parentClass = (Class)parent;
				cp=parentClass.getAttribute(umlName,null);
				if (cp==null) {cp=parentClass.createOwnedAttribute(umlName,null);}
				if (s!=null) {
					utils.log("StereoQN:"+s.getQualifiedName());
					cp.getAppliedStereotype(s.getQualifiedName());
					try {cp.applyStereotype(s); utils.log("Stereotype successfully applied");} catch(Exception e) {utils.log("Stereotype NOT applied: "+e);}
				}
			} else {
				boolean exists=true;
				c = root.getOwnedMember(umlName);  // no efficient way to retrieve parent of c - instead examine sibs of new 
				if (c==null) {exists=false;}
				if (exists) {
					match=false;
					if (parent==null) {match=true;} else {
						EList dps = parent.getClientDependencies();
						for (Iterator iter = dps.iterator(); iter.hasNext();) {
							Dependency dp = (Dependency) iter.next();
							//					utils.log("Processing dep: "+dp);
							EList sps=dp.getSuppliers();
							utils.log("Checking for match with same parent");
							for (Iterator it2 = sps.iterator(); it2.hasNext();) {
								NamedElement ne = (NamedElement) it2.next();
								if (ne.getName().equals(umlName)) {match=true;utils.log("Successful match");break;}
							}
							if (match) {break;}
						}
					}
					if (!match) {exists=false; collisions.add(new CollisionDescriptor(umlName,umlName+=("."+getUid())));}
				}
				if (!exists) {
					utils.log("Creating new model element");
					c = root.createOwnedClass(umlName,false);
//					c.setName(umlName);
					if (parent!=null) {
						Dependency dp = parent.createDependency(c);
//						dp.getClients().add(parent);
//						dp.getSuppliers().add(c);
					} 
				}
				if (s!=null && null==c.getStereotypeApplication(s)) {  
					try {c.applyStereotype(s);utils.log("Stereotype successfully applied");} catch(IllegalArgumentException e) {} catch(Exception e) {utils.log("Stereotype NOT applied: "+e);}
				}
				if (parent==null) {rootClass=(Class)c;}
			}

			if (s==null) {}
			else {
				utils.log("Processing stereotype values for "+s.getName());
				if (nodeMap!=null) {
					for (int i = 0; i < nodeMap.getLength(); i++) {
						Node n = nodeMap.item(i);
						if ("xmlns".equalsIgnoreCase(n.getPrefix())) {continue;}
						//					String attName=n.getNodeName().replace(colon,uscore);
						String attName=n.getNodeName();
						if (attName==null) {continue;}
						//					utils.log("FullNodeName="+fullNodeName);
						//					String attName=fullNodeName.substring(fullNodeName.indexOf(':')+1);

						if ("name".equalsIgnoreCase(attName)) {continue;}
						if ("showAs".equalsIgnoreCase(attName)) {continue;}
//						utils.log("Processing value: "+attName+"="+n.getNodeValue());
						utils.log("Setting " +n);
						try{
							if (showAsProperty){
								utils.log("Class cp is: "+cp);
								cp.setValue(s,attName,n.getNodeValue());
							} else {
								utils.log("Class c is: "+c);
								c.setValue(s,attName,n.getNodeValue());
							}
//							utils.log("Value successfully set");
						} catch (Exception e) {utils.log(e);utils.log("SetValue error: "+s.getName()+':'+attName+"="+n.getNodeValue());}
					}
				}
			}
		}


		if (node.hasChildNodes()) {
			NodeList nodes = node.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				//				utils.log("Node: "+i+n.getNodeName()+" Type: "+n.getNodeType());
				if (n.getNodeType()!=Node.ELEMENT_NODE) {continue;}
				//				utils.log("Is Element");
				processNode(n,c);
				//				child.setValue(s,"Order",String.valueOf(i));  // not yet defined in stereos
				//				if (child.getClientDependencies().isEmpty()) {

				//				}
			}
		}
		//		} catch (Exception e) {e.printStackTrace(utils.getLogger());}

		return;
	}
	/**
	 * @return
	 */
	private String getUid() {
		if (uidString==null) {
			EAnnotation ea =m.getEAnnotation("properties");
			if (ea==null) {m.createEAnnotation("properties");ea =m.getEAnnotation("properties");}
			properties = ea.getDetails();
			uidString = (String)properties.get("uid");
			if (uidString==null) {uidString="0";}
		}
		int	uid=Integer.parseInt(uidString)+1;
		uidString=""+uid;
		properties.put("uid",uidString);
		utils.log("uid set to: "+uid);
		return uidString;

	}

	public void dumpNode(Node node) {
		Node p2=node.getParentNode();
		String p2Name="";
		if (p2!=null) {p2Name=p2.getNodeName();}
		utils.log(p2Name+"::"+node.getNodeName());
		if (!node.hasAttributes()) {utils.log("No attributes");} 
		else {
			NamedNodeMap map=node.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				Node n = map.item(i);
				utils.log(n.getNodeName()+"="+n.getNodeValue());
			}
		}
		if (!node.hasChildNodes()) {utils.log("No child nodes");} 
		else {
			NodeList nodes = node.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				dumpNode(n);
			}
		}
	}
	class CollisionDescriptor {
		String beforeName; String afterName;
		public CollisionDescriptor(String before, String after) {
			this.beforeName=before;
			this.afterName=after;
		}
		public String toString() {
			return beforeName+" renamed to "+afterName;
		}
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}

