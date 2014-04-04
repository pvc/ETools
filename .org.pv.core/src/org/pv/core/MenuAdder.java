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
package org.pv.core;

//Uses adapter to gen profile from diagram
import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.SubContributionItem;
import org.pv.core.Utils;
import org.pv.plugin.Copyright;


public class MenuAdder  {
	final static Utils utils=Utils.getSingleton();
	final PVLoader runner=new PVLoader();
	static final String targetMenu="PBEMenu";
	final static MenuManager mm=getTargetMenuManager(targetMenu);
	
	public void run() {
		IJavaElement j=null;
		final IFile f=utils.getActiveFile();
		if (!utils.isFileType(f,"java")) {utils.log("Please select a Java class");return;}
		PBEPath path = new PBEPath(f);
//		if ( (j==null) || j.getElementType()!=IJavaElement.COMPILATION_UNIT) {utils.log("Please select a Java class");return;}
//		j = JavaCore.create(f);
//		ICompilationUnit ju = (ICompilationUnit)j;
//		utils.log(j.getElementName());
//		String className="";
//		try {className=ju.getTypes()[0].getFullyQualifiedName();} catch (JavaModelException e1) {e1.printStackTrace(utils.getLogger());return;}

		IAction a=new Action() {
			public void run() { 
				runner.execute(f);
			}
		};
//		a.setText("Run "+className.substring(className.lastIndexOf('.')+1));
		a.setText("Run "+path.getNameWithoutExtension());
		mm.add(a);
		utils.log(path.getNameWithoutExtension()+" successfully added to PBECore Run Menu");
		return;
	}
	public static MenuManager getTargetMenuManager(String menuId) {
		IMenuManager mm;IContributionItem m;
		org.eclipse.ui.internal.WorkbenchWindow ww = (org.eclipse.ui.internal.WorkbenchWindow)utils.wb.getActiveWorkbenchWindow();
		mm=ww.getMenuManager();
		m = mm.findUsingPath(menuId);
		SubContributionItem sci = (SubContributionItem)m;
		MenuManager m2 = (MenuManager)sci.getInnerItem();
		return m2;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
