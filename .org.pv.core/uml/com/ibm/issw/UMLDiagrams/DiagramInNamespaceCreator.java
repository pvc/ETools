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
 * Created on 30-Nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.UMLDiagrams;
import java.util.Iterator;
import java.util.List;

import org.eclipse.uml2.uml.Namespace;

import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.pv.core.Utils;

import com.ibm.xtools.uml.ui.diagram.IUMLDiagramHelper;
import com.ibm.xtools.umlnotation.UMLDiagramKind;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DiagramInNamespaceCreator implements Executable{
	String dName;
	UMLDiagramKind dk=UMLDiagramKind.CLASS_LITERAL;
	boolean mainDiagram=true;
	boolean doOpenDiagram=true;
	static Utils utils=Utils.getSingleton();
	
	public DiagramInNamespaceCreator()  {
	}
	public DiagramInNamespaceCreator(String diagramName){
		dName=diagramName;
	}
	public Diagram execute(Namespace target) {
		IUMLDiagramHelper udh = UMLModeler.getUMLDiagramHelper();
		if (dName==null) {dName=target.getName();}
		List diags=udh.getDiagrams(target);
		Diagram d=null;
		for (Iterator iter = diags.iterator(); iter.hasNext();) {
			Diagram diag = (Diagram) iter.next();
			if (diag.getName().equals(dName)) {d=diag; break;}
		}
		if (d==null) {d=udh.createDiagram(target,dk);d.setName(dName);}
		if (mainDiagram) {udh.setMainDiagram(target,d);}
		if (doOpenDiagram) {udh.openDiagramEditor(d);}
		return d;
	}
	/**
	 * @return Returns the dk.
	 */
	public UMLDiagramKind getDiagramKind() {
		return dk;
	}
	/**
	 * @param dk The dk to set.
	 */
	public void setDiagramKind(UMLDiagramKind dk) {
		this.dk = dk;
	}
	/**
	 * @return Returns the dName.
	 */
	public String getDiagramName() {
		return dName;
	}
	/**
	 * @param name The dName to set.
	 */
	public void setDiagramName(String name) {
		dName = name;
	}
	/* (non-Javadoc)
	 * @see com.ibm.issw.rsa.interfaces.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		return execute((Namespace)o);
	}
	/**
	 * @return Returns the mainDiagram.
	 */
	public boolean isMainDiagram() {
		return mainDiagram;
	}
	/**
	 * @param mainDiagram The mainDiagram to set.
	 */
	public void setMainDiagram(boolean mainDiagram) {
		this.mainDiagram = mainDiagram;
	}
	/**
	 * @return Returns the openDiagram.
	 */

	/**
	 * @return Returns the doOpenDiagram.
	 */
	public boolean isDoOpenDiagram() {
		return doOpenDiagram;
	}
	/**
	 * @param doOpenDiagram The doOpenDiagram to set.
	 */
	public void setDoOpenDiagram(boolean doOpenDiagram) {
		this.doOpenDiagram = doOpenDiagram;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
