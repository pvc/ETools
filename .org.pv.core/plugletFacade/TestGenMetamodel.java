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
import org.pv.core.Utils;

import com.ibm.issw.actionImpl.GenAppdefOld;
import com.ibm.issw.actionImpl.GenMetamodel;
 

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestGenMetamodel   {
	Utils utils=Utils.getSingleton();
	
	
	public void plugletmain(String[] args) {
		GenMetamodel actionImpl=new GenMetamodel();
		actionImpl.execute();
		
	}	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
