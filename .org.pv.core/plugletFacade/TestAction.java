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

import com.ibm.issw.actionImpl.ImportXML;
 


public class TestAction    {
	
	Utils utils=Utils.getSingleton();

	
	public void plugletmain(String[] args) {
		ImportXML action=new ImportXML();
		utils.log("*************Starting run of "+action.getClass().getName()+ "at: "+new Date());
		action.execute();
		utils.log("************Ended run of "+action.getClass().getName()+ "at: "+new Date());
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
} 