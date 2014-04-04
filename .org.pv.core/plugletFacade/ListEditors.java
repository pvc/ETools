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
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.pv.core.Utils;

 

public class ListEditors   {
//	public static String configPath="C:/MyWorkspaces/RSAGold/MediationHandlers/src/mediationHandlers/";
	public static String configPath="C:/MyWorkspaces/RSAGold/ESBHandlers/configs/";
	Utils utils=Utils.getSingleton();
	
	public void plugletmain(String[] args) {
		IEditorRegistry er = PlatformUI.getWorkbench().getEditorRegistry();
		IFileEditorMapping[] map = er.getFileEditorMappings();
		for (int i = 0; i < map.length; i++) {
			IFileEditorMapping mapping = map[i];
			utils.p(mapping.getExtension()+"::"+mapping.getName()+"::"+mapping.getLabel());
			IEditorDescriptor[] eds = mapping.getEditors();
			for (int j = 0; j < eds.length; j++) {
				IEditorDescriptor d = eds[j];
				utils.p("   "+d.getLabel()+"::"+d.getId());
				
			}
		}
	}


	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
