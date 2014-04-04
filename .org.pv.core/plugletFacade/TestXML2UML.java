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
import org.w3c.dom.Document;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.issw.transforms.List2PackageApplier;
import com.ibm.p4eb.UML.UML2XML;
import com.ibm.p4eb.UML.UMLChildSupplier;
import com.ibm.p4eb.UML.XML2UMLApplier;
import com.ibm.pbe.rsa.interfaces.Executable;
 

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestXML2UML   {
	Utils utils=Utils.getSingleton();
	
	
	public void plugletmain(String[] args) {
		XML2UMLApplier action=new XML2UMLApplier();
		Document doc=utils.getDoc(utils.getFile("DoDESB/ESBTestClient.xml"));
		TxWrapper t=new TxWrapper(action);
		t.execute(doc);

	}	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
