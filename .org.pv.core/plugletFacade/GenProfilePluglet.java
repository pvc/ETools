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

import com.ibm.issw.actionImpl.GenProfile;
 


public class GenProfilePluglet    {
	GenProfile ad=new GenProfile();
	
	public void plugletmain(String[] args) {
		ad.execute();
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}