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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Package;

import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.pv.core.Utils;

import com.ibm.xtools.uml.ui.diagram.IUMLDiagramHelper;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DiagramAnalyser implements Executable{
	Utils utils=Utils.getSingleton();
	Diagram d;
	IUMLDiagramHelper dh = UMLModeler.getUMLDiagramHelper();
	List roots=new LinkedList();
	List leaves=new LinkedList();
	boolean isTree=false;
	boolean isSink=false;
	
	public DiagramAnalyser(View v) {
		d=v.getDiagram();
		recompute();
	}
	
	public void recompute() {
	EList nodes=d.getChildren();
	for (Iterator iter = nodes.iterator(); iter.hasNext();) {
		Node node = (Node) iter.next();
		if (node.getSourceEdges().isEmpty()) {roots.add(node);}
		if (node.getTargetEdges().isEmpty()) {leaves.add(node);}
	}
	if (roots.size()==1) {isTree=true;}
	if (leaves.size()==1) {isSink=true;}
	}
	

	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return (Diagram)execute((Package) o);
	}
	/**
	 * @return Returns the leaves.
	 */
	public List getLeaves() {
		return leaves;
	}
	/**
	 * @return Returns the roots.
	 */
	public List getRoots() {
		return roots;
	}
	
	public Node getLeaf() {
		return (Node)leaves.get(0);
	}
	/**
	 * @return Returns the roots.
	 */
	public Node getRoot() {
		if (roots.isEmpty()) {return null;}
		return (Node)roots.get(0);
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
