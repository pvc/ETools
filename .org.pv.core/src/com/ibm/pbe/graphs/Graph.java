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
package com.ibm.pbe.graphs;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.pv.core.Utils;


/*
 * Created on 08-Nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Graph  {
	Utils utils=Utils.getSingleton();
	PrintStream out=System.out;
	// won't yet handle connector direction switch
	HashSet nodes=new HashSet();
	HashSet roots=new HashSet();
	HashSet leaves=new HashSet();
	HashSet joins=new HashSet();
	HashSet forks=new HashSet();

	HashSet connectors=new HashSet();
	Xref nodeChildren=new Xref();
	Xref nodeParents=new Xref();
	
	Class type=Object.class; // use for typing node values
	private String leadin;private String prevLeadin; private HashSet processedNodes;
	
	public boolean isTree() {
		if (roots.size()==1) {return true;} else return false;
	}
	
	
	public void addNode(Object n) {
		if (nodes.add(n)) {roots.add(n);leaves.add(n);}
	}

	
	public void addConnector(Connector c) {
		connectors.add(c);
		Collection refs;Object node;
		
		node=c.getSource();
		refs=(Collection)nodeChildren.get(node);
		refs.add(c);
		leaves.remove(node); 
		if (refs.size()>1) {forks.add(node);}
		
		node=c.getTarget();
		refs=(Collection)nodeParents.get(node);
		refs.add(c);
		roots.remove(node); if (refs.size()>1) {joins.add(node);}
	}

	/**
	 * @return Returns the connectors.
	 */
	public HashSet getConnectors() {
		return connectors;
	}
	/**
	 * @param connectors The connectors to set.
	 */
	public void setConnectors(HashSet connectors) {
		this.connectors = connectors;
	}
	/**
	 * @return Returns the forks.
	 */
	public HashSet getForks() {
		return forks;
	}
	/**
	 * @param forks The forks to set.
	 */
	public void setForks(HashSet forks) {
		this.forks = forks;
	}
	/**
	 * @return Returns the joins.
	 */
	public HashSet getJoins() {
		return joins;
	}
	/**
	 * @param joins The joins to set.
	 */
	public void setJoins(HashSet joins) {
		this.joins = joins;
	}
	/**
	 * @return Returns the leaves.
	 */
	public HashSet getLeaves() {
		return leaves;
	}
	/**
	 * @param leaves The leaves to set.
	 */
	public void setLeaves(HashSet leaves) {
		this.leaves = leaves;
	}
	/**
	 * @return Returns the out.
	 */
	public PrintStream getOut() {
		return out;
	}
	/**
	 * @param out The out to set.
	 */
	public void setOut(PrintStream out) {
		this.out = out;
	}
	/**
	 * @param nodeChildren The nodeChildren to set.
	 */
	public void setNodeChildren(Xref nodeChildren) {
		this.nodeChildren = nodeChildren;
	}
	/**
	 * @param nodeParents The nodeParents to set.
	 */
	public void setNodeParents(Xref nodeParents) {
		this.nodeParents = nodeParents;
	}
	/**
	 * @param roots The roots to set.
	 */
	public void setRoots(HashSet roots) {
		this.roots = roots;
	}
	/**
	 * @return Returns the nodes.
	 */
	public Set getNodes() {
		return nodes;
	}
	/**
	 * @param nodes The nodes to set.
	 */
	public void setNodes(HashSet nodes) {
		this.nodes = nodes;
	}
	/**
	 * @return Returns the type.
	 */
	public Class getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(Class type) {
		this.type = type;
	}
	/**
	 * @return Returns the nodeChildren.
	 */
	public Xref getNodeChildren() {
		return nodeChildren;
	}
	/**
	 * @return Returns the nodeParents.
	 */
	public Xref getNodeParents() {
		return nodeParents;
	}
	/**
	 * @return Returns the roots.
	 */
	public Set getRoots() {
		return roots;
	}
	
	public void dump() {
		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
			Object o= iter.next();
			utils.log("Node: "+o);
			if (leaves.contains(o)) {utils.log("Leaf ");}
			if (roots.contains(o)) {utils.log("Root ");}
			
		}
		for (Iterator iter = connectors.iterator(); iter.hasNext();) {
			Connector c = (Connector) iter.next();
			utils.log("Connector: "+c.getValue());
			utils.log(c.getSource()+" TO "+c.getTarget());
		}
	}
	
	public Object clone() {
		Graph g=new Graph();
		g.setNodes((HashSet)nodes.clone());
		g.setRoots((HashSet)roots.clone());
		g.setLeaves((HashSet)leaves.clone());
		g.setForks((HashSet)forks.clone());
		g.setJoins((HashSet)joins.clone());
		g.setConnectors((HashSet)connectors.clone());
		g.setNodeChildren((Xref)nodeChildren.clone());
		g.setNodeParents((Xref)nodeParents.clone());
		return g;
	}
	
	public void treeDump() {
		if (!isTree()) {utils.log("Not a tree!"); return;}
		Object root=roots.iterator().next();
		int level=0;
		leadin=""; processedNodes=new HashSet();
		treeDumpLevel(level,root);
		
	}
	public void treeDumpLevel(int level,Object o) {
		if (!processedNodes.add(o)) {utils.log("***Cycle Detected at: "+o);return;}
		utils.log(leadin+o);
		
		prevLeadin=leadin;
		leadin=leadin+"==>";
		for (Iterator iter = ((Collection)nodeChildren.get(o)).iterator(); iter.hasNext();) {
			Connector c = (Connector)iter.next();
			treeDumpLevel(++level,c.getTarget());
		}
		leadin=prevLeadin;
	}
	public boolean hasCycles() {
		Graph g=(Graph)this.clone();
		return cycleTest(g);
	
	}
	
	/**
	 * @param g
	 * @return
	 */
	private boolean cycleTest(Graph g) {
		Object[] refs;
		if (g.getNodes().size()==0) {return false;}
		if ( (g.getRoots().size()==0) || (g.getLeaves().size()==0) ) {return true;}
		refs=((Collection)g.getRoots()).toArray();
		for (int i=0; i<refs.length; i++) {
			g.removeNode(refs[i]);
		}
		refs=((Collection)g.getLeaves()).toArray();
		for (int i=0; i<refs.length; i++) {
			g.removeNode(refs[i]);
		}
		return cycleTest(g);
	}


	public void removeNode(Object n) {
		Object[] refs;
		refs=((Collection)nodeChildren.get(n)).toArray();
		for (int i=0; i<refs.length; i++) {
			Connector c = (Connector)refs[i];
			removeConnector(c);
		}
		refs=((Collection)nodeParents.get(n)).toArray();
		for (int i=0; i<refs.length; i++) {
			Connector c = (Connector)refs[i];
			removeConnector(c);
		}
		
		nodes.remove(n);
		roots.remove(n);
		leaves.remove(n);
		joins.remove(n);
		forks.remove(n);
		nodeChildren.remove(n);
		nodeParents.remove(n);
	}


	/**
	 * @param c
	 */
	public void removeConnector(Connector c) {
		Object o; Collection orefs;
		o=c.getSource();
		orefs=(Collection)nodeChildren.get(o);
		orefs.remove(c);
		if (orefs.size()==0) {leaves.add(o);}
		if (orefs.size()==1) {forks.remove(o);}
		
		o=c.getTarget();
		orefs=(Collection)nodeParents.get(o);
		orefs.remove(c);
		if (orefs.size()==0) {roots.add(o);}
		if (orefs.size()==1) {joins.remove(o);}
		
		connectors.remove(c);
		
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
