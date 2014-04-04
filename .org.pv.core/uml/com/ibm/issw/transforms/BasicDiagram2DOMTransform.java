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
package com.ibm.issw.transforms;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.issw.transforms.UML2DOMElementTransform;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Administrator
 * 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BasicDiagram2DOMTransform implements Diagram2DOMTransform  {
	Utils utils=Utils.getSingleton();
	Element app=null;
	Document doc=null;
	NamedElement root=null;
	Stereotype patternApp;
	UML2DOMElementTransform t;
	Package rootPkg;
	private IPath documentPath;
	private IFile documentFile;

	/* (non-Javadoc)
	 * @see com.ibm.issw.rsa.interfaces.Executable#execute(java.lang.Object)
	 */
	public void run() {
		Document d=execute(utils.getDiagram());
		utils.dump(d);
	}
			
	public Document execute(Diagram d) {	
		root=utils.getParent(d);
		rootPkg=root.getNearestPackage();

		utils.log("Root:"+root);
		doc=execute2(d);
		if (doc==null) {return null;}
		
//		IFile fout=null;
//		if (fout==null) {
//			documentPath=utils.getPath(d).removeLastSegments(1).append(d.getName()+".appdef");
//			documentFile=utils.getFile(documentPath);
//		}
//		
//		utils.saveInputStream2File(utils.doc2InputStream(doc),documentFile);
		return doc;
	}
	public Document execute2(Diagram d) {
		Map dir = new Hashtable();

		String dName=d.getName();
	
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
		if (doc==null) {return null;}
		t=new UML2DOMElementTransform(doc);
//		root.getValue(patternApp,"xmlTagname");
//		utils.log(root);
		
		String projName = utils.getProjectName(d);
		List roots=utils.getRoots(d);
		if (roots.isEmpty()) {return doc;}
		
		Element app;
		NamedElement patternApp = rootPkg.getOwnedMember(rootPkg.getName());
		Stereotype appStereo=null;
		if (patternApp!=null) {appStereo=patternApp.getAppliedStereotype("PBEProfile::metadata");}
		if (appStereo!=null) {app=t.execute(patternApp);} else {app=doc.createElement("metadata");}
		process(roots,app);
		if ( (roots.size()==1) && (appStereo!=null) && (((Node)roots.get(0)).getElement()==patternApp) )  {app=(Element)app.getFirstChild();} //
		if (appStereo==null) {
			app.setAttribute("name", rootPkg.getName());
			app.setAttribute("targetModel", rootPkg.getModel().getName());
		}
		
		doc.appendChild(app);
		
		
//		EList views=d.getChildren();
//		List cells=new ArrayList();
//		for (Iterator iter = views.iterator(); iter.hasNext();) {
//			Element e=null;
//			View v = (View) iter.next();
//			EObject o=v.getElement();
//			if (o instanceof org.eclipse.uml2.uml.Classifier) {
//				Classifier c=(Classifier)o;
//				if (c.getAppliedStereotype("TypedEJB::model")==null) {continue;}
//				cells.add(v);
//			}
//		}
//		utils.log("Found models:"+cells.size());
//		for (Iterator iter = cells.iterator(); iter.hasNext();) {
//			View c = (View) iter.next();
//			process(c,app);
//		}	
//		
//
//		
//		EList edges = d.getEdges();
//		for (Iterator iter = edges.iterator(); iter.hasNext();) {
//			Element e=null;
//			Edge ed = (Edge) iter.next();
//			EObject o = ed.getElement();
//			if (o instanceof org.eclipse.uml2.uml.Association) {
//				Association a=(Association)o;
//				Stereotype st = a.getAppliedStereotype("Metamodel::link");
//				if (st!=null) {
//					e=t.executeE((org.eclipse.uml2.uml.Element)o);
//					if (e!=null) {app.appendChild(e);}
//					List l=getTopicLinks(a);
//					for (Iterator iterator = l.iterator(); iterator.hasNext();) {
//						String[] ends = (String[]) iterator.next();
//						Element tl = doc.createElement("topicLink");
//						tl.setAttribute("topic1",ends[0]);
//						tl.setAttribute("topic2",ends[1]);
//						e.appendChild(tl);
//					}
//				}
//			}
//		}
		return doc;
	}
	/**
	 * @param c
	 */
	private void process(List views,Element parent) {
		utils.log("Processing diagram roots");
		for (Iterator iter = views.iterator(); iter.hasNext(); ) {
				process((View) iter.next(),parent);
		}
	}
	private void process(View v,Element parent) {
		EObject o=v.getElement();
		if ( (o==null) || !(o instanceof Classifier)) {return;} 
		utils.log("Processing: "+((Classifier)o).getName()+", parent="+parent.getAttribute("name"));
		
		Element e=t.execute((Classifier)o);
		
		if (e!=null) {
			parent.appendChild(e);
			List chs = getChildren(v);
			for (Iterator iter = chs.iterator(); iter.hasNext();) {
				//				utils.log("Processing source edge of: "+o);
				View ch = (View) iter.next();
				process(ch,e);
			}
		}
	}
	
	public List getChildren(View v) {
		List ch=new ArrayList();
		for (Iterator iter = v.getSourceEdges().iterator(); iter.hasNext();) {
			Edge edge = (Edge) iter.next();
			EObject r = edge.getElement();
			if (r instanceof Dependency) {ch.add(edge.getTarget()); }
		}
		return ch;
	}
	
	private List getTopicLinks(org.eclipse.uml2.uml.Element e) {
		List list=new LinkedList();
		EList comments = e.getOwnedComments();
		if (comments.isEmpty()) {return list;}
		Comment comment = (Comment)comments.get(0);
		String body=comment.getBody();
		LineNumberReader is = new LineNumberReader(new StringReader(body));
		String line=null;
		for(;;) {
			try {line=is.readLine();} catch (IOException e1) {} 
			if (line==null) {break;}
			line=line.split("#")[0].trim();
			if (line.length()==0) {continue;}
			String[] ends=line.split(":");
			if (ends.length!=2 ) {utils.p("Error in TopicLink definition - ignoring: "+line);continue;}
			utils.p("TopicLink: "+ends[0]+" <--> "+ends[1]);
			list.add(ends);
		}
		return list;
	}

	public IPath getDocumentPath() {
		return documentPath;
	}
	public IFile getDocumentFile() {
		return documentFile;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
