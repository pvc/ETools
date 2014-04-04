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


import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.pv.core.Utils;
import org.w3c.dom.Document;

import com.ibm.pbe.text.XMLResolver;
 

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestXMLResolver   {
	Utils utils=Utils.getSingleton();
	
	
	public void plugletmain(String[] args) {
		XMLResolver action=new XMLResolver();
		URI uri=null;
//		uri = URI.createFileURI("/LoanApplicationModule/xsd-includes/http.LoanApplicationModule.com.mybank.loan.xsd");
//		IFile f = utils.getFile("/LoanApplicationModule/xsd-includes/http.LoanApplicationModule.com.mybank.loan.xsd");
//		IFile f = utils.getFile("/LoanApplicationModule/com/mybank/loan/mainProcessInterface.wsdl");
		IFile f = utils.getFile("/L_MailService/I_Postcode.wsdl");
		Document d = action.execute(f);
		utils.dump(d);
	}	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
