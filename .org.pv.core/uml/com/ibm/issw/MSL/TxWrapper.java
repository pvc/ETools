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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.pv.core.Utils;


import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;
/*
 * Created on 12-Dec-2005
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
public class TxWrapper implements Executable {
	Utils utils=Utils.getSingleton();
	private OpWrapper op=new OpWrapper();
	Executable target;
	
	/**
	 * @param arg0
	 */
	public TxWrapper() {
//		this.op=new OpWrapper();
		// TODO Auto-generated constructor stub
	}
	public TxWrapper(Executable e) {
//		this.op=new OpWrapper(e);
		setTarget(e);
		// TODO Auto-generated constructor stub
	}
	
	public Object execute() {
		return execute(null);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Object execute(Object msgIn) {
		op.setMsgIn(msgIn);
		final String undoLabel = "Updating Model ...";
		TransactionalEditingDomain editDomain = UMLModeler.getEditingDomain();
		class DoItCommand extends RecordingCommand {
			public DoItCommand() {
				super(UMLModeler.getEditingDomain(),undoLabel);
			}
			protected void doExecute() {
				try{  op.execute(null);
				} catch (InterruptedException e) {
					utils.log("The operation was interrupted"); //$NON-NLS-1$
				} catch (InvocationTargetException e) {
					e.printStackTrace(utils.getLogger());
					Throwable t=e.getCause();
					utils.log(t);
					if (t instanceof MSLActionAbandonedException) {
						IStatus i=((MSLActionAbandonedException)e.getCause()).getStatus();
						utils.log(i);
					}
					throw new RuntimeException(e.getCause());
				}
			}
		}

		editDomain.getCommandStack().execute(new DoItCommand());
//		UMLModeler.getEditingDomain().run( op ,new NullProgressMonitor());
		return op.getMsgOut();
	}

	
	/**
	 * @return Returns the target.
	 */
	public Executable getTarget() {
		return target;
	}
	/**
	 * @param target The target to set.
	 */
	public void setTarget(Executable target) {
		this.target = target;
		op.setTarget(target);
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
