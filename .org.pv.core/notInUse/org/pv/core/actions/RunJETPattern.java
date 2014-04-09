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
package org.pv.core.actions;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//import com.ibm.dptk.patternWizard.PatternApplicationStatus;
//import com.ibm.issw.transforms.BasicDiagram2DOMTransform;
//import com.ibm.issw.transforms.Diagram2DOMTransform;
import com.ibm.pbe.patterns.PatternApplicator;
import com.ibm.pbe.rsa.interfaces.Executable;

//import org.eclipse.gmf.runtime.notation.Diagram;
//import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RunJETPattern implements Executable {
	final static Utils utils=Utils.getSingleton();
	final static PatternApplicator patrunner=new PatternApplicator();
	public Object execute(Object o) {execute(); return null;}
	
	public void run() {execute();}
	public void execute() {
//		utils.log("****************************Beginning Run of"+this.getClass().getName());
		Document doc=null; IProject proj=null; IFile f=null;
		utils.saveEditors();
		EObject eo=utils.getSelectedUMLElement();
		if (eo!=null) {doc=getAppdef(eo);proj=utils.getProject(eo);} 
		else {
			IResource r=utils.getSelectedResource();
			if (r==null||(r.getType()!=IResource.FILE)) {} else {f=(IFile)r;}
			if (f==null) {f=utils.getEditedFile();}
		if (f!=null) {doc=utils.getDoc(f); proj=f.getProject();}
		}
		if (doc==null) {utils.log("Selection must be either an appdef file or diagram"); return;}
//		utils.dump(doc);
		Element root=(Element)doc.getDocumentElement();
		String id=root.getAttribute("pattern");
		String modelName=root.getAttribute("name");
//		if ( (modelName.length()>0) && (f!=null)) {
//			String fn=modelName+".appdef";
//			if (!f.getName().equals(fn)) {
//				IFile newFile = utils.getFile(f.getFullPath().removeLastSegments(1).append(fn));
//				if (newFile.exists()) {utils.changeExtension(newFile,"appdef"+utils.getUid());}
//					utils.log("Saving serialised model to: "+f.getProjectRelativePath().removeLastSegments(1).append(fn));
//				try {
//					f.move(newFile.getFullPath(),true,null);
//				} catch (CoreException e) {
//					e.printStackTrace(utils.getLogger());
//				}
//			}
//		}
		
		if (id.length()==0) {id = getProjectPatternId(proj);root.setAttribute("pattern",id);}
		if (id.length()==0) {utils.log("Pattern cannot be identified for this model - cannot continue"); return;}
		utils.log("Applying Pattern: "+id);
		if (id.endsWith(".jet")) {
			patrunner.generate(root);
//			IStatus status = utils.runJet(root, id);
//			utils.dump(status);
		} else {utils.log("Invalid pattern id - must end with '.jet' - found: "+id);}
		
//		utils.log("****************************Run Complete");
	}


	/**
	 * @param eo
	 * @return
	 */
	private Document getAppdef(EObject eo) {
//@UML
//		Diagram d;
//		if (eo instanceof View) {d=((View)eo).getDiagram();} else {return null;}
//		Diagram2DOMTransform t = new BasicDiagram2DOMTransform();
//		return t.execute(d);
//@UML
		return null;
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
}
