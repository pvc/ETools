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
 * Created on 23-Dec-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.UMLDiagrams;

import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.pv.core.Utils;

import com.ibm.xtools.uml.ui.diagram.IUMLDiagramHelper;
import com.ibm.xtools.umlnotation.UMLDiagramKind;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PackageDiagramBuilder implements Executable{
	Utils utils=Utils.getSingleton();
	String diagramName;
	int x;int y;
	
	public PackageDiagramBuilder(){
	}
	
	public PackageDiagramBuilder(String name){
		diagramName=name;
	}
	public Diagram execute(Package m) {   // cant return root node with this algorithm (can do general graphs)
	int x=7500;int y=2000;
	utils.log("Creating Diagram for model: "+m);
	IUMLDiagramHelper dh = UMLModeler.getUMLDiagramHelper();
	Diagram d=dh.getMainDiagram(m);
//	Diagram d=null;
	if (d==null) {
		d=dh.createDiagram(m,UMLDiagramKind.CLASS_LITERAL); 
		//	dh.openDiagramEditor(d);
		dh.setMainDiagram(m,d);
		d.setName(m.getName());
		utils.log("created new diag: "+d);
	}
	if (diagramName!=null) {d.setName(diagramName);}
	d.getChildren().clear(); // NB This will destroy layout!
	EList mbrs=m.getOwnedMembers();
	Hashtable map=new Hashtable();

	for (Iterator iter = mbrs.iterator(); iter.hasNext();) {
		 Element e = (Element)iter.next();
		 if (e instanceof Class) {
//		 	utils.log("Processing Class: "+e);
		 	Node v=dh.createNode(d,e);
		 	map.put(e,v);
//		 	EList styles=v.getStyles();
//		 	for (Iterator iterator = styles.iterator(); iterator.hasNext();) {
//				Style s= (Style) iterator.next();
//				utils.log("Style: "+s);
//			}
		 	Bounds b = (Bounds)v.getLayoutConstraint();
//		 	utils.log("Layout: "+b);
		 	b.setX(x); x=x+2500;
		 	b.setY(y); y=y+1000;
		 	
		 }
	}
	for (Iterator iter = mbrs.iterator(); iter.hasNext();) {
		 Element e = (Element)iter.next();
		 if (e instanceof Dependency) {
//		 	utils.log("Processing dependency: "+e);
		 	Dependency dep=(Dependency)e;
//		 	utils.log("Getting source");
		 	Node s=(Node)map.get(dep.getClients().get(0));
		 	Node t=(Node)map.get(dep.getSuppliers().get(0));
//		 	utils.log("Creating edge");
		 	dh.createEdge(t,s,dep);
//		 	utils.log("Created edge");
		 }
	}
	
// 	Edge edge=d.createEdge();
// 	utils.log("Edge: "+edge);
// }
// utils.log("?Class: "+(e.eClass().getName().equals("Class")));
// if (e.eClass()=Class) {}
// if (o instanceof Class)
//	utils.log("creating view");
//	View v=d.createChild(app.eClass()); // NO - relates to child containers of node
//	View v=dh.createNode(d,app);
//	utils.log("Created: "+v);
	

	return d;
	}

	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return (Diagram)execute((Package) o);
	}
	/**
	 * @return Returns the diagramName.
	 */
	public String getDiagramName() {
		return diagramName;
	}
	/**
	 * @param diagramName The diagramName to set.
	 */
	public void setDiagramName(String diagramName) {
		this.diagramName = diagramName;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
