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

import org.pv.core.Utils;

import com.ibm.pbe.trees.FileTreeDumper;
 


public class TestTreeDump    {
	
	Utils utils=Utils.getSingleton();

	
	public void plugletmain(String[] args) {
		FileTreeDumper action=new FileTreeDumper();
		utils.log("*************Starting run of "+action.getClass().getName()+ "at: "+new Date());
		action.setSourceDir("C:/SDLC/SDLC4ESB/LocalArtifacts");
		action.execute();
		utils.log("************Ended run of "+action.getClass().getName()+ "at: "+new Date());
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
} 