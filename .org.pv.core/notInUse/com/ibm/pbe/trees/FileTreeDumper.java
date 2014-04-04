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
 * Created on 27-Oct-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.pbe.trees;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.pbe.trees.TreeIterator;
import com.ibm.pbe.trees.TreeNode;
import com.ibm.pbe.trees.TreeNodeFileAdapter;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileTreeDumper {
	Utils utils=Utils.getSingleton();
	String source="C:/SDLC/SDLC4ESB/LocalArtifacts";
	
	public void execute() {
		utils.log("****************************Beginning Test Run");
		
		TreeIterator it=new TreeIterator(new TreeNodeFileAdapter(source));
		for (; it.hasNext();) {
			TreeNode tn = (TreeNode) it.next();
			if (!tn.isLeaf()) {continue;} 
			File f=(File)tn.getData();
			IPath relPath = new Path(f.getPath().substring(source.length()));
			utils.p(relPath);
		}
	}	
	/**
	 * @return Returns the source.
	 */
	public String getSourceDir() {
		return source;
	}
	/**
	 * @param source The source to set.
	 */
	public void setSourceDir(String source) {
		this.source = source;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
