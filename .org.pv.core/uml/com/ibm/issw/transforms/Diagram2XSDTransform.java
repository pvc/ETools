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
package com.ibm.issw.transforms;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.pv.core.Utils;
import org.w3c.dom.Document;

import com.ibm.issw.UMLDiagrams.DiagramAnalyser;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Diagram2XSDTransform {
	Utils utils=Utils.getSingleton();
	

	public Document execute(Diagram d) {
		DiagramAnalyser da = new DiagramAnalyser(d);
		Node root=da.getRoot();
		
		DiagramRoot2XSDTransform ad5=new DiagramRoot2XSDTransform();
		Document appdoc=(Document)ad5.execute(root);
		return appdoc;
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
