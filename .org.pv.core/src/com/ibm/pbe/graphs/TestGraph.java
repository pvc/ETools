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
public class TestGraph {

	public static void main(String[] args) {
		TestGraph t=new TestGraph();
		t.execute();
	
	}

	public void execute() {
		Graph g =new Graph();
		g.addNode(this);
		Reference r1=new Reference(this);
		g.addNode(r1);
		Connector c=new Connector(this,r1);
		g.addConnector(c);
		Reference r2=new Reference(this);
		g.addNode(r2);
		Connector c2=new Connector(r1,r2);
		g.addConnector(c2);
		Connector c3=new Connector(r2,r1);
		g.addConnector(c3);
//		g.dump();
		Graph g2=(Graph)g.clone();
		System.out.println("HasCycles is: "+g.hasCycles() );
//		g.treeDump();
		g.removeConnector(c3);
		System.out.println("HasCycles is: "+g.hasCycles() );
		g.addConnector(new Connector(r2,this));
		System.out.println("HasCycles is: "+g.hasCycles() );
//		g.treeDump();
//		g.removeNode(r1);
//		g.treeDump();
	}
	
	/**
	 * 
	 */
	private void testTiming() {
		int n=500000;
		long[] times=new long[n];
		for (int i=0;i<n;i++) {
			times[i]=System.currentTimeMillis();
		}
		for (int i=0;i<n/100;i++) {
			System.out.println(times[i*100]+": "+i*100);
		}
	}
	

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
