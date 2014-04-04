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
 * Created on 26-Apr-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.actionImpl;

import java.util.Collection;

import org.pv.core.Utils;


/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Loop {
	static final int CONTINUE=0;
	static final int BREAK=1;
	static final Utils utils=Utils.getSingleton();
	Object result;
	Collection c;
	Object[] objects;
	
	Loop(Collection c) {
		this.c=c;
	}
	Loop(Object[] o) {
		objects=o;
	}
	Loop() {}
	

	/**
	 * @param objects
	 * @param loop
	 */

	
	public Object run(Collection c) {
		if (c==null) {return null;}
		return run(c.toArray());
	}
	public Object run(Object[] objects) {
		if (objects==null) {return null;}
		for (int i = 0; i < objects.length; i++) {
			int rc=process(objects[i]);
			if (rc==BREAK) {break;}
		}
		return result;
	}	
	/**
	 * @return
	 */
	private Object getResult() {
		// TODO Auto-generated method stub
		return result;
	}

	/**
	 * @param object
	 * @return
	 */
	public int process(Object object) {
		utils.log("Null run method executing");
		return CONTINUE;
	}
	/**
	 * @param result The result to set.
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}