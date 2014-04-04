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
 * Created on 13-Dec-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.pbe.SVG;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Stack;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO 
 * 1) type=literal is bad parctice - check for ns/localname instead
 * 2) wsdl imports need to pool schemas into the (single) types node 
 * 3) imports/includes at 2 levels or more of nesting will  not be deduplicated ??
 * 
 */
public class SVGTransform implements Executable{
	
	static Utils utils=Utils.getSingleton();
	static final String xsd="http://www.w3.org/2001/XMLSchema";
	static final String wsdl="http://schemas.xmlsoap.org/wsdl/";
	Document doc;
	URI uri;
	private IPath fileDir;
	Stack stack=new Stack();
	Properties includeList=new Properties();
	private Document d;
	private Document tgtDoc;
	private float cellx;
	private float celly;
	private String cellxString;
	private String cellyString;
	private float gridMinx=99;
	private float gridMaxx=-1;
	private float gridMiny=99;
	private float gridMaxy=-1;
	private Hashtable cellMap=new Hashtable();
	private boolean shallow=false;
	private boolean deep=true;
	
	/**
	 * @return Returns the d.
	 */
	public Document getDoc() {
		return doc;
	}
	/**
	 * @param d The d to set.
	 */
	public void setDoc(Document d) {
		this.doc = d;
	}
	
	
	
	public Object execute(Object o) {
		IResource f = utils.getSelectedResource();
		if (!((f instanceof IFile) && ("svgdef".equalsIgnoreCase(f.getFileExtension())) ) )  {utils.log("Please select an svgdef file"); return null;}
		IFile tgtFile=utils.getFileWithNewExtension((IFile)f,"xml");
		d = utils.getDoc((IFile)f);
		tgtDoc=utils.getNewDocument();
		processNode(d.getDocumentElement(),tgtDoc);
		utils.save(tgtDoc,tgtFile);
//		PatternApplicationStatus ps = utils.execDPTK(tgtDoc,"com.ibm.p4eb.svgpattern",f.getProject(),null);
//		utils.dumpStatus(ps);
		return tgtDoc;
	}
	
	/**
	 * @param d2
	 * @param tgtDoc2
	 */
	private void processNode(Node e, Node parent) {
		Node e2=tgtDoc.importNode(e,shallow);
		parent.appendChild(e2);
		NodeList nl=e.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n= nl.item(i);
			if ("grid".equals(n.getLocalName())) {processGrid((Element)n,e2);}
			else {processNode(n,e2);}
		}
	}
	/**
	 * @param firstChild
	 * @param app
	 */
	private void processGrid(Element e, Node parent) {
		Element app=utils.addElement(parent,"grid");
		String[] cellSize=e.getAttribute("cellSize").split(",");
		cellx=Float.parseFloat(cellSize[0]);
		if (cellSize.length==2) {celly=Float.parseFloat(cellSize[1]);} else {celly=cellx;}
		cellxString=String.valueOf(cellx);
		cellyString=String.valueOf(celly);
		NodeList nl=e.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n= nl.item(i);
			if ("cell".equals(n.getLocalName())) {processCell((Element)n,app);}
		}
		for (int i = 0; i < nl.getLength(); i++) {
			Node n= nl.item(i);
			if ("line".equals(n.getLocalName())) {processLine((Element)n,app);}
		}
		
		float targetWidth=Float.parseFloat(((Element)e.getParentNode()).getAttribute("bodyWidth"));
		double width=(1+gridMaxx-gridMinx)*cellx;
		double height=(1+gridMaxy-gridMiny)*celly;
		double targetHeight=height*targetWidth/width;
		String x1=String.valueOf(gridMinx*cellx);
		String x2=String.valueOf((1+gridMaxx-gridMinx)*cellx);
		String y1=String.valueOf(gridMiny*celly);
		String y2=String.valueOf((1+gridMaxy-gridMiny)*celly);
		
		app.setAttribute("viewBox",x1+","+y1+","+x2+","+y2);
		app.setAttribute("startx",x1);
		app.setAttribute("starty",y1);
		app.setAttribute("width",String.valueOf(width));
		app.setAttribute("height",String.valueOf(height));
//		app.setAttribute("width",String.valueOf(targetWidth));
//		app.setAttribute("height",String.valueOf(targetHeight));
		app.setAttribute("name",e.getAttribute("name"));
	}
	
	/**
	 * @param element
	 * @param app
	 */
	private void processLine(Element e, Element parent) {
		Point from=(Point)cellMap.get(e.getAttribute("from"));
		double startx=from.x+cellx*0.5;
		double starty=from.y+celly;
		Point to=(Point)cellMap.get(e.getAttribute("to"));
		double endx=to.x+cellx*0.5;
		double endy=to.y;
		Element line=utils.addElement(parent,"line");
		line.setAttribute("x1",String.valueOf(startx));
		line.setAttribute("x2",String.valueOf(endx));
		line.setAttribute("y1",String.valueOf(starty));
		line.setAttribute("y2",String.valueOf(endy));
		line.setAttribute("type",e.getAttribute("type"));
	}
	/**
	 * @param element
	 * @param app
	 */
	private void processCell(Element e, Element parent) {
		String[] coords=e.getAttribute("coords").split(",");
		float cx=Float.parseFloat(coords[0]);
		float cy=Float.parseFloat(coords[1]);
		float x=cx*cellx;
		float y=cy*celly;
		
		String shapeType=e.getAttribute("shape");
		Element cell;
		if ("ellipse".equals(shapeType)) {
			cell=utils.addElement(parent,"ellipse");
			cell.setAttribute("rx",String.valueOf(cellx/2));
			cell.setAttribute("ry",String.valueOf(celly/2));
			cell.setAttribute("cx",String.valueOf(x+cellx/2));
			cell.setAttribute("cy",String.valueOf(y+celly/2));
		}
		else {
		cell=utils.addElement(parent,"rect");
		cell.setAttribute("width",cellxString);
		cell.setAttribute("height",cellyString);
		cell.setAttribute("x",String.valueOf(x));
		cell.setAttribute("y",String.valueOf(y));
		cell.setAttribute("id",e.getAttribute("id"));
		}
		cell.setAttribute("id",e.getAttribute("id"));
		if (e.getAttribute("link").length()>0) {cell.setAttribute("link",e.getAttribute("link"));}
		
		cellMap.put(e.getAttribute("id"),new Point(x,y));
		String[] text=e.getAttribute("text").split("#");
		Element te1=utils.addElement(cell,"text");
		te1.setAttribute("x", String.valueOf(x+20));
		te1.setAttribute("y", String.valueOf(y+20));
		utils.addText(te1,text[0]);
		if (text.length>1) {
			Element te2=utils.addElement(cell,"text");
			te2.setAttribute("x", String.valueOf(x+20));
			te2.setAttribute("y", String.valueOf(y+35));
			utils.addText(te2,text[1]);
		}
		Element count=utils.addElement(cell,"countPosition");
		count.setAttribute("x", String.valueOf(x+20));
		count.setAttribute("y", String.valueOf(y+50));
		
		gridMinx=Math.min(cx,gridMinx);
		gridMaxx=Math.max(cx,gridMaxx);
		gridMiny=Math.min(cy,gridMiny);
		gridMaxy=Math.max(cy,gridMaxy);
		
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}




