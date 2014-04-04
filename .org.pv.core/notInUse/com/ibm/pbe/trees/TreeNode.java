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

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface TreeNode {
	public abstract boolean isRoot();
	public abstract void setRoot(boolean b);
	public abstract boolean isLeaf();
	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.tree.TreeNode#getParent()
	 */public abstract TreeNode getParent();

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.tree.TreeNode#setParent(org.eclipse.emf.edit.tree.TreeNode)
	 */public abstract void setParent(TreeNode value);

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.tree.TreeNode#getChildren()
	 */public abstract List getChildren();

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.tree.TreeNode#getData()
	 */public abstract Object getData();

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.tree.TreeNode#setData(org.eclipse.emf.ecore.EObject)
	 */public abstract void setData(Object value);
}