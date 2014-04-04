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
package com.ibm.p4eb.sdl;

class Copyright
{
	static final String PV_COPYRIGHT =
    	"\n\nLicensed Materials - Property of IBM\n" +
	    "com.ibm.sal.rapidsat\n" +
	    "(C) Copyright IBM Corp. 2007. All Rights Reserved.\n" +
    	"US Government Users Restricted Rights - Use, duplication or\n" +
    	"disclosure restricted by GSA ADP Schedule Contract with IBM Corp.\n\n";
	static final String PV_COPYRIGHT_SHORT = 
		"\n\n(C) Copyright IBM Corp. 2007.\n\n";

	static String copyright() { return Copyright.PV_COPYRIGHT; }  
}