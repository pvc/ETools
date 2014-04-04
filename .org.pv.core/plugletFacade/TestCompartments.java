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
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.issw.UMLDiagrams.CompartmentControl;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.pv.core.Utils;
 
import com.ibm.xtools.uml.ui.diagram.IUMLDiagramHelper;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestCompartments   {
	Utils utils=Utils.getSingleton();
	
	Model m;
	
	public void plugletmain(String[] args) {
		URI testModelURI= utils.string2URI("GenProfile/TestMetamodel.emx");
		try {
			m= UMLModeler.openModel(testModelURI);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			utils.log(e);
			m= UMLModeler.createModel(testModelURI);
		}
		IUMLDiagramHelper dh = UMLModeler.getUMLDiagramHelper();
		Diagram d=dh.getMainDiagram(m);
		Node n=(Node)d.getChildren().get(0);
		CompartmentControl cc=new CompartmentControl();
		TxWrapper w=new TxWrapper(cc);
		w.execute(n);
		dh.openDiagramEditor(d);
//		Node n=d.getFirstChild().getFirstChild();
		utils.log("Done!");
		
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
