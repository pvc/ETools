package com.ibm.p4eb.UML;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.pv.core.Utils;

import com.ibm.xtools.modeler.ui.UMLModeler;
 


public class TestGetElementByPath    {
	
	Utils utils=Utils.getSingleton();
	
	public void plugletmain(String[] args) {
		String qn="Mediations::ESBTestClient::Class1::Class1";
		String[] segs=qn.split("::");
		utils.p("Segments:"+segs.length);
		Collection models=UMLModeler.getOpenedModels();
		boolean found=false;Model m=null;
		for (Iterator iter = models.iterator(); iter.hasNext();) {
			m = (Model) iter.next();
			if (m.getName().equals(segs[0])) {found=true;break;}
		}
		if (!found) {utils.p("Model not opened: "+segs[0]);}
		Element e=m;
		for (int i = 1; i < segs.length; i++) {
			utils.p("Locating seg:"+segs[i]);
			if (e instanceof org.eclipse.uml2.uml.Class) {
				e=((org.eclipse.uml2.uml.Class)e).getNestedClassifier(segs[i]);
			} else if (e instanceof Package) {
				e= ((Package)e).getOwnedMember(segs[i]);
			} else {e=null;}
			if (e==null) {break;}
		}
		if (e==null) {utils.p("Could not find element "+qn);return;}
		utils.p("Found:"+e);
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}