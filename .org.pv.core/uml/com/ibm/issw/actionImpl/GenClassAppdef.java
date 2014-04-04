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
 * Created on 11-Jan-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

// this is  now a special tform just for this pattern - uses portlet tag
// use genappdef2 for generic
// sets package from package containing diagram
// sets page title from diagram title

package com.ibm.issw.actionImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.dptk.patternWizard.PatternApplicationStatus;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.Node;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenClassAppdef {
	Utils utils=Utils.getSingleton();

	/* (non-Javadoc)
	 * @see com.ibm.issw.rsa.interfaces.Executable#execute(java.lang.Object)
	 */
	public void execute() {
		EObject eo=utils.getSelectedUMLElement();
		Diagram d;
		if (eo instanceof View) {d=((View)eo).getDiagram();} else {utils.log("Selection must be a diagram - please retry");return;}
		String dName=d.getName();
		String pkg=((NamedElement)d.eContainer().eContainer()).getName();
		
		IFile f=utils.getFile(utils.getProjectName(eo)+"/.pattern");
		InputStream is=null;
//		utils.log("Getting file contents");
		try {
			is = f.getContents();
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(utils.getLogger());
		}
//		utils.log("Got file contents");
		if (is==null) {return;}
		LineNumberReader lr = new LineNumberReader(new InputStreamReader(is));
		boolean stop=false;
		String line=null;
		String id=null;
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
		if (id==null) {utils.log("Cannot find a pattern id in this Project - cannot continue"); return;}
		
		
		Document doc=null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
		if (doc==null) {return;}
		Element app=doc.createElement("app");
		doc.appendChild(app);
		Element portlet=doc.createElement("portlet");
		app.appendChild(portlet);
		portlet.setAttribute("basePackage",pkg);
		portlet.setAttribute("display",dName);
		
		EList views=d.getChildren();
		for (Iterator iter = views.iterator(); iter.hasNext();) {
			Node v = (Node) iter.next();
			EObject o=v.getElement();
			if (o instanceof org.eclipse.uml2.uml.Class) {
				org.eclipse.uml2.uml.Class c=(org.eclipse.uml2.uml.Class)o;
				Element e=doc.createElement("entity");
				portlet.appendChild(e);
				e.setAttribute("name",c.getName());
				EList atts=utils.getAttributes(v);
				for (Iterator iterator = atts.iterator(); iterator.hasNext();) {
					Property att = (Property) iterator.next();
					if (att.getAssociation()!=null) {continue;}
					Element eAtt=doc.createElement("attribute");
					e.appendChild(eAtt);
					eAtt.setAttribute("name",att.getName());
					eAtt.setAttribute("type","String");
					eAtt.setAttribute("length","250");
					eAtt.setAttribute("variable","true");
					EList kw=att.getKeywords();
//					utils.log("KWords for "+att.getName()+kw);
					if (!kw.isEmpty()) {e.setAttribute("key",att.getName());} // BAD - must dd an iterator to check values of kws!!
				}
			}
		}
		IFile fout=null;
		if (fout==null) {
			IPath xmlPath=utils.getPath(d).removeLastSegments(1).append(d.getName()+".appdef");
			fout=utils.getFile(xmlPath);
		}
		
		utils.save(doc,fout);
		PatternApplicationStatus status = utils.execDPTK(doc,id,utils.getProject(eo),null);
		utils.log("DPTK invocation results:");
		utils.dump(status);
		
//		utils.edit(fout);
		return;
		

	}
	
	

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
