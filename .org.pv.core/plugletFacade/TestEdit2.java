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
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.pv.core.Utils;

 



public class TestEdit2   {
	Utils utils=Utils.getSingleton();

	public void plugletmain(String[] args) {
		String fileName=utils.root+"/MediationHandlers/src/mediationHandlers/Order2SupplierBHandler01.java";
		IFile f=utils.wsr.getFileForLocation(new Path(fileName));
//		IFile f=wsr.getFileForLocation(new Path(configPath+"Reuters/AuditLogDest.cfg"));
		if (!f.exists()) {utils.p("File does not exist - please create: "+f); return;}
//		String editorId="org.eclipse.ui.DefaultTextEditor";
		IEditorInput input=new FileEditorInput(f);
//		utils.p(input.getName());
//		input.getPersistable();
		
		IEditorRegistry er = PlatformUI.getWorkbench().getEditorRegistry();
		IEditorDescriptor edd = er.getDefaultEditor(fileName);
		String edid = edd.getId();
		IWorkbenchPage p=utils.getActivePage();
		try {
			p.openEditor(input, edid);
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			utils.p("Error opening editor: "+e1);
			e1.printStackTrace(utils.getLogger());
		}
		utils.p("Editor opened");
	}


	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
