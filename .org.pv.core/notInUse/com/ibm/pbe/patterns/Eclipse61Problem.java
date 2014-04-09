package com.ibm.pbe.patterns;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.pv.core.Utils;


public class Eclipse61Problem {
	Utils utils=Utils.getSingleton();
	public void run() {
		IProject p=null;
		try {
			p = utils.getProject("TestJetMedLib");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IJavaProject jp = JavaCore.create(p);
		if (jp==null) {return;}
		utils.p(""+jp);  // Hangs
	}
}
