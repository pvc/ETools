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
package org.pv.core;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.pv.plugin.Copyright;


public abstract class P4ebTransform extends Job {
	P4ebModel m;
	protected static final Utils utils=Utils.getSingleton();
	protected final boolean OVERWRITE=utils.OVERWRITE;
	protected final boolean NOOVERWRITE=utils.NOOVERWRITE;
	
	
	/**
	 * @param name
	 */
	public P4ebTransform() {
		super("P4ebTransform");
	}
	public P4ebTransform(P4ebModel m, String name) {
		super(name);
		this.m=m;
	}
	public P4ebTransform(P4ebModel m) {
		super("Running Transform for: "+m.getName());
		this.m=m;
	}
	
	
	protected void p(Object o) {
		utils.p(o); // simple delegation to p4eb toolkit
	}

    protected IStatus run(IProgressMonitor monitor) {
        runTransform(m);
        return Status.OK_STATUS; // needs more error handling!
     }
	/**
	 * @param m2
	 */
	protected abstract void runTransform(P4ebModel m);
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
