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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.pv.core.Utils;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileFinder {
	Utils utils=Utils.getSingleton();
	String root="C:/SDLC/SDLC4ESB/LocalArtifacts";
	IContainer rootFolder;
	String target="I_PostCode.java";
	File file=null;
	IFile ifile=null;
	IPath relPath;
	

	public IFile find(IContainer folder, String name) {
		rootFolder=folder;
		target=name;
		execute();
		return ifile;
	}
	public IFile find(IContainer folder) {
		rootFolder=folder;
		execute();
		return ifile;
	}
	public IFile find(String name) {
		target=name;
		execute();
		return ifile;
	}
	
	public void execute() {
		utils.log("****************************Beginning Test Run");
		root=rootFolder.getLocation().toString();
		File found=null;
		TreeIterator it=new TreeIterator(new TreeNodeFileAdapter(root));
		for (; it.hasNext();) {
			TreeNode tn = (TreeNode) it.next();
			if (!tn.isLeaf()) {continue;} 
			File f=(File)tn.getData();
			if (f.getName().equals(target)) {found=f; break;}
		}
		if (found!=null) {
		relPath = new Path(found.getPath().substring(root.length()));
		ifile=(IFile)rootFolder.findMember(relPath);
		} else {ifile=null; relPath=null;}
	}	
	
	/**
	 * @return Returns the target.
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target The target to set.
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @return Returns the relPath.
	 */
	public IPath getRelPath() {
		return relPath;
	}
	/**
	 * @param root The root to set.
	 */
	public void setRoot(String root) {
		this.root = root;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
