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
 * Created on 11-Jan-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.issw.actionImpl;

import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.issw.transforms.UML2DOMElementTransform;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenAppdef2Old {
	Utils utils=Utils.getSingleton();

	/* (non-Javadoc)
	 * @see com.ibm.issw.rsa.interfaces.Executable#execute(java.lang.Object)
	 */
	public void execute() {
//		utils.log("**************Starting GenAppdef2");
		EObject eo=utils.getSelectedUMLElement();
		Diagram d;
		if (eo instanceof View) {d=((View)eo).getDiagram();} else {utils.log("Selection must be a diagram - please retry");return;}
		String dName=d.getName();
		String pkg=((NamedElement)d.eContainer().eContainer()).getName();
		
		Document doc=null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
		if (doc==null) {return;}
		Element app=doc.createElement("cell");
		doc.appendChild(app);
		
		app.setAttribute("container",pkg);
		app.setAttribute("viewName",dName);
		
		EList edges = d.getEdges();
		for (Iterator iter = edges.iterator(); iter.hasNext();) {
			Edge ed = (Edge) iter.next();
			EObject o = ed.getElement();
			if (o instanceof org.eclipse.uml2.uml.Association) {
				Association a=(Association)o;
				EList sts = a.getAppliedStereotypes();
				for (Iterator iterator = sts.iterator(); iterator.hasNext();) {
					Stereotype st = (Stereotype) iterator.next();
					utils.log("Stereo: "+st);
					
				}
			}
			
		}
		
		
		EList views=d.getChildren();
		UML2DOMElementTransform t=new UML2DOMElementTransform(doc);
		for (Iterator iter = views.iterator(); iter.hasNext();) {
			View v = (View) iter.next();
			EObject o=v.getElement();
			utils.log("***Processing: "+o);
			Element e=null;
			if (o instanceof org.eclipse.uml2.uml.Element) {
			e=(Element) t.execute((Element)o);
			}
			if (o instanceof org.eclipse.uml2.uml.Classifier) {
				utils.log("****Processing as classifier");
				org.eclipse.uml2.uml.Classifier c=(org.eclipse.uml2.uml.Classifier)o;
				if (e==null) {e=doc.createElement(c.eClass().getName());}
				
				e.setAttribute("name",c.getName());
				String npkg = c.getNearestPackage().getName();
				if (!npkg.equals(pkg)) {e.setAttribute("package",npkg);}
				EList atts=c.getAttributes();
				for (Iterator iterator = atts.iterator(); iterator.hasNext();) {
					Property att = (Property) iterator.next();
					Element eAtt;
					eAtt=(Element)t.execute(att);
					if (eAtt==null) {eAtt=doc.createElement("attribute");}
					Association assoc = att.getAssociation();
					if (assoc!=null) {
						eAtt.setAttribute("ref","true");
						String assocName=assoc.getName();
						if (assocName.length()>0) {eAtt.setAttribute("refName",assoc.getName());}
						}
					eAtt.setAttribute("name",att.getName());
					String typeName="String";
					Type type=att.getType();
					if (type!=null) {typeName=type.getName();}
//					eAtt.setAttribute("type",typeName);
					String def=att.getDefault();
					if (def.length()>0) {eAtt.setAttribute("default",def);}
					eAtt.setAttribute("att","true");
					e.appendChild(eAtt);
					
					
					
//					if (def!=null) {eAtt.setAttribute("default",def);}
//					eAtt.setAttribute("length","250");
//					eAtt.setAttribute("variable","true");
//					EList kw=att.getKeywords();
////					utils.log("KWords for "+att.getName()+kw);
//					if (!kw.isEmpty()) {e.setAttribute("key",att.getName());} // BAD - must dd an iterator to check values of kws!!
				}
				
				EList ops=null;
				if (c instanceof Class) { ops=((Class)c).getOwnedOperations();}
				if (c instanceof Interface) { ops=((Interface)c).getOwnedOperations();}
				if (ops!=null) {
//					utils.log("************Behaviors:");
					for (Iterator iterator= ops.iterator(); iterator.hasNext();) {
						Operation op= (Operation) iterator.next();
						utils.log(op);
					Element eOp;
					eOp=(Element)t.execute(op);
					if (eOp==null) {eOp=doc.createElement("operation");}
					eOp.setAttribute("name",op.getName());
					EList parms = op.getOwnedParameters();
					for (Iterator iterator2 = parms.iterator(); iterator2.hasNext();) {
						Parameter parm = (Parameter) iterator2.next();
						Element eParm=doc.createElement("parameter");
						eParm.setAttribute("name",parm.getName());
						Type t2=parm.getType();
						if (t2!=null) {eParm.setAttribute("type",parm.getType().getName());}
						eParm.setAttribute("direction",parm.getDirection().getName());
//						eParm.setAttribute("default",parm.getDefault());
						eOp.appendChild(eParm);
					}
					
					e.appendChild(eOp);
					}
//				}
				}
				
				
				
			}
			if (e!=null) {app.appendChild(e);}
		}
		
		
		
		IFile fout=null;
		if (fout==null) {
			IPath xmlPath=utils.getPath(d).removeLastSegments(1).append(d.getName()+".appdef");
			fout=utils.getFile(xmlPath);
		}
		
		utils.save(doc,fout);
		return;
		

	}
	
	

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
