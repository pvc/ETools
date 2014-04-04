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
import org.eclipse.core.resources.IFile;
import org.pv.core.Utils;

 


public class TestEdit    {
	
	Utils utils=Utils.getSingleton();
	
	
	public void plugletmain(String[] args) {
		IFile f=utils.getFile("/GenProfile/src/com/ibm/p4eb/genProfile/GenProfilePlugin.java");
		utils.edit(f);
		utils.log("****************************");
		
	}
//com.ibm.sse.editor.xml.StructuredTextEditorXML
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}