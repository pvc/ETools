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
import java.util.Hashtable;
import java.util.LinkedList;

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
public class Xref extends Hashtable{
	public Object get(Object key) {
		Object o=super.get(key);
		if (o==null) {o=new LinkedList();super.put(key,o);}
		return o;
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
