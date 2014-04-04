package com.ibm.pbe.transforms;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.xml.transform.OutputKeys;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XML2DOMTransform {
	Utils utils= Utils.getSingleton();
	/**
	 * filepath:=projectname/path/filename
	 * 
	 */
	public Document execute(String filepath) {
		IFile f=ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filepath));
		return execute(f);
	}
	
	public Document execute(IFile f) {
		Document d=null;
		try {
			d=execute(f.getContents());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			utils.log("Exception thrown:");
			utils.log(e);
		}
//		utils.log("Loaded XML Doc: "+f);
		if (d!=null) {d.setUserData("pbeFile",f,null);}
		return d;
	}
	public Document execute(InputStream inStream) {
//		utils.log("Entering execute inputStream");
		DocumentBuilderFactory dbf=null;
		DocumentBuilder db=null;
		Document d=null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			db=dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			utils.log(e);
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			utils.log(e);
		}
//		utils.log("Namespace aware="+db.isNamespaceAware());
		try {
			d = db.parse(inStream);
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			utils.log(e1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			utils.log(e1);
		}
//		utils.log("Leaving execute inputStream");
		return d;
		
	}
}
