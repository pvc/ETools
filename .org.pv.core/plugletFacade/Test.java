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

import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.pbe.text.HexDump;
 

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test   {
	Utils utils=Utils.getSingleton();
//	String className="com.ibm.p4eb.SVG.SVGTransform";
//	String className="com.ibm.p4eb.WPSTest.GenWPSTestAppdef";
//	String className="com.ibm.p4eb.WPSTest.GenWPSMockObjectAppdef";
//	String className="com.ibm.pbe.patterns.CreatePattern";
//	String className="com.ibm.pbe.core.P4ebEdit";
//	String className="com.ibm.p4eb.wsdl.GenWPSProcessTestAppdef";
//	String className="com.ibm.issw.actionImpl.RunDPTK";
	String className="com.ibm.issw.actionImpl.ImportAppdef";
//	String className="com.ibm.issw.transforms.DOM2ModelApplier";
//	String className="com.ibm.issw.actionImpl.GenWasScript";
	
	public void plugletmain(String[] args) {
		utils.log("*****************************Starting run of: "+className);
		Executable action=null;
		try {
			action = (Executable)Class.forName(className).newInstance();
		} catch (Exception e) {
			e.printStackTrace(utils.getLogger());
		}
		if (action==null) {utils.log("Could not load "+className);return;}
		action.execute(null);
		utils.log("*****************************End of run of: "+className);
		utils.log("");

	}	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
