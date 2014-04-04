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
package com.ibm.pbe.datatypes;

import java.util.List;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NamedNode {
	String name;
	Object value;
	boolean leaf=true;
	public NamedNode(String name, Object value) {
		setName(name);
		setValue(value);
	}
	/**
	 * @param name2
	 * @param values
	 * @param b
	 */
	public NamedNode(String name, Object values, boolean b) {
		setLeaf(b);
		setName(name);
		setValue(values);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return Returns the leaf.
	 */
	public boolean isLeaf() {
		return leaf;
	}
	/**
	 * @param leaf The leaf to set.
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
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
