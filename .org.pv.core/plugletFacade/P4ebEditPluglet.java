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

import com.ibm.issw.UMLDiagrams.P4ebEdit;
 


public class P4ebEditPluglet    {
	P4ebEdit ad=new P4ebEdit();
	
	public void plugletmain(String[] args) {
		ad.execute();
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}