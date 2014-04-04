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
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.issw.transforms.Diagram2ProfileApplier;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.pv.core.Utils;

public class GenProfile2  {
	Utils utils=Utils.getSingleton();
	Model m=null;
	Profile p;
	URI profileURI;
	Diagram d=null;
	
	public GenProfile2(String profilePath) {
		profileURI=utils.string2URI(profilePath);
	}
	public GenProfile2() { // take all defaults
	}
	
	
	
	public Profile execute(Diagram d) {
		this.d=d;
		build();
		return p;
	}
	
	public void build() {
		TxWrapper d2p=new TxWrapper(new Diagram2ProfileApplier(profileURI));
		p=(Profile)d2p.execute(d);
//		utils.rename(p,"DomainLanguage");
		
//		utils.log("Entering apply, m= "+m);
//		//				Model m=(Model)utils.load(URI.createPlatformResourceURI("TestModel/TestModel.emx")); MUSt use ibm itils to load profiles
//		URI testModelURI= utils.getFolder(m).appendSegment("Sample"+m.getName()).appendFileExtension("emx");
//		utils.log(testModelURI);
//		Model testModel=null;
//		testModel=utils.getModel(testModelURI);
//		utils.log("Applied version: "+testModel.getAppliedVersion(p));
//		utils.log("Trying apply of "+p);
//		try {testModel.applyProfile(p);} catch (Exception e) {utils.log("Apply problem: "+e);}
//		utils.log("Done!");
//		
//		utils.log("Applied version now: "+testModel.getAppliedVersion(p));
//		//				utils.save(m,m.eResource().getURI());
//		//				utils.log("Saved2: "+m);
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
