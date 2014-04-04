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
import java.util.Date;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.impl.EcoreFactoryImpl;
import org.pv.core.Utils;

import com.ibm.pbe.rsa.interfaces.Executable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AnnotateOp implements Executable {
	
	Utils utils=Utils.getSingleton();
	String uri="ibm.p4eb.profile";
	/* (non-Javadoc)
	 * @see com.ibm.issw.rsa.interfaces.Executable#execute(java.lang.Object)
	 */
	public Object execute(Object o) {
		EModelElement em=(EModelElement)o;
		EAnnotation a = em.getEAnnotation(uri);
		if (a==null) {
		
		a=EcoreFactoryImpl.eINSTANCE.createEAnnotation();
		a.setSource(uri);
		EMap map=a.getDetails();
		map.put("ns",new Date());
		em.getEAnnotations().add(a);
		return "Annotation added to "+em;
		} else return a.getDetails().get("ns"); 
			
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
