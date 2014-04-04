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
 * Created on 03-Oct-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.p4eb.WPSTest;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenWPSComponentTest implements Executable {
	Utils utils=Utils.getSingleton();
	static final GenWPSExportTest genTest=new GenWPSExportTest();
	static final GenWPSMockObject genMock=new GenWPSMockObject();
	Document d;
	Document tgtDoc;
	Element curNode;
	Element sdodefs;
	Element app;
	Element ife;
	public void plugletmain(String[] args) {execute(null);}
	
	public Object execute(Object o) {
		IFile f=null; Object selob=null;
		if (o==null) {o = utils.getBaseSelection();
		if (o instanceof IStructuredSelection) {selob=((IStructuredSelection)o).getFirstElement();}
		if (selob!=null) {
			if (selob instanceof AbstractEditPart) {f=getBackingFile((AbstractEditPart)selob);}
			if (selob instanceof IFile) {f=(IFile)selob;}
		}
		} else if (o instanceof IFile) {f=(IFile)o;}
		//		if (!((f instanceof IFile) && ("wsdl".equalsIgnoreCase(f.getFileExtension())) ) )  {utils.log("Please select a wsdl file"); return;}
		if ((f==null) || !("component".equalsIgnoreCase(f.getFileExtension()) ) )  {utils.log("Please select a component"); return null;}
		utils.log(f);
		
		IResource[] mbrs;
		try {
			mbrs = f.getProject().members();
			for (int i = 0; i < mbrs.length; i++) {
				IResource mbr=mbrs[i];
				utils.log("Processing:"+mbr.getName());
				if ( (mbr.getType()==IResource.FILE) && ("import".equals(mbr.getFileExtension())) ) {
					genMock.execute((IFile)mbr);
				}
				if ( (mbr.getType()==IResource.FILE) && ("export".equals(mbr.getFileExtension())) ) {
					genTest.execute((IFile)mbr);
				}
			}
			
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(utils.getLogger());
		}
		return null;
	}
	
	private IFile getBackingFile(AbstractEditPart iep) {
		Resource res=((EObject)iep.getModel()).eResource();
		IFile f = utils.getFile(res.getURI());
		return f;
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
