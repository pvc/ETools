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
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
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



public class CSV2XML  {
	Utils utils=Utils.getSingleton();
	String sourceName;
	String targetName;
	String rootName;
	List sourcePaths=new LinkedList();
	TextFileDocumentProvider tp = new TextFileDocumentProvider();
	String rootNameVar="<attr node=\"app\" name=\"name\" />";
	
	public void execute() {
		// todo - find source classpathentry & net it off front of resource path for java
		// suppress class files, & frag
		// how to elim timing problems (dont build auto)
		// 
		
		IResource sel = utils.getSelectedResource();
		if ((sel==null)||IResource.FILE!=sel.getType()) {utils.p("Please select a File!"); return;}
		
		IFile source = (IFile)sel;
		LineNumberReader in=null;
		try {
			in = new LineNumberReader(new InputStreamReader(source.getContents(),Charset.forName(source.getCharset())));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
		String tab=String.valueOf('\u0009');
		Document d=utils.getNewDocument("root");
		Node root=d.getFirstChild();
		String line;
		try {
			for (int rowno=0;null!=(line=in.readLine());rowno++) {
				Element row=d.createElement("row");
				row.setAttribute("id",String.valueOf(rowno));
				root.appendChild(row);
				String[] fields = line.split(tab);
				for (int i = 0; i < fields.length; i++) {
					Element field=d.createElement("field");
					row.appendChild(field);
					field.setAttribute("id",String.valueOf(i));
					field.setAttribute("value",fields[i]);
				}
			}
		} catch (DOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(utils.getLogger());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(utils.getLogger());
		}
		utils.save(d,utils.changeExtension(source,"xml"));
	}
		
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
