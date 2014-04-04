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
 * Created on 19-May-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.pv.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.pv.core.Utils;
import org.pv.plugin.Copyright;


/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PBEResult extends MultiStatus {
	private final static Utils utils=Utils.getSingleton();
	private Object result;
	boolean multi=false;
	int statusCount=0;
	static public PBEResult OK_RESULT=new PBEResult(null);
//	static public PBEResult CANCEL_RESULT=new PBEResult(null,CANCEL,null,null,null);
	
	public PBEResult(String sourceId, int status, String message, Throwable exception, Object result) {
		super(sourceId, status, message, exception);
		this.result=result;
	}
	public PBEResult(String sourceId, int status, IStatus[] children,String message, Throwable exception, Object result) {
		super(sourceId, status, children, message, exception);
		this.result=result;
	}
	public PBEResult(Object result){
		super("com.ibm.pbe",OK,"",new Throwable());
		this.result=result;
	}
	public PBEResult() {
		super("com.ibm.pbe",OK,null,null); 
		result=null;
	}
	
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public void setMessage(String msg) {if (msg==null) {msg="";}; super.setMessage(msg);}
	public void addError(String msg) {
		add(new Status(PBEResult.ERROR,"com.ibm.pbe",0,msg,null));
		setMessage(msg);
		statusCount++;
		
		// TODO Auto-generated method stub
		
	}
	public boolean isMultistatus() {return statusCount>1;}
	public void run() {
		PBEResult r = new PBEResult(null);
		r.addError("fed");
		utils.log("Msg="+r.getMessage());
	}
	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
