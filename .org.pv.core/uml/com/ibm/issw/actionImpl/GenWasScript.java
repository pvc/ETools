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
 * Created on 27-Oct-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.actionImpl;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.dptk.patternWizard.PatternApplicationStatus;
import com.ibm.issw.transforms.Diagram2DOMTransformOld;
import com.ibm.pbe.rsa.interfaces.Executable;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenWasScript implements Executable {
	Utils utils=Utils.getSingleton();
	public Object execute(Object o) {execute(); return null;}
	
	public void execute() {
//		utils.log("****************************Beginning Run of"+this.getClass().getName());
		Document doc=null; IProject proj=null;
		EObject eo=utils.getSelectedUMLElement();
		if (eo!=null) {doc=getAppdef(eo);proj=utils.getProject(eo);} 
		else {
		IFile f=(IFile)utils.getSelectedResource();
		if (f==null) {f=utils.getEditedFile();}
		if (f!=null) {doc=utils.getDoc(f); proj=f.getProject();}
		}
		if (doc==null) {utils.log("Selection must be either an appdef file or diagram"); return;}
		Element root=(Element)doc.getFirstChild();
		String id=root.getAttribute("pattern");
		if (id.length()==0) {id = getProjectPatternId(proj);}
		if (id.length()==0) {utils.log("Pattern cannot be identified for this model - cannot continue"); return;}
		if (root.hasAttribute("traceProject")) {
			if (root.getAttribute("traceProject").length()==0) {root.setAttribute("traceProject",proj.getName());}
		}
		utils.log("Applying Pattern: "+id);
		PatternApplicationStatus status = utils.execDPTK(doc,id,proj,null);
		if (status.isSuccessful()) {utils.log("Successful application! "+status.getSummary());}
		else {utils.log("Problem applying pattern: "+status.getStatus());}
		
		
//		utils.log("****************************Run Complete");
	}


	/**
	 * @param eo
	 * @return
	 */
	private Document getAppdef(EObject eo) {
		Diagram d;
		if (eo instanceof View) {d=((View)eo).getDiagram();} else {return null;}
		GenAppdef t = new GenAppdef();
//		Document doc=t.execute(d);
		Document doc=null;	
		if (doc==null) {return null;}
		IFile fout=null;
		if (fout==null) {
			IPath xmlPath=utils.getPath(eo).removeLastSegments(1).append("WasScript.appdef");
			fout=utils.getFile(xmlPath);
		}
		
		utils.save(doc,fout);
		return doc;
	}


	/**
	 * @param p
	 * @return
	 */
	private String getProjectPatternId(IProject p) {
		
		String id="";
		IFile f=(IFile)p.findMember("/.pattern");
		if (f==null) {return id;}
		InputStream is=null;
		utils.log("Getting file contents");
		try {
			is = f.getContents();
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(utils.getLogger());
		}
		utils.log("Got file contents");
		if (is!=null) {
			LineNumberReader lr = new LineNumberReader(new InputStreamReader(is));
			boolean stop=false;
			String line=null;
			while (!stop) {
				try {
					line = lr.readLine();
					if (line.startsWith("SetID:")) {id=line.substring(6); break;}
					stop=!lr.ready();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace(utils.getLogger());
					stop=true;
				}
			}
		}
		return id;
	}	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
