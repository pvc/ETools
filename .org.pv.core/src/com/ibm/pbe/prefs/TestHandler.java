package com.ibm.pbe.prefs;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.NewExampleAction;
import org.pv.core.StopWatch;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.pbe.patterns.PatternApplicator;
import com.ibm.pbe.prefs.IPBEFieldValidator;
import com.ibm.pbe.prefs.IPBEModelHandler;
import com.ibm.pbe.prefs.IPBEModelHandler2;
import com.ibm.pbe.prefs.PBEDialog;

// Generate mediation from wsdl
public class TestHandler implements  IPBEFieldValidator,IPBEModelHandler2 {
	private static final String SUBFLOWMSG = "Please use the mouse to select a valid error handler subflow";
	private static final String WSDLMSG = "Please use the mouse to select the WSDL interface";
	private IWorkbenchWindow window;
	Utils utils=Utils.getSingleton();
	private ISelection sel;
	private int minSegs=5;
	IPath root=new Path("C:/MyWorkspaces/MyToolkits/Setup/");
	static String MSET="com.ibm.etools.msg.validation.msetnature";
	private String patternId="com.ibm.pbe.patterns.CBR.jet";
	final PatternApplicator runner=new PatternApplicator();
	
	

	public int run(Element contextRoot) {
		utils.log("Hello World");
		return 0;
	}
	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.sel=selection;
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public String doField(FieldEditor fe) {
		String msg=null;boolean valid=true;
		String name=fe.getPreferenceName();
		fe.store();
		String value=fe.getPreferenceStore().getString(name);
		IResource selection=null;
		Path path = new Path(value);
		if (path.segmentCount()>1) {selection=utils.getFile(path);}
		if (!selection.exists()) {selection=null;}
		if ("wsdl".equals(name)) {
			try {
				if ( (selection==null) || !(selection instanceof IFile) || !("wsdl".equals(((IFile)selection).getFileExtension()))  ) {valid=false;}
			} catch (Exception e) {e.printStackTrace(utils.getLogger());valid=false;}
			if (!valid) {msg=WSDLMSG;}
		}
		if ("errorHandlerResource".equals(name)) {
				if ( (selection==null) || !(selection instanceof IFile) || !("msgflow".equals(((IFile)selection).getFileExtension())) ) {valid=false;}
			if (!valid) {msg=SUBFLOWMSG;}
		}
		return msg;
	}

	public String doSelect(FieldEditor fe, Object selection) {
		String msg=null;boolean valid=true;
		String name=fe.getPreferenceName();
		if ("wsdl".equals(name)) {
			try {
				if ( !(selection instanceof IFile) || !("wsdl".equals(((IFile)selection).getFileExtension())) ) {valid=false;}
			} catch (Exception e) {e.printStackTrace(utils.getLogger());valid=false;}
			if (!valid) {msg=WSDLMSG;}
		}
		if ("errorHandlerResource".equals(name)) {
//			utils.log("Value is "+selection);
				if ( !(selection instanceof IFile) || !("msgflow".equals(((IFile)selection).getFileExtension())) ) {valid=false;}
			if (!valid) {msg=SUBFLOWMSG;}
		}
		return msg;
	}

}
