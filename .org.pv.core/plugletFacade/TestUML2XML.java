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
import java.util.HashSet;
import java.util.Set;

import org.eclipse.uml2.uml.UMLPackage;
import org.pv.core.Utils;

import com.ibm.p4eb.UML.UML2XML;
import com.ibm.p4eb.UML.UMLChildSupplier;
import com.ibm.pbe.rsa.interfaces.Executable;
 

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestUML2XML   {
	Utils utils=Utils.getSingleton();
	
	
	public void plugletmain(String[] args) {
		UML2XML action=new UML2XML();
		Set filter=new HashSet();
		filter.add(UMLPackage.eINSTANCE.getClass_());
		filter.add(UMLPackage.eINSTANCE.getProperty());
		UMLChildSupplier cs = new UMLChildSupplier();
//		cs.setFilter(filter);
		action.setUMLChildSupplier(cs);
		action.execute();
	}	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
