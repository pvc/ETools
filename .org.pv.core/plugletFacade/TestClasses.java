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
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.issw.actionImpl.ImportXML;
import com.ibm.pbe.transforms.XML2DOMTransform;
 


public class TestClasses    {
	
	Utils utils=Utils.getSingleton();
//	Diagram2DOMTransform ad=new Diagram2DOMTransform();
//	GenMetamodel actionImpl=new GenMetamodel();
	ImportXML actionImpl=new ImportXML();
	
	public void plugletmain(String[] args) {
		utils.log("Starting run *************");
		Object o=new Object();
		Class c=o.getClass();
		utils.log("Class="+c);
		Class cc=c.getClass();
		utils.log("Class="+cc);
		Class ccc=cc.getClass();
		utils.log("Class="+ccc);
		utils.log("Is Class an Object="+(ccc instanceof Object));
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
} 