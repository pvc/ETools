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
import java.util.LinkedList;
import java.util.List;

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
public class GN {
	private Object value;
	private GN child;
	private List children;
	private List parents;
	private GN parent;
	private boolean multiParent=false;
	private boolean noParent=true;
	private boolean multiChild=false;
	private boolean noChild=true;
	
	public GN(Object o) {
		this.value=o;
	}
	
	public void addChild(Object o) {
		GN node=new GN(o);
		addChild(node);
	}
	
	public void addChild(GN node) {
		node.addParent(this);
		if (noChild) {child=node; noChild=false;}
		else {
			if (!multiChild) {children=new LinkedList();children.add(child); multiChild=true;}
			children.add(node);
		}
	}

	/**
	 * @param gn
	 */
	private void addParent(GN node) {
		if (noParent) {parent=node; noParent=false;}
		else {
			if (!multiParent) {parents=new LinkedList();parents.add(parent); multiParent=true;}
			parents.add(node);
		}
		
		
	}

	/**
	 * @return Returns the child.
	 */
	public GN getChild() {
		return child;
	}
	/**
	 * @param child The child to set.
	 */
	public void setChild(GN child) {
		this.child = child;
	}
	/**
	 * @return Returns the children.
	 */
	public List getChildren() {
		return children;
	}
	/**
	 * @param children The children to set.
	 */
	public void setChildren(List children) {
		this.children = children;
	}
	/**
	 * @return Returns the multiChild.
	 */
	public boolean isMultiChild() {
		return multiChild;
	}
	/**
	 * @param multiChild The multiChild to set.
	 */
	public void setMultiChild(boolean multiChild) {
		this.multiChild = multiChild;
	}
	/**
	 * @return Returns the multiParent.
	 */
	public boolean isMultiParent() {
		return multiParent;
	}
	/**
	 * @param multiParent The multiParent to set.
	 */
	public void setMultiParent(boolean multiParent) {
		this.multiParent = multiParent;
	}
	/**
	 * @return Returns the noChild.
	 */
	public boolean isNoChild() {
		return noChild;
	}
	/**
	 * @param noChild The noChild to set.
	 */
	public void setNoChild(boolean noChild) {
		this.noChild = noChild;
	}
	/**
	 * @return Returns the noParent.
	 */
	public boolean isNoParent() {
		return noParent;
	}
	/**
	 * @param noParent The noParent to set.
	 */
	public void setNoParent(boolean noParent) {
		this.noParent = noParent;
	}
	/**
	 * @return Returns the parent.
	 */
	public GN getParent() {
		return parent;
	}
	/**
	 * @param parent The parent to set.
	 */
	public void setParent(GN parent) {
		this.parent = parent;
	}
	/**
	 * @return Returns the parents.
	 */
	public List getParents() {
		return parents;
	}
	/**
	 * @param parents The parents to set.
	 */
	public void setParents(List parents) {
		this.parents = parents;
	}
	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
