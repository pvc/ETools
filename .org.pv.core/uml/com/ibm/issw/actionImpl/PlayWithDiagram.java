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
package com.ibm.issw.actionImpl;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.pbe.rsa.interfaces.Executable;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.NotationPackageImpl;
import org.pv.core.Utils;

import com.ibm.xtools.uml.ui.diagram.IUMLDiagramHelper;
import com.ibm.xtools.umlnotation.UMLDiagramStyle;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PlayWithDiagram implements Executable {
	Utils utils=Utils.getSingleton();
	boolean allowProfileUpdate=true;
	static Node v1;
	static Node v2;
	static Association a;
	IUMLDiagramHelper udh = utils.udh;
	
	public void run() {
		utils.log("****************************Beginning Script Run at "+new Date());
//		String inputFile="NLS Exception Pattern/sample.appdef";
		boolean valid=false;


		TxWrapper ad2 = new TxWrapper(new PlayScript2());
		ad2.execute(null);
		

		utils.log("****************************Script Run Complete "+new Date());
	}
	
	
	
	public class PlayScript implements Executable {
		public Object execute(Object o) {
			IEditorReference[] eds = utils.getActivePage().getEditorReferences();
//			utils.dump(eds);
//			for (int i = 0; i < eds.length; i++) {
//			utils.log(eds[i].getContentDescription());
//			utils.log(eds[i].getFactoryId());
//			utils.log(eds[i].getId());
//			utils.log(eds[i].getName());
//			utils.log(eds[i].getTitle());
//			utils.log(eds[i].getTitleToolTip());
//			IEditorPart ed = eds[i].getEditor(false);
//			IEditorInput edinp = ed.getEditorInput();
//			utils.log(edinp.getName());
//			utils.log(edinp.getAdapter(Diagram.class));
//			utils.log("***");
//			}
			
			boolean found=false;
			Loop loop=new Loop() { public int process(Object input){
				utils.log(input); 
				IEditorReference edr = (IEditorReference)input;
			if (!"ModelerDiagramEditor".equals(edr.getId())) {return CONTINUE;}
			IEditorPart ed = edr.getEditor(false);
			IEditorInput edinp = ed.getEditorInput();
			result=(Diagram)edinp.getAdapter(Diagram.class);
			return BREAK;
			}} ; 
			Diagram d=(Diagram)loop.run(eds);
//			for (int i = 0; i < eds.length; i++) {
//			if (!"ModelerDiagramEditor".equals(eds[i].getId())) {continue;}
//			IEditorPart ed = eds[i].getEditor(false);
//			IEditorInput edinp = ed.getEditorInput();
//			d=(Diagram)edinp.getAdapter(Diagram.class);
//			break;
//			}
			utils.log("Starting loop with "+d);
			Loop loop2=new Loop() {public int process(Object input){utils.log(input); return CONTINUE;} } ; 
			loop2.run(d.getChildren());
			if (true) {return null;}
//			EObject sel=utils.getSelectedUMLElement();
//			if (sel==null) {utils.log("Please select a UML element!"); return null;} else {utils.log(sel);}
//			List sels=utils.getSelectedUMLElements();
//			Node v1=(Node)sels.get(0);
//			Node v2=(Node)sels.get(1);
			View v=(View)utils.getSelectedUMLElement();
			if (v==null) {utils.log("Please select a UML element!"); return null;} else {}
//			utils.log("Type="+v.getType());
//			utils.log("ModelElt="+v.getElement());
//			utils.log("ModelElt="+v.getElement());
//			a=(Association)v.getElement();
			EList views=v.getDiagram().getEdges();
			View v2=(View)views.get(0);
//			dumpView(v2,"");
			View v3=udh.getChildView(v2,"ToMultiplicityLabel");
			dump(v3,"");
			v3.setVisible(false);
			views=v2.getChildren();
			for (Iterator iter = views.iterator(); iter.hasNext();) {
				v3 = (View) iter.next();
				utils.log(v3);
			}
			EList vs=((View)views.get(2)).getChildren();
			utils.log("Children="+vs.size());
			View v4=(View)vs.get(0);
			utils.log(v4);
//			View v5=(View)vs.get(1);
//			utils.log(v5);
			EList vs2=v4.getChildren();
			utils.log("Children="+vs2.size());
			EList styles=v4.getStyles();
			for (Iterator iter = styles.iterator(); iter.hasNext();) {
				Style style = (Style) iter.next();
				utils.log(style);
			}
			Node n5=(Node)v4;
			EList anns=n5.getEAnnotations();
			for (Iterator iter = anns.iterator(); iter.hasNext();) {
				EAnnotation ann = (EAnnotation) iter.next();
				utils.log(ann);
			}
			utils.log("Elt="+n5.getElement());
			n5.setVisible(true);
//			EList mes=a.getMemberEnds();
//			for (Iterator iter = mes.iterator(); iter.hasNext();) {
//				Property end = (Property) iter.next();
//				utils.log(end.getOpposite());
//			}
			return null;
//			Edge edge=udh.createEdge(v1,v2,"arrow");
//			Edge edge=udh.createEdge(v1,v2,"arrow");
			
//			utils.log("Edge="+edge);
		}
	}
	
	public class PlayScript2 implements Executable {
		public Object execute(Object o) {
			EObject eo=utils.getSelectedUMLElement();
			if (eo==null||!(eo instanceof View)) {utils.log("Please select a diagram");return null;}
			View v=(View)eo;
			Diagram d=((View)eo).getDiagram();
			Resource res = d.eResource();
			ResourceSet rset = res.getResourceSet();
			URI vuri = EcoreUtil.getURI(v);
			utils.log("VURI="+vuri);
//			utils.log(res);
//			utils.log(rset);
			String id=utils.eoh.getProxyID(v);
			utils.log(id);
			String name=utils.eoh.getQualifiedName(v, false);
			utils.log(name);
			URI uri=URI.createURI("platform:/resource/InputModels/TypedEJB.emx#"+id); // so can't do hierarchical!
			eo=rset.getEObject(uri,true);
//			if (eo!=null) {utils.log(((Node)eo).getElement());}
			if (eo!=null) {utils.log(eo);}
//			URIConverter uric = rset.getURIConverter();
//			Map urim = uric.getURIMap();
//			utils.dump(urim.keySet());
			
			return null;
		}
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.ibm.issw.rsa.interfaces.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		run();
		return null;
	}	
	/**
	 * @return Returns the allowProfileUpdate.
	 */
	public boolean isAllowProfileUpdate() {
		return allowProfileUpdate;
	}
	/**
	 * @param allowProfileUpdate The allowProfileUpdate to set.
	 */
	public void setAllowProfileUpdate(boolean allowProfileUpdate) {
		this.allowProfileUpdate = allowProfileUpdate;
	}
	
	public void dump(View v) {
		dump(v,"");
	}
	public void dump(View v, String indent) {
		String elt="";String invisible="";String styleString="";
		EList styles=v.getStyles();
		if (styles.size()>0) {styleString=" (Styles="+styles.size()+")";}
		if (!v.isVisible()) {invisible=" (invisible)";}
		EObject eo=v.getElement();
		if ( (eo!=null) && (eo instanceof NamedElement) ) {elt=((NamedElement)eo).getName();}
		utils.log(indent+v.getType()+":"+elt+invisible+styleString);
		EList vs=v.getChildren();
		for (Iterator iter = vs.iterator(); iter.hasNext();) {
			View vc = (View) iter.next();
			dump(vc,indent+"  ");
		}
	}
	
	public void showMultiplicity(View v) {
		setMultiplicity(v,true);
	}
	public void hideMultiplicity(View v) {
		setMultiplicity(v,false);
	}
	public void setMultiplicity(View v, boolean visible) {
		if (!(v.getElement() instanceof Association)) {return;}
		View child;
		child=udh.getChildView(v,"ToMultiplicityLabel");
		child.setVisible(visible);
		child=udh.getChildView(v,"FromMultiplicityLabel");
		child.setVisible(visible);
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
