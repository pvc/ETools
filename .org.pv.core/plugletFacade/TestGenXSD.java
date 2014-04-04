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
/*
 * Created on 27-Oct-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import org.pv.core.Utils;
import org.w3c.dom.Document;

import com.ibm.issw.actionImpl.GenXSD;

import org.eclipse.gmf.runtime.notation.Diagram;
 

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestGenXSD   {
	Utils utils=Utils.getSingleton();
	
	
	public void plugletmain(String[] args) {
		GenXSD action=new GenXSD();
		action.execute();
	}	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
