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
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.internal.ui.preferences.formatter.ProfileStore;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.osgi.framework.Bundle;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.pbe.graphs.Graph;
import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DOM2MetamodelApplier implements Executable {
	
	Utils utils=Utils.getSingleton();
	
	Properties nsMap=new Properties();
	Package nsPkg;
	char colon=':';
	char uscore='_';
	Bundle b=Platform.getBundle("com.ibm.pbe.umlToolkit");
	URL url=b.getEntry("/profiles/PBEMMProfile.epx"); //must include path, leading slash is optional
	URI profileURI=URI.createURI(url.toString());
	Stereotype stereotype;
	Stereotype xmlns;
	Stereotype profileStereotype;
	Enumeration types;
	Graph g;
	Model m;
	URI modelURI;
	String modelName;
	String profileName;
	Package profile;
	Document d;
	Map config;
	/**
	 * @param m
	 */
	public DOM2MetamodelApplier() {}
	
	public DOM2MetamodelApplier(Document doc, String filepath, String profileName) {
		modelURI=utils.string2URI(filepath);
		this.profileName=profileName;
		d=doc;
//		utils.dump(d);
	}
	
	
//	public void run() {
//		modelURI= utils.string2URI("InputModels/Metamodel.emx");
//		Document d=null;
//		TxWrapper tx=new TxWrapper(this);
//		Model m2=(Model)tx.execute(null);
//		utils.save(m2, modelURI);
//		utils.log(m.getMember("Stereotype").getOwner());
//		
//	}
//	
	
	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return execute();
	}
	
	
	public Package execute() {
		utils.log("Entering: "+this.getClass().getName());
		if (m==null) {
			try {
				m= UMLModeler.openModel(modelURI);
				utils.log("Existing DSL MetaModel successfully opened: "+m.getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				utils.log("Creating a new DSL Metamodel at: "+modelURI);
				m= UMLModeler.createModel(modelURI);
				if (modelName==null) {modelName=modelURI.trimFileExtension().lastSegment();}
				m.setName(modelName);
				m.createNestedPackage("NameSpaces");
				//				utils.log("Model created: "+m);
				Profile p=null;
				try {
					p = UMLModeler.openProfile(profileURI);
//					utils.log("Opened profile at: "+profileURI);
//					utils.log("Members: "+p.getMembers().size());
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					utils.log(e3);
					return null;
				}
				//				utils.log("Loaded Profile: "+p);
				//				utils.log("Trying apply of "+p);
				try {m.applyProfile(p);} catch (Exception e2) {utils.log("Profile apply problem: "+e2);} 
//				utils.log("Profile applied!");
//				utils.save(m);
			}
		}
		Profile p = m.getAppliedProfile("Profile");
		stereotype = p.getOwnedStereotype("Stereotype");
		xmlns = p.getOwnedStereotype("xmlns");
		profileStereotype = p.getOwnedStereotype("Profile");
		types=(Enumeration)p.getOwnedMember("Types"); // allowed extension types
//		utils.log("Found: "+stereotype);
//		utils.log("Found: "+xmlns);
//		utils.log("Found: "+profileStereotype);
//		utils.log("Found enum: "+types);
		nsPkg=m.getNestedPackage("NameSpaces");
		if (nsPkg==null) {nsPkg=(Package)m.createNestedPackage("NameSpaces");}
		for (Iterator iter = nsPkg.getOwnedMembers().iterator(); iter.hasNext();) {
			NamedElement ne = (NamedElement) iter.next();
			nsMap.put(ne.getName(),ne.getValue(xmlns,"nsPrefix"));
		}
		processDoc(d); 
		utils.save(m);
		return profile;
	}
	public Package processDoc(Document doc) {
		profile=cgetPackage(m,profileName);
//		utils.log("Profile="+profile+", "+profileStereotype);
		if (!hasStereotype(profile, profileStereotype)) {profile.applyStereotype(profileStereotype);}
		Class root=processNode(doc.getDocumentElement()); // ignore patternApp node
		link(profile,root);
		utils.log("Doc processed");
		return profile;
	}
	
	
	/**
	 * @param profile2
	 * @param root
	 */
	private boolean link(NamedElement parent, NamedElement child) {
		boolean linked=false;
		for (Iterator iter = parent.getClientDependencies().iterator(); iter.hasNext();) {
			Dependency d = (Dependency) iter.next();
			if (d.getSuppliers().get(0)==child) {linked=true;break;}
		}
		if (!linked) {
			Dependency d = parent.createDependency(child);
			return true;
		} else {return false;}
	}
	
	public Class processNode(Node node) {
		String showAs="class";
		utils.log("Processing document node: "+node.getNodeName());
		Class c=null;
		
		String name=node.getNodeName();
//		String lName=node.getLocalName();
//		String pfx=getStdPrefix(node);
//		if (pfx==null) {name=lName;} else {name=pfx+"_"+lName;}
		c=cgetClass(profile,name);
		stereotype(c);
		//			if (c==null) {
		//				c = (Class)m.createOwnedMember(UMLPackage.eINSTANCE.getClass_());
		//				c.setName(name);
		//				//			utils.log("xmlns "+xmlns);
		//				//			utils.log("pfx:"+pfx+":");
		//				c.setValue(xmlns,"nsPrefix",node.getPrefix());
		//			}
		
		
		if (node.hasAttributes()) {
			utils.log("Processing attributes");
			NamedNodeMap map=node.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				Node n = map.item(i);
				//				String attName=n.getNodeName().replace(colon,uscore);
				String attName=n.getLocalName();
				utils.log("Processing attribute: "+n.getNodeName());
				if (attName==null) {continue;}
				String nodePrefix=n.getPrefix();
				if ("name".equalsIgnoreCase(attName)) {continue;}
				if ("showAs".equalsIgnoreCase(attName)) {
					showAs=n.getNodeValue();
					utils.log("Showas type="+showAs);
					c.setValue(stereotype,"Extends",types.getOwnedLiteral("Attribute"));
					utils.log("+++Extends set to:"+c.getValue(stereotype,"Extends"));
					continue;
				}
				if ("xmlns".equalsIgnoreCase(nodePrefix)) {
					String uri=n.getNodeValue();
					String stdPrefix=nsMap.getProperty(uri);
					if (stdPrefix==null) {addNamespace(uri,attName); }
					//					Package p=(Package)m.getOwnedMember(attName);
					//					if (p==null) {
					//						utils.log("Creating namespace: "+attName+"::"+n.getNamespaceURI());
					//						p = (Package)m.createOwnedMember(UMLPackage.eINSTANCE.getPackage());
					//						p.setName(attName);
					//						p.setValue(xmlns,"nsPrefix",n.getNodeValue()); // bad practice - misuse of nsPrefix to store nsURI
					//						continue;
					//					}
				}
				if ("xmlns".equalsIgnoreCase(attName)) {
					utils.log("Warning: Default namespaces are not supported and will be ignored in: "+c.getName());
					continue;
				}
				
				Property att=c.getAttribute(attName,null);
				if (att==null) {
					att = c.createOwnedAttribute(attName,(Type)m.getImportedMember("String"));
//					att.setName(attName);
//					att.setType((Type)m.getMember("String"));
					if (null!=nodePrefix) {att.setValue(xmlns,"nsPrefix",nodePrefix);}
					utils.log("  Added new attribute: "+att.getName());
				}
			}
		}
		if (node.hasChildNodes()) {
			utils.log("Processing ChildNodes of "+node.getNodeName());
			NodeList nodes = node.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				//				utils.log("  Node: "+i+n.getNodeName()+" Type: "+n.getNodeType());
				if (n.getNodeType()!=Node.ELEMENT_NODE) {continue;}
				//				utils.log("  Processing as Element");
				Class child=processNode(n);
				
				utils.log("Creating link - parent:"+c.getName()+ " child:"+child.getName());
				link(c,child);
			}
		}
		utils.log("Completed processing of Stereotype: "+node.getNodeName());
		utils.log("Stereotype Target set to: "+c.getValue(stereotype,"Extends"));
		utils.log("Stereotype will be diagrammed as a "+showAs);
		return c;
	}
	/**
	 * @param c
	 */
	private void stereotype(Element e, String targetType) {
		if (!hasStereotype(e,stereotype)) { 
			try {e.applyStereotype(stereotype);
			EnumerationLiteral type = types.getOwnedLiteral(targetType);
			if (type==null) {type=types.getOwnedLiteral("@any");}
			e.setValue(stereotype,"Extends",type); //default
			} catch (Exception e2) {e2.printStackTrace(utils.getLogger());}
		}
	}
	private void stereotype(Element e) {
		stereotype(e,"Class");
	}
	public boolean hasStereotype(Element e, Stereotype s) {
		return (e.getAppliedStereotype(s.getQualifiedName())!=null);
	}
	
	/**
	 * @param prefix
	 * @return
	 */
	private String getStdPrefix(Node node) {
		String uri=node.getNamespaceURI();
		//		utils.p("Namespace URI="+uri);
		if (uri==null) {return null;}
		String stdPrefix=nsMap.getProperty(uri);
		if (stdPrefix==null) {stdPrefix=node.getPrefix(); addNamespace(uri,stdPrefix); utils.p("Undeclared namespace:"+stdPrefix);}
		return stdPrefix;
	}
	/**
	 * @param uri
	 * @param stdPrefix
	 */
	private void addNamespace(String uri, String stdPrefix) {
		nsMap.setProperty(uri,stdPrefix);
		Class newNs=(Class)nsPkg.createOwnedClass(uri,false); // default to concrete
		newNs.setName(uri);
		//		newNs.applyProfile(xmlns); // requires stereo so already applied!
		newNs.setValue(xmlns,"nsPrefix",stdPrefix);
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
	public Class getClass(Package p,String name) {
		NamedElement ne=p.getMember(name);
		if (ne instanceof Class) {return (Class)ne;} else {return null;}
	}
	public Class cgetClass(Package p,String name) {
		Class c;
		c=getClass(p,name);
		if (c!=null) {return c;} else {return addClass(p,name);}
	}
	public Class addClass(Package p,String name) {
		PackageableElement pe = p.createOwnedClass(name,false);
		return (Class)pe;
	}
	public Property getAttr(Class c,String name) {
		return c.getAttribute(name,null);
	}
	public Property cgetAttr(Class c,String name) {
		Property p=getAttr(c,name);
		if (p!=null) {return p;} else {return addAttr(c,name);}
	}
	public Property addAttr(Class c,String name) {
		Property pe = c.createOwnedAttribute(name,(Type)m.getImportedMember("String"));
		pe.setType((Type)m.getMember("String"));
		return (Property)pe;
	}
	public Package getPackage(Package p,String name) {
		NamedElement ne=p.getMember(name);
		if (ne instanceof Package) {return (Package)ne;} else {return null;}
	}
	public Package cgetPackage(Package p,String name) {
		Package c;
		c=getPackage(p,name);
		if (c!=null) {return c;} else {return addPackage(p,name);}
	}
	public Package addPackage(Package p,String name) {
		Package pe = p.createNestedPackage(name);
//		pe.setName(name);
		return pe;
	}
	
	
	public void setType(TypedElement p, String typeName) {
		p.setType((Type)m.getMember(typeName));
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
