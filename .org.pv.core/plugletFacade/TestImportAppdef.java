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

import com.ibm.issw.actionImpl.ImportAppdef;
import com.ibm.issw.actionImpl.ImportXML;
import com.ibm.pbe.transforms.XML2DOMTransform;
 


public class TestImportAppdef    {
	
	Utils utils=Utils.getSingleton();
//	Diagram2DOMTransform ad=new Diagram2DOMTransform();
	ImportAppdef actionImpl=new ImportAppdef();
	
	public void plugletmain(String[] args) {
		utils.log("Starting run  *************");
		actionImpl.execute();
		utils.log("Completed run *************");
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
} 