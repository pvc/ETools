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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.graphics.Point;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;

import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode;
import org.pv.core.Utils;

import com.ibm.xtools.uml.ui.diagram.IUMLDiagramHelper;
import com.ibm.xtools.umlnotation.UMLDiagramKind;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PackageTreeDiagramBuilder implements Executable{
	Utils utils=Utils.getSingleton();
	String diagramName;
	int x;int y;
	int level=0;
	int startX=7500;int startY=2000;int spaceX=3000;int spaceY=1750;
//	Node root;
	Hashtable map=new Hashtable();
	HashSet nodesDone=new HashSet();
	Hashtable frames=new Hashtable();
	EList mbrs;
	PackageableElement treeRoot;
	
	public PackageTreeDiagramBuilder(){
	}
	
	public PackageTreeDiagramBuilder(String name){
		diagramName=name;
	}
	//	public Node getRoot() {
	//	DiagramAnalyser da = new DiagramAnalyser(d);
	//	Node root=da.getRoot();
	//	if (root==null) {utils.log("Warning: This diagram is not a Tree!: " +d.getName()); return d;}
	//	return root;
	//	}
	
	public Diagram execute(Package m) {   // cant return root node with this algorithm (can do general graphs)
		if (m==null) {return null;}
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
		d.getEdges().clear();
		NamedElement rootElement = m.getOwnedMember("DocumentRoot");
		if (rootElement==null) {utils.log("Could not find DocumentRoot element - cannot create tree in" +d.getName()); return d;}
		
		
		mbrs=m.getOwnedMembers();
		
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
				//		 	Bounds b = (Bounds)v.getLayoutConstraint();
				////		 	utils.log("Layout: "+b);
				//		 	b.setX(x); x=x+2500;
				//		 	b.setY(y); y=y+1000;
				
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
		
		//	DiagramAnalyser da = new DiagramAnalyser(d);
		//	Node root=da.getRoot();
		Node root=(Node) map.get(rootElement);
		Frame rootFrame=new Frame(root);
		utils.log("Rootframe width="+rootFrame.width);
		utils.log("Rootframe depth="+rootFrame.depth);
//		rootFrame.calc();
		int offset=0;
		if (rootFrame.width<4) {offset=2;}
		layout(root,offset,1);  //avoid starting in top left corner
		utils.log("Completed at: "+new Date());
		return d;
	}
	
	/**
	 * @param root
	 * @param i
	 * @param j
	 */
	private void layout(Node n, float i, float j) {
//		utils.log("Laying out: "+((NamedElement)n.getElement()).getName()+" " +new Date());
//		if (level++>100) {return;}
		if (nodesDone.contains(n)) {return;}
		nodesDone.add(n);
		Frame nf=(Frame)frames.get(n);
//		utils.log("Frame="+nf);
		Bounds b = (Bounds)n.getLayoutConstraint();
		ILayoutNode lyn = utils.udh.getLayoutNode(n);
		int nwidth=new Double(lyn.getWidth()/2).intValue();
		b.setX(new Float( (i+ nf.width/2)*spaceX - nwidth ).intValue());
//		b.setX(new Float( (i+ nf.width/2)*spaceX  ).intValue());
		b.setY(new Float( j*spaceY ).intValue());
		EList edges=n.getTargetEdges();
		if (edges.isEmpty()) {return;}
		float myWidth=nf.width; float cumWidth=0;
		//		utils.log(((NamedElement)n.getElement()).getName()+" width: "+myWidth);
		for (Iterator iter = edges.iterator(); iter.hasNext();) {
			Node child = (Node)((Edge) iter.next()).getSource();
			if (nodesDone.contains(child)) {continue;}
			float cWidth=((Frame)frames.get(child)).width;
			//			utils.log(((NamedElement)child.getElement()).getName()+" width: "+cWidth);
			layout(child,i+cumWidth,j+1);
			cumWidth+=cWidth;
		}
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
	
	private class Frame {
		boolean done=false;
		Node node;
		boolean leaf=true;
		int width=1;
		int depth=1;
		List children;
		
		public Frame(Node n) {
			this.node=n;
			calc();
		}
		
		public Frame calc() {
//			utils.log("Calculating frame for: "+node.getElement()+level);
			if (done) {return this;}
			level=level+1;
//			if (level++>10) {return this;}
			done=true;
			frames.put(node,this);
			EList edges = node.getTargetEdges();
			if (edges.isEmpty()) {return this;}
			children=new ArrayList(edges.size());
			for (Iterator iter = edges.iterator(); iter.hasNext();) {
				Node n = (Node)((Edge) iter.next()).getSource();
				if (null!=frames.get(n)) {continue;}  // ignore loop links
				Frame nf=new Frame(n);
				children.add(nf);
				width+=nf.width;
				if (nf.depth>depth) {depth=nf.depth;}
			}
			if (children.size()>0) {width--; depth++;}
			return this;
		}
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
