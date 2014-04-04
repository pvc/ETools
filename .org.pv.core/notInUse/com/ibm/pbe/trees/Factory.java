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
 * Created on 18-Aug-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.pbe.trees;

import java.io.File;
import java.util.Iterator;

import org.pv.core.Utils;



/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Factory  {
	Utils utils=Utils.getSingleton();
	private Factory helperFactory;
	private boolean pruned=false;
	private Object tn;
	private Iterator childrenIterator;
	boolean childrenDone=false;
	boolean allDone=false;
	
	public Object getConnector(Object target, Class type) {
//		if (type instanceof RegisteredConnector) {}
		return null;
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
