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
 


public class TestIntegers  {
	
	Utils utils=Utils.getSingleton();
	
	
	
	public void plugletmain(String[] args) {
		utils.log("Result"+6/2);
		utils.log("Result"+(4+7/2));
		
		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}