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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.emf.core.ResourceSetModifyOperation;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Extension;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.pv.core.Utils;

import com.ibm.xtools.modeler.ui.UMLModeler;
/*
 * Created on 12-Dec-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CreateInternalProfile extends ResourceSetModifyOperation {
	
	Utils utils=Utils.getSingleton();
	Model m;
	/**
	 * @param arg0
	 */
	public CreateInternalProfile(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void execute(IProgressMonitor arg0)
			throws InvocationTargetException, InterruptedException {
		try {
		utils.log("Entering CreateInternalProfile");
		try {
			m = UMLModeler.openModel(utils.string2URI("GenProfile/TestModel.emx"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			utils.log(e);
			m = UMLModeler.createModel(utils.string2URI("GenProfile/TestModel.emx"));
		}
		
		Profile p=(Profile)m.createPackagedElement("InternalProfile", UMLPackage.eINSTANCE.getProfile());
//		p.setName("InternalProfile");
		Stereotype s = (Stereotype)p.createPackagedElement("MyStereoZZ",UMLPackage.eINSTANCE.getStereotype()); //true=Abstarct
//        s.setName("MyStereoZZ");
        utils.log("Setting extension");
        Extension extension = s.createExtension((Class) UMLPackage.eINSTANCE.getClass_(), false); 
		
		utils.log("Saving");
		try {
			UMLModeler.saveModel(m);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			utils.log(e1);
		}
		
		
		}catch (Exception e) {utils.log(e+", CAUSE: "+e.getCause());}
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
