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
import org.pv.core.Utils;

 


public class TestPaths    {
	Utils utils=Utils.getSingleton();
	
	public void plugletmain(String[] args) {
//		IPath p=utils.toPath("GenProfile");
//		File f=p.toFile();
//		out.println(f.getAbsolutePath());
		
		URI uri=utils.string2URI("Temp withblank");
		utils.log(uri);
		utils.log(uri.path());
		utils.log(uri.toFileString()); //null for folders
		utils.log(URI.decode(uri.path()));
		
		
		
//		utils.log(uri.device()); //null
//		utils.log(uri.scheme()); //platform
//		utils.log(uri.authority()); //null
//		utils.log(uri.devicePath()); // resource/proj/path/fn.ext
//		utils.log(uri.path()); //ditto
//		utils.log(uri.opaquePart()); //null
//		utils.log(uri.trimSegments(1)); //path inc resource segment
//		utils.log(uri.segment(1)); //project name
//		
//		IPath p=(new Path(uri.path())).removeFirstSegments(1); //removes "resource"
//		utils.log("Path: "+p);
//		
//		URI fileURI = URI.createFileURI(new File("test.ext").getAbsolutePath()); // in SDP root
//		utils.log(fileURI);
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}