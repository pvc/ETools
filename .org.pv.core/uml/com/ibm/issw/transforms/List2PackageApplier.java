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
package com.ibm.issw.transforms;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;
import org.osgi.framework.Bundle;
import org.pv.core.Utils;

import com.ibm.pbe.graphs.Graph;
import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class List2PackageApplier implements Executable {
	
	Utils utils=Utils.getSingleton();
	
	char colon=':';
	char uscore='_';
	Bundle b=Platform.getBundle("com.ibm.pbe.umlToolkit");
	URL url=b.getEntry("/profiles/PBEMMProfile.epx"); //must include path, leading slash is optional
	URI profileURI=URI.createURI(url.toString());
	
	Stereotype stereotype;
	Stereotype xmlns;
	Graph g;
	Package m;
	URI modelURI;
	String modelName;
	/**
	 * @param m
	 */
	public List2PackageApplier(Package m) {
		this.m = m;
	}
//	public List2PackageApplier(String filepath) {
//		modelURI=utils.string2URI(filepath);
//	}
//	public List2PackageApplier(URI uri) {
//		modelURI=uri;
//	}
	/**
	 * @param o
	 */

	
	/* (non-Javadoc)
	 * @see Executable.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return (Package)execute((List)o);
	}

	public Package execute(List names) {
		utils.log("Entering: "+this.getClass().getName());
//		URI testModelURI= utils.string2URI("GenProfile/TestMetamodel.emx");
		for (Iterator iter = names.iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			processNode(name);
		}
		
		
//		try {
//			UMLModeler.saveModel(m);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		return m;
	}

	public Class processNode(String name) {
		Class c=(Class)m.getOwnedMember(name);
		if (c==null) {
			c = (Class)m.createOwnedClass(name, false);
		}
		return c;
	}
	


	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
