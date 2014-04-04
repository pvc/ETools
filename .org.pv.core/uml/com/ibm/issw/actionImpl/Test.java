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
 * Created on 20-May-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.actionImpl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.pbe.rsa.interfaces.Executable;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.pv.core.Utils;

/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test {
	Utils utils=Utils.getSingleton();
	
	public void run()  {
		TxWrapper t = new TxWrapper(new Executable() {
			public Object execute(Object o) {
		List elts = utils.getSelectedUMLElements();
		Node node1=(Node)elts.get(0);
		Node node2=(Node)elts.get(1);
		Class c1=(Class) node1.getElement();
		Class c2=(Class) node2.getElement();
		Package p=c1.getNearestPackage();
		Diagram d=node1.getDiagram();
		utils.dump(c2);
				
//		Association a = (Association)p.createOwnedMember(UMLPackage.eINSTANCE.getAssociation());
//		Type s=(Type)p.getMember("String");
		AggregationKind noAgg = AggregationKind.NONE_LITERAL;
		Association a = c1.createAssociation(false, noAgg, "fred", 11, 11, c2 , false, noAgg, null, 1,1);
		a.setName("assoc");
		utils.udh.createEdge(node1, node2, a);
		utils.log("Edge created");
		Collection refs = utils.eoh.getReferencers(a, null);
		for (Iterator iter = refs.iterator(); iter.hasNext(); ) {
			EObject ref = (EObject) iter.next();
			utils.dump(ref);
		}
//		a.destroy();
		utils.log("Destroyed");
		refs = utils.eoh.getReferencers(a, null);
		for (Iterator iter = refs.iterator(); iter.hasNext(); ) {
			EObject ref = (EObject) iter.next();
			utils.dump(ref);
			if (ref instanceof Edge) { ((Element)((Edge)ref).getElement()).destroy(); utils.udh.destroyView((Edge)ref); };
		}
//		elts=p.getOwnedElements();
//		for (Iterator iter = elts.iterator(); iter.hasNext(); ) {
//			NamedElement ne = (NamedElement) iter.next();
//			if (ne instanceof Association) {
//				ne.destroy();
//			}
//		}
//		utils.dump(a);
//		a.destroy();
//		utils.dump(a);
		return null;
		}

			
		});
		t.execute();
		
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
