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
public class Connector extends Reference {
	static final public int A2B=0;
	static final public int B2A=1;
	static final public int BOTH=2;
	static final public int UNDEFINED=-1;
	
	private Object nodeA;
	private Object nodeB;
	private int direction=A2B;
	

	public Connector(Object o1, Object o2) {
		nodeA=o1;
		nodeB=o2;
	}
	public Connector(Object o1, Object o2, int direction) {
		this.direction=direction;
		nodeA=o1;
		nodeB=o2;
	}
	/**
	 * @return Returns the direction.
	 */
	public int getDirection() {
		return direction;
	}
	/**
	 * @param direction The direction to set.
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}
	/**
	 * @return Returns the nodeA.
	 */
	public Object getNodeA() {
		return nodeA;
	}
	/**
	 * @return Returns the nodeB.
	 */
	public Object getNodeB() {
		return nodeB;
	}
	public Object getSource() {
		return (direction==A2B)?nodeA:nodeB;
	}
	public Object getTarget() {
		return (direction==A2B)?nodeB:nodeA;
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
