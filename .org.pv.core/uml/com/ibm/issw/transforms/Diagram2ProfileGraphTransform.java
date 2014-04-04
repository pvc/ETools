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

package com.ibm.issw.transforms;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import com.ibm.pbe.graphs.Connector;
import com.ibm.pbe.graphs.Graph;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.pv.core.Utils;

public class Diagram2ProfileGraphTransform {
	Utils utils=Utils.getSingleton();
	Diagram d;
	public Diagram2ProfileGraphTransform(Diagram d) {
		this.d=d;
	}

	public Graph execute(Diagram d) {
		this.d=d;
		return execute();
	}
	public Graph execute() {
		Graph g=new Graph();
		List elts=d.getChildren();
		for (Iterator iter = elts.iterator(); iter.hasNext();) {
			View v = (View) iter.next();
			EObject eo=v.getElement();
			if (eo instanceof Class) {
//				utils.log("Adding to profile: "+((Class)eo).getName());
				g.addNode(eo);
//				dumpAttributes((Class)eo);
			}
			else {utils.log("Not a stereotype: "+v.getElement());}
		}
		List edges=d.getEdges();
		for (Iterator iter = edges.iterator(); iter.hasNext();) {
			Edge v = (Edge) iter.next();
//			utils.log("Edge: "+v);
			EObject eo=v.getElement();
//			utils.log("Is: "+eo);
//			utils.log(v.getSource().getElement());
//			utils.log(v.getTarget().getElement());
			if (eo==null) {continue;}
			if (eo instanceof Generalization) {
				Connector c=new Connector(v.getSource().getElement(),v.getTarget().getElement());
				c.setValue(eo);
				g.addConnector(c);
			}
			
//			utils.log("Edge has children: "+v.getChildren().size()); 
		}
//		g.dump();
		
//		g.addNode(utils.getModel(d).getOwnedMember("DocumentRoot"));
		
		if (true) {return g;}
//		EObject eo=v.getElement();
//		if (eo==null) {utils.log("Please select a Requester in an ESB Flow Diagram to begin generation"); return;}
//		if (!(eo instanceof Class)) {utils.log("Please select a Requester Class in an ESB Flow Diagram to begin generation"); return;}
//		Class ec=(Class)eo;
//		String eName=ec.getName();
////		utils.log(eName);
////		if (!eName.equals("Requester")) {utils.log("Please select a Requester"); return;}
//		EList el=v.getSourceEdges();
//		if (el.size()!=1) {utils.log("Requester must have just one target - please correct"); return;}
//		Edge edge=(Edge)el.get(0);
//		View tgt=edge.getTarget();
//		EObject eo2=tgt.getElement();
//		if (!(eo2 instanceof Component)) {utils.log("Requester must connect to a Mediation Component - please correct"); return;}
//		if (ec.getAppliedStereotype("P4ebESBProfile::TestRequester")!=null) {genClient(ec);}
//				
//		utils.log("Beginning ESBFlow analysis ...");
////		ESBFlow esbflow=new ESBFlow(tgt);
//		utils.log("Inbound message from client -->");
////		esbflow.dump(out);
//		utils.log("ESBFlow analysis complete");
//		utils.log("");
//		utils.log("Beginning Build ...");
////		esbflow.build(out);
//		utils.log("ESBFlow build completed successfully");
//		utils.log("Complete any customisation of POVs prior to Flow deployment");
//		utils.log("");
		return g;
	}	
	public void dumpAttributes(Class c) {
		utils.log("Attributes of: "+c);
		Collection atts=c.getMembers();
		for (Iterator iter = atts.iterator(); iter.hasNext();) {
//			Element p = (Element) iter.next();
			Object o=iter.next();
			if (o instanceof Property) {
			Property p=(Property)o;
			Type d= p.getType(); // can be UML2 PrimitiveType or Class
			utils.log("\t"+p.getName()+": "+d.getName()+": "+d.getClass());
				
			}
//			utils.log(p.getClass());
//			utils.log(p.getName()+": "+p.getClass());
//			utils.log(p.getClass_()); // OWNING Class (not Type!)
//			utils.log(p.getDefault());
//			utils.log(p.getDefaultValue());
//			utils.log(p.getDatatype());
		}
	}
	
	
/**
	 * @param ec
	 */
	private void genClient(Class ec) {
		if (true) {return;} // NOT YET WORKING!!
//		GenTestClient gtc=new GenTestClient(new MediationModel(),"Generating Test Client");
//		gtc.schedule();
	}



	static String copyright() { return Copyright.PV_COPYRIGHT; }
}