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
import java.io.File;
import java.util.Iterator;

import org.pv.core.Utils;
import org.w3c.dom.Document;

import com.ibm.issw.MSL.TxWrapper;
import com.ibm.p4eb.UML.XML2UMLApplier;
import com.ibm.pbe.trees.TreeNodeFileAdapter;
 

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestFileTree   {
	Utils utils=Utils.getSingleton();
	
	
	public void plugletmain(String[] args) {
		utils.p("***********************");
		File f=new File("D:/AWBTutorial");
		TreeNodeFileAdapter tn=new TreeNodeFileAdapter(f);
		for (Iterator iter = tn.iterator(); iter.hasNext();) {
			TreeNodeFileAdapter tn2= (TreeNodeFileAdapter) iter.next();
			utils.p(((File)tn2.getData()).getAbsolutePath());
		}

	}	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
