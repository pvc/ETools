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
package com.ibm.pbe.text;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.pv.core.Utils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



public class HexDump  {
	Utils utils=Utils.getSingleton();
	String sourceName;
	String targetName;
	String rootName;
	List sourcePaths=new LinkedList();
	TextFileDocumentProvider tp = new TextFileDocumentProvider();
	String rootNameVar="<attr node=\"app\" name=\"name\" />";
	
	public void run() {}
	public void execute() {
		
		// todo - find source classpathentry & net it off front of resource path for java
		// suppress class files, & frag
		// how to elim timing problems (dont build auto)
		// 
		IResource sel = utils.getSelectedResource();
		if ((sel==null)||IResource.FILE!=sel.getType()) {utils.p("Please select a File!"); return;}
		
		IFile source = (IFile)sel;
		InputStream in;
		try {
			in = source.getContents();
		
		int b;String line="";
		int max=70;
		utils.p("****************");
		for (int i=0;i<max;i++){
			b=in.read();
//			utils.p("Byte: "+b);
			utils.p(Integer.toHexString(b)+' '+(char)b);
//			utils.p("Char: "+(char)b);
			
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}

//		utils.saveDoc(d,utils.getFile(utils.changeExtension(utils.path2URI(source.getFullPath()),"xml")));
	}
		
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
