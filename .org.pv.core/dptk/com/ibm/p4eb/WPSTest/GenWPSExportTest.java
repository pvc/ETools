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

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.pv.core.Utils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenWPSExportTest implements Executable {
	Utils utils=Utils.getSingleton();
	Document d;
	Document tgtDoc;
	Element curNode;
	Element sdodefs;
	Element app;
	Element ife;
	
	public void plugletmain(String[] args) {execute(null);}
	public void execute() {
		execute(null);
	}
	
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
		if ((f==null) || !("export".equalsIgnoreCase(f.getFileExtension()) ) )  {utils.log("Please select an export"); return null;}
		
		Document export = utils.getDoc((IFile)f);
		String pName=f.getProject().getName();
		String fName=f.getName().split("\\.")[0];
		tgtDoc=utils.getNewDocument("app");
		app=(Element)tgtDoc.getFirstChild();
		sdodefs=tgtDoc.createElement("sdoDefs");
		
		app.setAttribute("module",pName);
		app.setAttribute("name",fName);
		app.setAttribute("export",fName);
		app.setAttribute("import",fName);
		Element root=(Element)export.getFirstChild();
		
		NodeList ifs = export.getElementsByTagName("interface");
		for (int i = 0; i < ifs.getLength(); i++) {
			
			Element iface=(Element)ifs.item(i);
			String[] port=iface.getAttribute("portType").split(":");
			NamedNodeMap atts = root.getAttributes();
			//		for (int i = 0; i < atts.getLength(); i++) {
			//			Attr a=(Attr)atts.item(i);
			//			utils.log(a);
			//		}
			Attr a=(Attr)atts.getNamedItem("xmlns:"+port[0]);
			if (a==null) {utils.log("No namespace found for prefix: "+port[0]); return null;}
			String lib;
			try {
				lib=(new URI(a.getValue())).getAuthority();
			} catch (URISyntaxException e) {utils.log("Problem with URI in export file: "+a.getValue());
				e.printStackTrace(utils.getLogger()); return null;
			}
			
			try {
				IProject proj=utils.getProject(lib);
				IFile wsdlFile=(IFile)proj.findMember(port[1].concat(".wsdl"));
				if (wsdlFile==null) {utils.log("Could not find wsdl: "+port[1]+".wsdl - interface omitted"); continue;}
				d=utils.getResolvedDoc(wsdlFile);
			} catch (Exception e) {utils.log("Could not load wsdl: "+port[1]+".wsdl - interface omitted");
			e.printStackTrace(utils.getLogger()); continue;
			}
			
			ife=tgtDoc.createElement("interface");
			ife.setAttribute("name",port[1]);
			ife.setAttribute("seq",""+i);
			ife.setAttribute("lib",lib);
			app.appendChild(ife);
			//		IProject p= utils.wsr.getProject(lib);
			//		IFile f2=utils.findFile(p,port[1]+".java");
			//		if (f2!=null) {String ipkg=f2.getProjectRelativePath().removeLastSegments(1).toString().replace('/','.');
			//						app.setAttribute("ipkg",ipkg);}
			//		if (d==null) {utils.log("Could not find wsdl: "+port[1]);return;}
			processWSDL(ife);
			app.appendChild(sdodefs);
		}
		IPath appdefPath=f.getProjectRelativePath().removeFileExtension().addFileExtension("appdef");
		IFile appdef=f.getProject().getFile(appdefPath);
		utils.save(tgtDoc,appdef);
//		utils.edit(appdef);
		utils.execDPTK(tgtDoc,"com.ibm.pv.patterns.SCAModuleTest");
		return null;
	}
	/**
	 * @param o
	 * @return
	 */
	private IFile getBackingFile(AbstractEditPart iep) {
		utils.log(iep);
		Resource res=((EObject)iep.getModel()).eResource();
		IFile f = utils.getFile(res.getURI());
		return f;
	}
	/**
	 * @param firstChild
	 */
	private void processWSDL(Element ife) {
		NodeList ops = d.getElementsByTagName("wsdl:operation");
		Node app=tgtDoc.getFirstChild();
		for (int i = 0; i < ops.getLength(); i++) {
			Element opNode = (Element)ops.item(i);
			utils.log(opNode.getAttribute("name"));
			curNode=tgtDoc.createElement("method");
			curNode.setAttribute("name",opNode.getAttribute("name"));
			ife.appendChild(curNode);
			NodeList nodes = opNode.getChildNodes();
			for (int j = 0; j < nodes.getLength(); j++) {
				Node n = nodes.item(j);
				switch(n.getNodeType()) {
				case Node.ELEMENT_NODE:
					//					utils.p("Element:"+n.getNodeType()); 
					processElement((Element)n); break;
				case Node.TEXT_NODE:
					break;
				default:
					utils.log("Found unexpected Node: "+n);
				}
			}
		}
	}
	/**
	 * @param element
	 */
	private void processElement(Element e) {
		if (e.getLocalName().equals("fault")) {
			utils.log("Fault: "+e.getAttribute("name")); 
			Element test=tgtDoc.createElement("test");
			curNode.appendChild(test);
			test.setAttribute("faultName",e.getAttribute("name"));
			test.setAttribute("description","Test of Fault: "+curNode.getAttribute("name"));
			test.setAttribute("id","");
			return;}
		
		if (e.getLocalName().equals("input")) {utils.log("Input: "+e.getAttribute("message")); 
		String[] msgType=e.getAttribute("message").split(":");
		NodeList msgs = d.getElementsByTagName("wsdl:message");
		Element theMsg=null;
		for (int i = 0; i < msgs.getLength(); i++) {
			Element msg = (Element)msgs.item(i);
			if (msgType[1].equals(msg.getAttribute("name"))) {theMsg=msg; break;}
		}
		if (theMsg==null) {utils.log("Error: Could not find message definition: "+msgType[1]);return;}
		Node ch = theMsg.getFirstChild();
		while (ch.getNodeType()==Node.TEXT_NODE) {ch=ch.getNextSibling();}
		Element part=(Element)ch;	
		String partName=part.getAttribute("element").split(":")[1];
		NodeList elts = d.getElementsByTagName("xsd:element");
		Element theElt=null;
		for (int i = 0; i < elts.getLength(); i++) {
			Element elt = (Element)elts.item(i);
			if (partName.equals(elt.getAttribute("name"))) {theElt=elt; break;}
		}
		if (theElt==null) {utils.log("Error: Could not find schema definition: "+partName);return;}
		
		NodeList children=theElt.getChildNodes();
		Element theElt2=null; 
		for (int i = 0; i < children.getLength(); i++) {
			Node elt = children.item(i);
			if  ("complexType".equals(elt.getLocalName())) {
				NodeList nl2=elt.getChildNodes();
				for (int i2 = 0; i2 < nl2.getLength(); i2++) {
					Node elt2=nl2.item(i);
					if  ("sequence".equals(elt2.getLocalName())) {
						NodeList nl3=elt2.getChildNodes();
						for (int i3 = 0; i3 < nl3.getLength(); i3++) {
							Node elt3=nl3.item(i);
							if  ("element".equals(elt3.getLocalName())) {
								theElt2=(Element)elt3; break;
							}
						}
						break;
					}
				}
				break;
			}
		}
		if (theElt2==null) {utils.log("Error: Could not find schema element definition: "+partName);return;}
		utils.log("Argument name is: "+theElt2.getAttribute("name"));
		utils.log("Argument type is: "+theElt2.getAttribute("type"));
		String qType=theElt2.getAttribute("type");
		if (qType.length()==0) {qType="xsd:string";}
		String argType[]=qType.split(":");
		curNode.setAttribute("inputName",theElt2.getAttribute("name"));
		if (argType[0].equals("xsd")) {curNode.setAttribute("inputType",argType[1]);}
		else {curNode.setAttribute("inputType","sdo");curNode.setAttribute("inputSDO",argType[1]);}
		//		String argString="commonj.sdo.DataObject";
		//		utils.log(argString);
		
		
		if (argType[0].equals("xsd")) {return;}
		Element sdo=tgtDoc.createElement("sdo");
		sdo.setAttribute("name",argType[1]);
		processSDO(theElt2,sdo);
		sdodefs.appendChild(sdo);
		return;
		}
		
		
		if (e.getLocalName().equals("output")) {utils.log("Output: "+e.getAttribute("message")); 
		String[] msgType=e.getAttribute("message").split(":");
		NodeList msgs = d.getElementsByTagName("wsdl:message");
		Element theMsg=null;
		for (int i = 0; i < msgs.getLength(); i++) {
			Element msg = (Element)msgs.item(i);
			if (msgType[1].equals(msg.getAttribute("name"))) {theMsg=msg; break;}
		}
		if (theMsg==null) {utils.log("Error: Could not find message definition: "+msgType[1]);return;}
		Node ch = theMsg.getFirstChild();
		while (ch.getNodeType()==Node.TEXT_NODE) {ch=ch.getNextSibling();}
		Element part=(Element)ch;	
		String partName=part.getAttribute("element").split(":")[1];
		NodeList elts = d.getElementsByTagName("xsd:element");
		Element theElt=null;
		for (int i = 0; i < elts.getLength(); i++) {
			Element elt = (Element)elts.item(i);
			if (partName.equals(elt.getAttribute("name"))) {theElt=elt; break;}
		}
		if (theElt==null) {utils.log("Error: Could not find schema definition: "+partName);return;}
		
		NodeList children=theElt.getChildNodes();
		Element theElt2=null; 
		for (int i = 0; i < children.getLength(); i++) {
			Node elt = children.item(i);
			if  ("complexType".equals(elt.getLocalName())) {
				NodeList nl2=elt.getChildNodes();
				for (int i2 = 0; i2 < nl2.getLength(); i2++) {
					Node elt2=nl2.item(i);
					if  ("sequence".equals(elt2.getLocalName())) {
						NodeList nl3=elt2.getChildNodes();
						for (int i3 = 0; i3 < nl3.getLength(); i3++) {
							Node elt3=nl3.item(i);
							if  ("element".equals(elt3.getLocalName())) {
								theElt2=(Element)elt3; break;
							}
						}
						break;
					}
				}
				break;
			}
			
		}
		if (theElt2==null) {utils.log("Error: Could not find schema element definition: "+partName);return;}
		utils.log("Argument name is: "+theElt2.getAttribute("name"));
		utils.log("Argument type is: "+theElt2.getAttribute("type"));
		String[] argType=theElt2.getAttribute("type").split(":");
		String returnType=null;
		if (argType[0].equals("xsd")) {curNode.setAttribute("resultType",argType[1]);}
		else {curNode.setAttribute("resultType","sdo");curNode.setAttribute("resultSDO",argType[1]);}
		
		Element test=tgtDoc.createElement("test");
		curNode.appendChild(test);
		test.setAttribute("id","");
		test.setAttribute("description","Test Valid Call to: "+curNode.getAttribute("name"));
		if (argType[0].equals("xsd")) {return;}
		Element sdo=tgtDoc.createElement("sdo");
		sdo.setAttribute("name",argType[1]);
		processSDO(theElt2,sdo);
		sdodefs.appendChild(sdo);
		}
	}
	/**
	 * @param theElt2
	 * @param sdo2
	 */
	private void processSDO(Element e, Element parent) {
		Element out=tgtDoc.createElementNS("http://scatest/issw/ibm/com","_:TestDefinition");  
		out.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		out.setAttribute("xsi:type","l:"+e.getAttribute("type").split(":")[1]);
		out.setAttribute("xmlns:l","http://"+ife.getAttribute("lib"));
		if (parent!=null) {parent.appendChild(out);}
		
		//		Element temp=tgtDoc.createElement("temp");
		processComplexType(e,out);
		//		parent.appendChild(temp.getFirstChild());
		
	}
	/**
	 * @param elt
	 */
	private void processSeq(Node elt,Element parent) {
		NodeList nl=elt.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if ("element".equals(n.getLocalName())) {
				Element e=(Element)n;
				if (e.getAttribute("maxOccurs").length()>0) {processArray((Element)n,parent); }
				else {processOne((Element)n,parent);}
			}
		}
		
	}
	
	
	/**
	 * @param element
	 */
	private void processOne(Element e,Element parent) {
		String[] type=e.getAttribute("type").split(":");
		Element out=tgtDoc.createElement(e.getAttribute("name"));  
		if (parent!=null) {parent.appendChild(out);}
		if (type[0].equals("xsd")) {
			if (type[1].equals("string")) {out.appendChild(tgtDoc.createTextNode("XXX")); }
			if (type[1].equals("int")) {out.appendChild(tgtDoc.createTextNode("999"));}
			if (type[1].equals("boolean")) {out.appendChild(tgtDoc.createTextNode("true"));}
			if (type[1].equals("dateTime")) {out.appendChild(tgtDoc.createTextNode("2006-12-21T21:30:17.714Z"));}
		}
		else {utils.log(e.getAttribute("name")+":ComplexType");
		processComplexType(e,out);
		}
	}
	/**
	 * @param e
	 */
	private void processComplexType(Element e, Element parent) {
		//		Element out=tgtDoc.createElement(e.getAttribute("name"));  // this is a type not an element
		//		if (parent!=null) {parent.appendChild(out);}
		utils.log("Complextype="+e.getNodeName());
		String[] argType=e.getAttribute("type").split(":");
		NodeList elts=d.getElementsByTagName("xsd:complexType");
		Element typedef=null;
		for (int i = 0; i < elts.getLength(); i++) {
			Element elt = (Element)elts.item(i);
			if (argType[1].equals(elt.getAttribute("name"))) {typedef=elt; break;}
		}
		if (typedef==null) {utils.log("Error: Could not find type definition: "+argType[1]);return;}
		NodeList nl=typedef.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node elt=nl.item(i);
			if  ("sequence".equals(elt.getLocalName())) {
				processSeq(elt,parent); break;
			}
		}
		
	}
	/**
	 * @param n
	 */
	private void processArray(Element e,Element parent) {
		processOne(e,parent);
		processOne(e,parent);
	}
	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
