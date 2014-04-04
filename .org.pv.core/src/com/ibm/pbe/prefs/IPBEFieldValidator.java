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
package com.ibm.pbe.prefs;

import org.eclipse.jface.preference.FieldEditor;

public interface IPBEFieldValidator {
	public String doField(FieldEditor fe);

	public String doSelect(FieldEditor fe, Object selection);

}
