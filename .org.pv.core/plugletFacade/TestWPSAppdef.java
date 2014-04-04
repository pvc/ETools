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
import org.pv.core.Utils;

import com.ibm.p4eb.WPSTest.GenWPSExportTest;
 


public class TestWPSAppdef    {
	
	Utils utils=Utils.getSingleton();
//	Diagram2DOMTransform ad=new Diagram2DOMTransform();
//	GenMetamodel actionImpl=new GenMetamodel();
	GenWPSExportTest actionImpl=new GenWPSExportTest();
	
	public void plugletmain(String[] args) {
		utils.log("Starting run *************");
		actionImpl.execute(null);
		if (true) {return;}
//		
		
		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
} 