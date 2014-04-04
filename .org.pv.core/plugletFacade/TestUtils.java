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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.pv.core.Utils;

 


public class TestUtils    {
	Utils utils=Utils.getSingleton();
	boolean NOOVERWRITE=true;
	
	public void plugletmain(String[] args) {
		String s="GenProfile/test.dat";
		String base="C:/P4ebESBMediations/Resources/"; // must be outside target workspace
//		try {
//			utils.genProject("fred"+"HLEAR", base+"HandlerListEAR", NOOVERWRITE);
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
////			utils.dumpCoreException(e);
//		}
		
//		String[] filenames=utils.root.toFile().list();
		String[] filenames=new String[] {"Test","fred"};
		for (int i = 0; i < filenames.length; i++) {
			String fname=filenames[i];
			if (!fname.startsWith(".")) {
				utils.p("Importing: "+fname);
				try {
					utils.importWorkspaceProject(fname);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					utils.dump(e);
				}
			}
		}
		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}