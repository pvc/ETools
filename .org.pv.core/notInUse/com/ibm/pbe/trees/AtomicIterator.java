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
public class AtomicIterator implements Iterator<Object> {
	Utils utils=Utils.getSingleton();
	private boolean pruned=false;
	private Object tn;
	private Iterator childrenIterator;
	private AtomicIterator currentChildIterator;
	boolean childrenDone=false;
	boolean allDone=false;
	
	public AtomicIterator(Object tn) {
		this.tn=tn;
		if (tn==null) {allDone=true;}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.util.TreeIterator#prune()
	 */
	public void prune() {
		allDone=true;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
//		utils.p("In hasnext");
		return !allDone;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
	if (allDone) {return null;}
	else {allDone=true; return tn;}
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
