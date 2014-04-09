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
package com.ibm.pbe.patterns;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IEditorLauncher;
import org.pv.core.Utils;


public class SystemEditorLauncher implements IEditorLauncher {
	final static Utils utils=Utils.getSingleton();
	//  Method to allow invocatiion for test purposes	
	//	Use Ctl-Alt-R to run this method, Ctl-Alt-M to add the Action to PVTools menu, Ctl-ALT-= to rerun last action in PVTools menu
	public void run() {
		Program prog = Program.findProgram("doc");
		utils.log("Found: "+prog);
		if (true) {return;}
		utils.log(utils.getActiveFile().getLocation());
		open(utils.getActiveFile().getLocation());
	}

	public void open(IPath path) {
		
		final String sPath=path.toOSString();
		utils.log("Path="+sPath);
		boolean result = Program.launch(sPath);
		utils.log("Result: "+result);
//		final IFile file=utils.getFile(path);
//		try {
//			utils.getActivePage().openEditor(new FileEditorInput(file),IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
//		} catch (PartInitException e) {e.printStackTrace(utils.getLogger());}
//		BusyIndicator.showWhile(utils.wb.getDisplay(), new Runnable() {
//			public void run() {
//				utils.log(sPath);
//				utils.log("Launching ...");
//				boolean result = Program.launch(sPath);
//				utils.log("Result: "+result);
//			}
//		});

	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
	
}
