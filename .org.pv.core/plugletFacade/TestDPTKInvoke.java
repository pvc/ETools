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


import java.util.List;

import org.pv.core.Utils;
import org.w3c.dom.Document;

import com.ibm.dptk.patternWizard.PatternApplicationStatus;
import com.ibm.issw.transforms.Diagram2DOMTransformOld;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
 

public class TestDPTKInvoke   {
	Utils utils=Utils.getSingleton();

	public void plugletmain(String[] args) {
		final List elements = UMLModeler.getUMLUIHelper().getSelectedElements();
		if (elements.isEmpty()) {
			utils.log("Please select a Diagram View");
			return;
		}
		
		View v=(View)elements.get(0);
		Diagram d=v.getDiagram();
		Diagram2DOMTransformOld ad=new Diagram2DOMTransformOld();
		Document doc=ad.execute(d);

		
		String patternId="ldif.pattern";
//		IFile f=getFile(new Path("GenProfile/SampleModel4.xml"));
//		utils.log(f);
		PatternApplicationStatus status;
		status=utils.execDPTK(doc,patternId);
		
        if (!status.isSuccessful()) {
        	utils.log("DPTK execution not successful:");
        	utils.log(status.getStatus());
        }
	}
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
