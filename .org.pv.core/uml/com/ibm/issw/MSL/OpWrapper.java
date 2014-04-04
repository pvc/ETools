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
package com.ibm.issw.MSL;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.pv.core.Utils;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OpWrapper extends org.eclipse.gmf.runtime.emf.core.ResourceSetModifyOperation {
	Utils utils=Utils.getSingleton();
	Executable target;
	Object msgIn;
	Object msgOut;
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void execute(IProgressMonitor arg0)
			throws InvocationTargetException, InterruptedException {
//		utils.log("Executing Modify Operation wrapper");
		msgOut=target.execute(msgIn);
//		utils.log("Modify Operation complete");
		
	}
	
	/**
	 * @return Returns the executable.
	 */
	public Executable getTarget() {
		return target;
	}
	/**
	 * @param executable The executable to set.
	 */
	public void setTarget(Executable executable) {
		this.target = executable;
	}
	/**
	 * @return Returns the msgIn.
	 */
	public Object getMsgIn() {
		return msgIn;
	}
	/**
	 * @param msgIn The msgIn to set.
	 */
	public void setMsgIn(Object msgIn) {
		this.msgIn = msgIn;
	}
	/**
	 * @return Returns the msgOut.
	 */
	public Object getMsgOut() {
		return msgOut;
	}
	/**
	 * @param msgOut The msgOut to set.
	 */
	public void setMsgOut(Object msgOut) {
		this.msgOut = msgOut;
	}
	/**
	 * @param arg0
	 */
	public OpWrapper(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	
	
	/**
	 * @param e
	 */
	public OpWrapper(Executable e) {
		super(e.toString());
		this.target=e;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public OpWrapper() {
		super("Operation Wrapper");
		// TODO Auto-generated constructor stub
	}




	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
