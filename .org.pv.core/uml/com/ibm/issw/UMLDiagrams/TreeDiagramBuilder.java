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

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.pv.core.Utils;

import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;
import com.ibm.xtools.uml.ui.diagram.IUMLDiagramHelper;
import com.ibm.xtools.umlnotation.UMLDiagramKind;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeDiagramBuilder implements Executable{
	Utils utils=Utils.getSingleton();
	int x;int y;
	int level=0;
	int startX=7500;int startY=2000;int spaceX=3000;int spaceY=1750;
//	Node root;
	Hashtable map=new Hashtable();
	HashSet nodesDone=new HashSet();
	Hashtable frames=new Hashtable();
	EList mbrs;
	NamedElement treeRoot;
	String treeRootName;
	String diagramName;
	Namespace diagramOwner;
	Diagram d;
	IUMLDiagramHelper udh = UMLModeler.getUMLDiagramHelper();
	private UMLDiagramKind diagramType;
	private boolean diagramDocNode=false;
	
	public TreeDiagramBuilder(){
	}
	
//	public TreeDiagramBuilder(Namespace m,Element treeRoot, String diagramName, String diagramType){
	public TreeDiagramBuilder(Namespace m,NamedElement treeRoot) {
		diagramOwner=m;
		this.treeRoot=treeRoot;
		this.diagramName=m.getName();
		diagramType=UMLDiagramKind.FREEFORM_LITERAL;
	}
	//	public Node getRoot() {
	//	DiagramAnalyser da = new DiagramAnalyser(d);
	//	Node root=da.getRoot();
	//	if (root==null) {utils.log("Warning: This diagram is not a Tree!: " +d.getName()); return d;}
	//	return root;
	//	}
	
	public Diagram execute() {   // cant return root node with this algorithm (can do general graphs)
		if (diagramOwner==null) {return null;}
		int x=7500;int y=2000;
		utils.log("Creating Diagram for: "+diagramOwner);
		List diagrams=udh.getDiagrams(diagramOwner);
		for (Iterator iter = diagrams.iterator(); iter.hasNext();) {
			Diagram d2 = (Diagram) iter.next();
			if (d2.getName().equals(diagramName)) {utils.udh.destroyView(d2); break;}
		}
		//	Diagram d=null;
		if (d==null) {
			d=udh.createDiagram(diagramOwner,diagramType); 
			//	udh.openDiagramEditor(d);
			udh.setMainDiagram(diagramOwner,d);
			d.setName(diagramName);
			utils.log("Created new empty diagram: "+d);
		}
		d.getPersistedChildren().clear(); // NB This will destroy layout!
		d.getPersistedEdges().clear();
//		NamedElement treeRoot = diagramOwner.getMember(treeRootName);
		if (treeRoot==null) {utils.log("Could not find tree root element - cannot create tree in" +d.getName()); return d;}
		
		Node root=buildTree(treeRoot); //build the relationships
		
		Frame rootFrame=new Frame(root); // calculate layout requirements
		utils.nolog("Rootframe width="+rootFrame.width);
		utils.nolog("Rootframe depth="+rootFrame.depth);
//		rootFrame.calc();
		int offset=0;
		if (rootFrame.width<4) {offset=2;}
		layout(root,offset,1);  //avoid starting in top left corner
		if (!diagramDocNode) {utils.udh.destroyView(root);}
		utils.nolog("Completed at: "+new Date());
		return d;
	}
	
	/**
	 * @param treeRoot
	 */
	private Node buildTree(NamedElement e) {
		if (map.get(e)!=null) {return null;} // already processed
		Node v=udh.createNode(d,e);
		map.put(e,v);
		EList deps = e.getClientDependencies();
		for (Iterator iter = deps.iterator(); iter.hasNext();) {
			Dependency  dep = (Dependency ) iter.next();
			EList suppliers=dep.getSuppliers();
			for (Iterator i2 = suppliers.iterator(); i2.hasNext();) {
				NamedElement supplier = (NamedElement) i2.next();
				Node sNode=buildTree(supplier);
				if (sNode!=null) {udh.createEdge(v,sNode,dep);}
			}
		}
		return v;
	}

	/**
	 * @param e
	 * @return
	 */
	private EList getChildren(NamedElement e) {
		EList children=new BasicEList();
		EList deps = e.getClientDependencies();
		for (Iterator iter = deps.iterator(); iter.hasNext();) {
			Dependency  dep = (Dependency ) iter.next();
			EList suppliers=dep.getSuppliers();
			children.addAll(suppliers);
		}
		return children;
	}

	/**
	 * @param root
	 * @param i
	 * @param j
	 */
	private void layout(Node n, float i, float j) {
		utils.nolog("Laying out: "+((NamedElement)n.getElement()).getName()+" " +new Date());
//		if (level++>100) {return;}
		if (nodesDone.contains(n)) {return;}
		nodesDone.add(n);
		Frame nf=(Frame)frames.get(n);
//		utils.log("Frame="+nf);
		Bounds b = (Bounds)n.getLayoutConstraint();
		ILayoutNode lyn = udh.getLayoutNode(n);
		int nwidth=new Double(lyn.getWidth()/2).intValue();
		b.setX(1000+new Float( (i+ nf.width/2)*spaceX - nwidth ).intValue());
//		b.setX(new Float( (i+ nf.width/2)*spaceX  ).intValue());
		b.setY(new Float( j*spaceY ).intValue());
		EList edges=n.getSourceEdges();
		if (edges.isEmpty()) {return;}
		float myWidth=nf.width; float cumWidth=0;
				utils.nolog(((NamedElement)n.getElement()).getName()+" width: "+myWidth);
		for (Iterator iter = edges.iterator(); iter.hasNext();) {
			Node child = (Node)((Edge) iter.next()).getTarget();
			if (nodesDone.contains(child)) {continue;}
			float cWidth=((Frame)frames.get(child)).width;
						utils.nolog(((NamedElement)child.getElement()).getName()+" width: "+cWidth);
			layout(child,i+cumWidth,j+1);
			cumWidth+=cWidth;
		}
	}
	

	
	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return execute();
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
			utils.nolog("Calculating frame for: "+node.getElement()+level);
			if (done) {return this;}
			level=level+1;
//			if (level++>10) {return this;}
			done=true;
			frames.put(node,this);
			EList edges = node.getSourceEdges();
			if (edges.isEmpty()) {utils.nolog("No edges found - returning 1,1");return this;}
			children=new ArrayList(edges.size());
			for (Iterator iter = edges.iterator(); iter.hasNext();) {
				Node n = (Node)((Edge) iter.next()).getTarget();
				if (null!=frames.get(n)) {continue;}  // ignore loop links
				Frame nf=new Frame(n);
				children.add(nf);
				width+=nf.width;
				if (nf.depth>depth) {depth=nf.depth;}
			}
			if (children.size()>0) {width--; depth++;}
			utils.nolog("Width:"+width+", Depth="+depth);
			return this;
		}
	}
	
	/**
	 * @return Returns the treeRootName.
	 */
	public String getTreeRootName() {
		return treeRootName;
	}
	/**
	 * @param treeRootName The treeRootName to set.
	 */
	public void setTreeRootName(String treeRootName) {
		this.treeRootName = treeRootName;
	}

	/**
	 * @param b
	 */
	public void setdiagramDocNode(boolean b) {
		diagramDocNode=b;
		
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
