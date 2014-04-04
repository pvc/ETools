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
 * Created on 18-Aug-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.pbe.trees;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.pv.core.Utils;





/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeNodeFileAdapter implements TreeNode {
	private File f;
	private TreeNode parent;
	private List children;
	private boolean isRoot=false;
	static final Utils utils=Utils.getSingleton();

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.tree.TreeNode#getParent()
	 */
	public TreeNodeFileAdapter(File f){
		this.f=f;
	}
	
	/**
	 * @param source
	 */
	public TreeNodeFileAdapter(String source) {
		this.f=new File(source);
		// TODO Auto-generated constructor stub
	}

	public TreeNode getParent() {
		// TODO Auto-generated method stub
		if (isRoot) {return null;} else {return parent;}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.tree.TreeNode#setParent(org.eclipse.emf.edit.tree.TreeNode)
	 */
	public void setParent(TreeNode value) {
		parent=value;
		isRoot=false;
	}



	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.tree.TreeNode#getData()
	 */
	public Object getData() {
		// TODO Auto-generated method stub
		return f;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.tree.TreeNode#setData(org.eclipse.emf.ecore.EObject)
	 */
	public void setData(Object value) {
		f=(File)value;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.EObject#eAllContents()
	 */
	public TreeIterator iterator() {
		// TODO Auto-generated method stub
		return new TreeIterator(this);
	}


		/* (non-Javadoc)
		 * @see com.ibm.p4eb.trees.TreeNode#isRoot()
		 */
		public boolean isRoot() {
			// TODO Auto-generated method stub
			return isRoot;
		}

		/* (non-Javadoc)
		 * @see com.ibm.p4eb.trees.TreeNode#setRoot(boolean)
		 */
		public void setRoot(boolean b) {
			isRoot=b;
		}

		/* (non-Javadoc)
		 * @see com.ibm.p4eb.trees.TreeNode#isLeaf()
		 */
		public boolean isLeaf() {
			return f.isFile();
		}

		/* (non-Javadoc)
		 * @see com.ibm.p4eb.trees.TreeNode#getChildren()
		 */
		public List getChildren() {
			if (children==null) {
				children=new LinkedList();
				if (f.isDirectory()) {
					File[] ch = f.listFiles();
					if (ch==null) {utils.log("***Error accessing contents of:"); utils.log(f.getPath()); return children;}
					for (int i = 0; i < ch.length; i++) {
						children.add(new TreeNodeFileAdapter(ch[i]));
					}
				}
			}
			return children;
		}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
