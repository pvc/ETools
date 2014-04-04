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
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
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
public class DeleteProfiles extends ResourceSetModifyOperation {
	
	Utils utils=Utils.getSingleton();
	Model m;
	/**
	 * @param arg0
	 */
	public DeleteProfiles(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void execute(IProgressMonitor arg0)
			throws InvocationTargetException, InterruptedException {
		utils.log("Entering DeleteProfiles");
		try {
			m = UMLModeler.openModel(utils.string2URI("GenProfile/TestModel.emx"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			utils.log(e);
			m = UMLModeler.createModel(utils.string2URI("GenProfile/TestModel.emx"));
		}
		utils.log("getting profiles");
		Object[] pas= m.getProfileApplications().toArray();
		for (int i = 0; i < pas.length; i++) {
// TODO: UML Migration. Unmapped Classifier org.eclipse.uml2.uml.ProfileApplication::ProfileApplication. Refer to migration guide.
			// TODO: UML Migration. Unmapped Classifier org.eclipse.uml2.uml.ProfileApplication::ProfileApplication. Refer to migration guide.
			//		for (Iterator iter = profiles.iterator(); iter.hasNext();) {
			ProfileApplication pa = (ProfileApplication) pas[i];
			// TODO: UML Migration.  Unmapped source property ProfileApplication::getImportedProfile. Refer to the migration guide.
			// TODO: UML Migration.  Unmapped source property ProfileApplication::getImportedProfile. Refer to the migration guide.
			// TODO: UML Migration.  Unmapped source property ProfileApplication::getImportedProfile. Refer to the migration guide.
			Profile p=pa.getAppliedProfile();
			utils.log("Unapplying: "+p.getName());
			m.unapplyProfile(p);
		}
		utils.log("Saving");
		try {
			UMLModeler.saveModel(m);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			utils.log(e1);
		}
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
