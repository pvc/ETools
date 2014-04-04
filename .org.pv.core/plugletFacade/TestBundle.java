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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.jar.JarInputStream;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Profile;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.pv.core.Utils;

import com.ibm.issw.actionImpl.GenProfile;
import com.ibm.xtools.modeler.ui.UMLModeler;
 


public class TestBundle    {
	
	Utils utils=Utils.getSingleton();
	GenProfile gp=new  GenProfile();
	
	
	public void plugletmain(String[] args) {
		utils.log("****************************");
		Bundle b=Platform.getBundle("com.ibm.p4eb.Profiles");
		String loc=b.getLocation();
		utils.log("Location: "+loc);
		
		utils.log("State: " +b.getState());
		utils.log("SymbolicName: " +b.getSymbolicName());
		utils.log("ID: " +b.getBundleId());
		URL url=b.getEntry("temp/ESBPIM.epx"); //must include path, leading slash is optional
		URL url2=Platform.find(b,new Path("temp/ESBPIM.epx")); //ditto
		URL url3=null;
			try {
				url3=Platform.asLocalURL(url);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace(utils.getLogger());
			}
		utils.log("URL: "+url);
		utils.log("URL2: "+url2);
		utils.log("URL3: "+url3);
		URI uri=URI.createURI(url2.toString());
		utils.log("URI: "+uri);
		try {
			Profile p=UMLModeler.openProfile(uri);
			utils.log("Profile opened: " +p.getName());
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace(utils.getLogger());
		}
		
		Enumeration l=b.getEntryPaths("/");  //lists files in bundle, path is rel to bundle root
		for (; l.hasMoreElements();) {
			String path = (String) l.nextElement();
			utils.log(path);
		}
		Dictionary d = b.getHeaders();
		Enumeration keys=d.keys();
		for (; keys.hasMoreElements();) {
			Object key = (String) keys.nextElement();
			utils.log(key+":"+d.get(key));
		}
		
		String loc2="C:/MyWorkspaces/P4ebMDALabPlugins/eclipse/plugins/com.ibm.p4eb.Profiles_1.0.2.jar";
		java.io.FileInputStream f=null;
		JarInputStream fj=null;
		try {
			f=new java.io.FileInputStream(loc2);
			fj=new JarInputStream(f);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(utils.getLogger());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
		try {
			b.stop();
			utils.log("State: " +b.getState());
			b.start();
			utils.log("State: " +b.getState());
//			b.update(fj); //doesn't work
//			utils.log("State: " +b.getState());
		} catch (BundleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
		
		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}