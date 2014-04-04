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
import org.eclipse.emf.ecore.EObject;
import org.pv.core.Utils;

import com.ibm.issw.MSL.TxWrapper;
 


public class TestAnnotation    {
	
	Utils utils=Utils.getSingleton();
//	Diagram2DOMTransform ad=new Diagram2DOMTransform();
//	GenMetamodel actionImpl=new GenMetamodel();
	AnnotateOp op=new AnnotateOp();
	
	public void plugletmain(String[] args) {
		utils.log("Starting run *************");
		EObject eo=utils.getSelectedUMLElement();
		if (eo==null) {utils.log("Please select a UML element");return;}
		TxWrapper t = new TxWrapper(op);
		String s=(String)t.execute(eo);
		utils.log(s);
		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
} 