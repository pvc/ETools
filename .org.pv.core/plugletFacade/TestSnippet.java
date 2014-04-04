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
import java.lang.reflect.Method;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;

import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.pbe.text.HexDump;

import org.eclipse.gmf.runtime.notation.View;
import org.pv.core.Utils;
 

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestSnippet   {
	Utils utils=Utils.getSingleton();
	String className="com.ibm.p4eb.SVG.SVGTransform";
	
	public void plugletmain(String[] args) {
		Object x = utils.getBaseSelectionList().get(0);
		Class xc = x.getClass();
		utils.log("Class: "+xc.getName());
		utils.log("SuperClass: "+xc.getSuperclass().getName());
		utils.log("***Interfaces:");
		Class[] xi = xc.getInterfaces();
		for (int i = 0; i < xi.length; i++) {
			Class c = xi[i];
			utils.log(c);
		}
		utils.log("***Methods:");
		Method[] xm = xc.getMethods();
		for (int i = 0; i < xm.length; i++) {
			Method c = xm[i];
			utils.log(c);
		}
		View v=(View)((IAdaptable)x).getAdapter(View.class);
		utils.log("ViewAdapter: "+v);

	}	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
