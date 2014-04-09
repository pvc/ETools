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
 * Created on 25-Jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.pv.core;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;














import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
//import org.eclipse.emf.common.util.URI;
//import org.eclipse.emf.ecore.EObject;
//import org.eclipse.emf.common.util.BasicEList;
//import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.common.util.URI;
//import org.eclipse.emf.common.util.WrappedException;
//import org.eclipse.emf.ecore.EAnnotation;
//import org.eclipse.emf.ecore.EObject;
//import org.eclipse.emf.ecore.resource.Resource;
//import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
//import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IContributorResourceAdapter;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;

//import org.eclipse.uml2.uml.Class;
//import org.eclipse.uml2.uml.Element;
//import org.eclipse.uml2.uml.Model;
//import org.eclipse.uml2.uml.NamedElement;
//import org.eclipse.uml2.uml.Namespace;
//import org.eclipse.uml2.uml.Package;
//import org.eclipse.uml2.uml.PackageableElement;
//import org.eclipse.uml2.uml.Profile;
//import org.eclipse.uml2.uml.Property;
//import org.eclipse.uml2.uml.UMLPackage;
import org.osgi.framework.Bundle;
import org.pv.plugin.Activator;
import org.w3c.dom.Attr;
//import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Text;
import org.w3c.dom.NodeList;













//import com.ibm.dptk.patternWizard.PatternApplicationStatus;
//import com.ibm.dptk.patternWizard.PatternApplicator;
//! UML
//import com.ibm.issw.MSL.TxWrapper;
//import com.ibm.issw.UMLDiagrams.DiagramInNamespaceCreator;
//import com.ibm.issw.rsa.interfaces.Executable;
//import com.ibm.issw.transforms.BasicDiagram2DOMTransform;
//import com.ibm.issw.transforms.Diagram2DOMTransform;
//import com.ibm.issw.transforms.Diagram2XSDTransform;
//import com.ibm.issw.transforms.Name2ElementApplier;
//import com.ibm.issw.transforms.Profile2ModelApplier;
//! UML
//import com.ibm.dptk.patternWizard.PatternApplicationStatus;
//import com.ibm.pbe.patterns.PatternApplicator;
import com.ibm.pbe.text.XMLResolver;
import com.ibm.pbe.transforms.XML2DOMTransform;
//import com.ibm.pbe.trees.FileFinder;

//import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
//import org.eclipse.gmf.runtime.notation.Diagram;
//import org.eclipse.gmf.runtime.notation.Node;
//import org.eclipse.gmf.runtime.notation.View;
//import com.ibm.xtools.uml.core.IUMLHelper;
//import com.ibm.xtools.uml.ui.diagram.IUMLDiagramHelper;
//import com.ibm.xtools.uml.ui.IUMLUIHelper;
//import com.ibm.xtools.umlnotation.UMLDiagramKind;
//import com.ibm.xtools.umlnotation.UMLListCompartmentStyle;
//import com.ibm.xtools.umlnotation.UmlnotationPackage;


/**
 * @author Paul Verschueren
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Utils {  
	private static final QualifiedName IMPORTED_PROJECT_FLAG = new QualifiedName("com.ibm.pbe.core","imported");
	
	public final static boolean NOOVERWRITE=false;
	public final static boolean OVERWRITE=true;
	final static int statusInfo=IStatus.INFO;
	long uNum=System.currentTimeMillis();
	
	MessageConsole c;
	MessageConsoleStream mcs; 
	PrintWriter logger;
	public final IWorkspace ws=	ResourcesPlugin.getWorkspace();
	public final IWorkspaceRoot wsr=ws.getRoot();
	public final IPath root=ResourcesPlugin.getWorkspace().getRoot().getRawLocation().addTrailingSeparator();
	
	public final IWorkbench wb;
	public final IPerspectiveRegistry pr;
//	 !UML	
//	public final IUMLDiagramHelper udh;
//	public final EMFCoreUtil eoh;
//	public final IUMLHelper umlh;
//	public final IUMLUIHelper uih;
//	 !UML
//	public static FileFinder fileFinder;
	
	private static Utils me;
//	private static PatternApplicator patRunner=new PatternApplicator();
	
	
	public final static String JAVA_PERSPECTIVE="org.eclipse.jdt.ui.JavaPerspective";
	public final static String DPTK_NATURE="com.ibm.dptk.patternNature";
	
	public final static String JAVA_CAPABILITY="org.eclipse.javaDevelopment";
	public final static String PLUGIN_DEVELOPMENT_CAPABILITY="org.eclipse.plugInDevelopment";
	public final static String UML_CAPABILITY="com.ibm.xtools.activities.umlmodelingActivity";
	public final static String UML_PROFILE_CAPABILITY="com.ibm.xtools.activities.umlprofilingActivity";
	public final static String J2EE_CAPABILITY="com.ibm.xtools.activities.umlprofilingActivity";
	public final static String XML_CAPABILITY="com.ibm.wtp.xml.core";
	public final static String WID_BASE_CAPABILITY="com.ibm.wbit.activity.bidevelopment.core";
	public final static String WID_FULL_CAPABILITY="com.ibm.wbit.activity.bidevelopment.full";
	
	Preferences globals;
	boolean log=true;
	
	static final ClassLoader myLoader=Utils.class.getClassLoader();
	static final boolean loadedFromWorkspace=(myLoader.getClass().getName().equals(Activator.LOADER));

	private static IDialogSettings settingsManager;
	private static Activator thisPlugin;
//	{if (thisPlugin!=null) {settingsManager=thisPlugin.getDialogSettings();} } // static initialisation code
		
	private Utils() {
		if (isHeadless()) {wb=null;pr=null;log=false;} 
		else {
			wb=PlatformUI.getWorkbench();
			pr=wb.getPerspectiveRegistry();
		} 
		try {getProject(".temp");} catch (CoreException e1) {e1.printStackTrace(getLogger());log("Warning - .temp project cannot be created automatically - please resolve and create an empty .temp project");}
//		p(b.getClass().getName());
// !UML
//		udh=UMLModeler.getUMLDiagramHelper();
//		eoh=new EMFCoreUtil();
//		umlh=UMLModeler.getUMLHelper();
//		uih=UMLModeler.getUMLUIHelper();
// !UML
	}
	public static void setActivator(Activator plugin) {
		thisPlugin=plugin;
		settingsManager=thisPlugin.getDialogSettings();
	}
	public Activator getActivator() {
		if (loadedFromWorkspace) {
		Bundle b = Platform.getBundle("org.pv.core");
		String clName=(String)b.getHeaders().get("Bundle-Activator");
//		log(clName);
		try {
			@SuppressWarnings("unchecked")
			Class<Activator> activator =(Class<Activator>) b.loadClass(clName);
			Method m = activator.getMethod("getDefault",new Class<?>[]{} );
			thisPlugin=(Activator)m.invoke(activator,new Object[]{});
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		if (thisPlugin==null) {p("Error: getActivator called before PV Activator instance available");}
		return thisPlugin;
		
	}

	public static Utils getSingleton() { 
		if (me==null) {me=new Utils();}
		if (loadedFromWorkspace) {me.p("Warning - running Workspace copy of Utils");}
		return me;
	}
	public IWorkbenchPage getActivePage() {
	return wb.getActiveWorkbenchWindow().getActivePage();
	}
	public Shell getShell() {
		return wb.getActiveWorkbenchWindow().getShell();
	}
	public IWorkbenchPart getActivePart() {
		return getActivePage().getActivePart();
	}
	public IDialogSettings getSettingsManager(String sectionName) {
		return DialogSettings.getOrCreateSection(settingsManager, sectionName);
	}
/*********************************************************/	
	public String getUid() {
		long temp=System.currentTimeMillis();
		if (temp>uNum) {uNum=temp;} else {uNum++;}
		return ""+uNum;
	}
	public String getUniqueString() {
		return getUid();
	}
	public String lc1(String s) {
		if (s==null) {return null;}
		return s.substring(0,1).toLowerCase()+s.substring(1);
	}
	public String uc1(String s) {
		if (s==null) {return null;}
		return s.substring(0,1).toUpperCase()+s.substring(1);
	}
	
	public void showConsole() {
		IConsoleManager cmgr=ConsolePlugin.getDefault().getConsoleManager();
		cmgr.showConsoleView(c);
	}
	
	
	public PrintWriter getLogger() {
		if (log && (logger==null) ) {
			logger=new PrintWriter(new ConsoleWriter(getConsoleStream()));
		}
		return logger;
	}

	public MessageConsoleStream getConsoleStream() {
		if (mcs==null) {
			IConsoleManager cmgr=ConsolePlugin.getDefault().getConsoleManager();
			IConsole[] consoles=cmgr.getConsoles();
			for (int i = 0; i < consoles.length; i++) {
				if (consoles[i].getName().equals("PBE Console")) {
					c=(MessageConsole)consoles[i];
					break;
				}
			}
			if (c==null) {c=new MessageConsole("PBE Console",null); 
			MessageConsole[] ca=new MessageConsole[] {c};
			cmgr.addConsoles(ca);
			}
			mcs=c.newMessageStream();
		}
		showConsole();
		return mcs;
	}
		
	public void log(Object o) {p(o);}
	
	public void p(Object o) {
		if (log) {
		MessageConsoleStream mcs=getConsoleStream();
//			cmgr.showConsoleView(c); -- not needed
			
		if (o==null) {mcs.println("null");} else mcs.println(o.toString());
		}
	}

	public IFile getFile(IPath path) {
		return wsr.getFile(path);
	}
	public IFile getFile(IContainer c,String relPath) {
		return c.getFile(new Path(relPath));
	}
	public IFile getFileWithNewExtension(IFile f, String newext) {
		IPath path=f.getFullPath().removeFileExtension().addFileExtension(newext);
		return wsr.getFile(path);
	}
	public IFile getFile(String workspacePath) {
		return wsr.getFile(new Path(workspacePath));
	}
	public IFileStore getFileStore(String fileSystemPath) {
		return EFS.getLocalFileSystem().getStore(new File(fileSystemPath).toURI());
	}
//	public IFile findFile(IContainer f, String targetName ) {
//		if (fileFinder==null) {fileFinder=new FileFinder();}
//		return fileFinder.find(f,targetName);
//	}
//	public IFile getFile(URI uri) {
//		return wsr.getFile(URI2Path(uri));
//	}
	public IFile save(String s,IFile f){
			return save(new ByteArrayInputStream(s.getBytes()),f);
	}
	public File saveExternal(String s,String externalFilePath){
		File file = new File(externalFilePath);
		StringReader in = new StringReader(s);
		FileWriter out;
		try {
			out = new FileWriter(file);
			char[] cbuf=new char[4096];
			while (in.ready()) {
				int count=in.read(cbuf);
				if (count==-1) {break;}
				out.write(cbuf,0,count);
			}
			in.close(); out.close();
		} catch (IOException e) {e.printStackTrace(getLogger());} 
		return file;
	}
	public IFile save(InputStream is,IFile f){
		try {
			if (f.exists()) {f.setContents(is,true,true,new NullProgressMonitor());}
			else {getContainer(f);f.create(is,true,new NullProgressMonitor());}
		} catch (CoreException e) {e.printStackTrace(getLogger());}
		return f;
	}
	/**
	 * @param f
	 * @throws CoreException
	 */
	private IContainer getContainer(IResource r) throws CoreException {
		IContainer parent = r.getParent();
		if (parent.exists()) {if (isProject(parent)) {((IProject)parent).open(null);} return parent;}
		
		if (isFolder(parent)) {
			getContainer(parent);
			((IFolder)parent).create(true,true,null);
		}
		else if (isProject(parent)) {
			((IProject)parent).create(null);
			((IProject)parent).open(null);
		}
		return parent;
		
	}
	public IFile save(org.w3c.dom.Element e,IFile f) {
		return save(e.getOwnerDocument(),f);
	}
	
	public IFile save(Document doc,IFile f){
		return save(doc2InputStream(doc),f);
	}
	public IFile save(Document doc,IPath fullPath){
		return save(doc2InputStream(doc),wsr.getFile(fullPath));
	}
//	public Document getDoc(URI uri) {
//		IPath path=new Path(uri.path());
//		String scheme=uri.scheme();
//		if ("platform".equals(scheme)) {
//			if ("plugin".equals(path.segment(0))) {
//				return getDoc(path.segment(1), path.removeFirstSegments(2).toString());
//			}
//			if ("resource".equals(path.segment(0))) {
//				return getDoc(getFile(path.removeFirstSegments(1)));
//			}
//		}
//		else if (uri.isFile()) {
//			return getDocExternal(uri.devicePath());
//		}
//		return null;
//	}
	
	public Document getDocExternal(String path) {
		Document doc=null;
		try {
			FileInputStream is = new FileInputStream(path);
			doc=getDoc(is);
		} catch (FileNotFoundException e) {}
		return doc;
	}
	public Document getDoc(IFile f) {
		if (f==null) {return null;}
		XML2DOMTransform ad1 = new XML2DOMTransform();
		return ad1.execute(f);
	}
	public org.w3c.dom.Element getDocRoot(IFile file) {
		Document d=getDoc(file);
		if (d==null) {return null;} 
		return d.getDocumentElement();
	}
	public Document getDoc(InputStream is) {
		if (is==null) {return null;}
		XML2DOMTransform ad1 = new XML2DOMTransform();
		return ad1.execute(is);
	}
	public org.w3c.dom.Element getDocRoot(InputStream is) {
		return getDoc(is).getDocumentElement();
	}
	public Document getDocFromFile(String workspacePath) {
		return getDoc(getFile(workspacePath));
	}
	public org.w3c.dom.Element getDocRootFromFile(String workspacePath) {
		return getDocFromFile(workspacePath).getDocumentElement();
	}
	public Document getDocFromString(String xmlString) {
		return getDoc(new ByteArrayInputStream(xmlString.getBytes()));
	}
	public org.w3c.dom.Element getDocRootFromString(String xmlString) {
		return getDocFromString(xmlString).getDocumentElement();
	}
	public Document getDoc(String fn) {
		return getDoc(getFile(fn));
	}
	public Document getDoc(String pluginId,String relPath) {
		Document d=null;
		if (wsr.getProject(pluginId).exists()) {d=getDoc(pluginId+'/'+relPath);}
		if (d==null) {d=getDoc(getPluginFileInputStream(pluginId,relPath));}
		return d;
	}
	public org.w3c.dom.Element getDocRoot(String pluginId,String relPath) {
		org.w3c.dom.Element root=null;
		Document d=getDoc(pluginId,relPath);
		if (d!=null) {root=d.getDocumentElement();}
		return root;
	}
	
	public IFile genFile(String path, Emitter e, Object model, boolean overwriteFlag) {
//		p("Generating: "+path);
		if (overwriteFlag==NOOVERWRITE) {IFile file=wsr.getFile(new Path(path)); if (file.exists()) {return file;}}
		String content=e.generate(model);
		return genFile(path,content, overwriteFlag);
	}

	

	/**
	 * @param qName - qualified filename, eg "myProject/myFolder/myFile.java"
	 */
	public IFile genFile(String workspacePath, String content, boolean overwriteFlag) {
		Path p=new Path(workspacePath);
		IFile file=wsr.getFile(p);
//				p(s);
				try {
					if (file.exists()) {
						if (overwriteFlag==OVERWRITE) {file.setContents(new ByteArrayInputStream(content.getBytes()),false,false, null);p("File recreated: "+file.getLocation());}
					}
					else {
						genFolder(p.removeLastSegments(1)); 
						file.create(new ByteArrayInputStream(content.getBytes()),false,null);
						p("File created: "+file.getLocation());
					}
				} catch (CoreException e2) {
					// TODO Auto-generated catch block
					p("genFile error: "+e2);
//					e2.printStackTrace();
				}
		return file;
	}

	
	public void genFiles(String folderPath, Emitter e, Collection<?> c, boolean overwriteFlag) {
//		p("Generating: "+c);
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			FileModelOld m = (FileModelOld) iter.next();
			if (!folderPath.endsWith("/")) {folderPath=folderPath+'/';}
			genFile(folderPath+m.getResourceName(), e,m, overwriteFlag);
		}
	}


	
//	public IProject genProject(String target,boolean overwriteFlag) throws CoreException {
//		IProject pnew=wsr.getProject(target); // create handle to project (may or may not exist)
//		if (pnew.exists()) {
//			if (overwriteFlag==NOOVERWRITE) {p("Project "+target+" already exists, no action taken"); p(""); pnew.open(null);return pnew;}
//			else {pnew.delete(true,false,null);} // need to clear any existing project in ws
//		}
//		pnew.create(null); p("Project "+target+" created"); pnew.open(null);
//		return pnew;
//	}	
	
	public IProject copyExternalProject(String targetName,String sourcePath, boolean overwriteFlag) throws CoreException { //or overwrite alwaya? so client repsonsible for check
	IPath source=new Path(sourcePath);	
	
	
//		p("Preparing to create Project: "+targetName);
//		p("from: "+source);
//					Bundle b=Platform.getBundle("com.ibm.this.P4ebMenu");
		//			IPath path=null;
		//			try {
		//				path=new Path(Platform.asLocalURL(b.getEntry("/OrderSystemEJB")).getPath());
		//			} catch (IOException e1) {
		//				// TODO Auto-generated catch block
		//				e1.printStackTrace();
		//			}
		//			//		IPath path=new Path("C:/MyWorkspaces/P4ebMDALab/OrdersSystemEJB");
		//			//		IPath path=new Path("/P4ebMDALab/OrdersSystemEJB");
		
		IProject pnew=wsr.getProject(targetName); // create handle to project (may or may not exist)
		if (pnew.exists()) {
			if (overwriteFlag==NOOVERWRITE) {p("Project exists: "+targetName); return pnew;}
			else {pnew.delete(true,false,null);} // need to clear any existing project in ws
		}
		IProject ptemp=wsr.getProject("T"+getUniqueString()); //must be a slot for use in ws
		ptemp.delete(false,false,null); // must clear - import fails if already exists
		IProjectDescription pd=ws.newProjectDescription("ipsolorem"); //can be any old name - create will override
		pd.setLocation(source);
		try {
//			p("***Creating "+ptemp.getName()+ " from "+sourcePath);
		ptemp.create(pd,null); //import the foreign project into ws (via logical mapping)
			
		} catch (CoreException e) {
			p("Exception during access of source: "+sourcePath+"::"+ptemp.getName()); 
			p("NB Source MUST be outside the workspace! (Eclipse restriction)");
			dump(e);
			throw(e);
		}
		
		ptemp.open(null);	
		IProjectDescription pdnew=ptemp.getDescription();
//		p(pdnew.getLocation());
		pdnew.setName(targetName);
		pdnew.setLocation(null); //signals within workspace
		try {
			ptemp.copy(pdnew,false, new NullProgressMonitor()); // copy to dest defined by pdnew
		} catch (CoreException e) {
//			p("Exception in during copy operation to: "+targetName); 
			dump(e);
		}
		//		p("Syncing ...");
		//		IProject pnew=wsr.getProject("testCopy");
		//		pnew.refreshLocal(IResource.DEPTH_INFINITE,null);
		//		p(pd.getLocation());
		//		pnew.open(null);
		ptemp.delete(false,false,null); //delete logical mapping, not content
//		p("Created Project: "+targetName+" from "+ptemp.getName());
//		p("");
		return pnew;
		
	}
	public IProject getProject(String name) throws CoreException {
		if ( (name==null) || name.length()==0) {return null;}
		IProject proj=wsr.getProject(name);
		if (!proj.exists()) {proj.create(null);}
		proj.open(null);
		return proj;
	}
	
	
	public IProject importWorkspaceProject(String targetName) throws CoreException { //or overwrite alwaya? so client repsonsible for check
		// imports or creates new project -- exists returns whether defined in ws, not whether file exists
		// Does not overwrite existing project - impossible with internal project
		IProject pnew=wsr.getProject(targetName); // create handle to project (may or may not exist)
		if (!pnew.exists()) {
			pnew.create(null); // create will import from filesystem if it exists
			pnew.open(null); // need to do this before can set property
			pnew.setSessionProperty(IMPORTED_PROJECT_FLAG,Boolean.TRUE);
		} else {pnew.open(null);pnew.setSessionProperty(IMPORTED_PROJECT_FLAG,null);}
		return pnew;		
	}
		
	public IProject importExternalProject(String sourcePath,String targetName) throws CoreException { //or overwrite alwaya? so client repsonsible for check
		IProject pnew=wsr.getProject(targetName.trim()); // create handle to project (may or may not exist)
		if (pnew.exists()) {pnew.open(null);pnew.setSessionProperty(IMPORTED_PROJECT_FLAG,null);}
		else {	
			IPath source=new Path(sourcePath.trim());	
			IProjectDescription pd=ws.newProjectDescription("ipsolorem"); //can be any old name - create will override
			pd.setLocation(source);
			try {
//				p("***Creating "+pnew.getName()+ " from "+sourcePath);
				pnew.create(pd,null); //import the foreign project into ws (via logical mapping)
				
			} catch (CoreException e) {
				p("Exception during access of project at: "+sourcePath); 
				p("NB: Source MUST be outside the workspace! (Eclipse restriction)");
				dump(e);
				return null;
			}
			pnew.open(null); // need to do this before can set property
			pnew.setSessionProperty(IMPORTED_PROJECT_FLAG,Boolean.TRUE);
		}
		return pnew;		
	}
	public boolean wasImported(IResource r) {
		boolean imported=false;
		if (r!=null) {
		try {
			imported=(null!=r.getSessionProperty(IMPORTED_PROJECT_FLAG));
		} catch (CoreException e) {e.printStackTrace(logger);}
		}
		return imported;
	}
	
	public IProject importExternalProject(String sourcePath) throws CoreException {
		String targetName=(new Path(sourcePath)).lastSegment();	
		return importExternalProject(sourcePath,targetName);
	}
	
	public IFolder genFolder(String target) throws CoreException {
		return genFolder(new Path(target));
	}
	public IFolder genFolder(IPath p,boolean b) throws CoreException {
		return genFolder(p);
	}
	
	public IFolder genFolder(IPath p) throws CoreException {
		if (p.segmentCount()>1) {
			IFolder f=wsr.getFolder(p);
			if (f.exists()) {
//				p("Folder exists: "+p); 
				return f;}
			else {try{  genFolder(p.removeLastSegments(1)); f.create(true,true,null); }
			catch (CoreException e) {p(e); }
			return f;
			}
		} else {
		IProject pnew=getProject(p.segment(0)); // create handle to project (may or may not exist)
		return null; // return null as Project/Folder are incompatible!
		}
	}



	
	public String getProjectName(String path) {
		IPath p=new Path(path);
		return p.segment(0);
	}
	public String extractFilenameNoExt(String path) {
		IPath p=new Path(path);
		return p.removeFileExtension().lastSegment();
	}
	public String extractFilenameNoExt(IFile f) {
		IPath p=f.getFullPath();
		return p.removeFileExtension().lastSegment();
	}
	
//	public URI getURI(EObject eo) {
//		URI uri=eo.eResource().getURI();
//		return uri;
//		}
//	
//	public URI getURI(IResource eo) {
//		URI uri=path2URI(eo.getFullPath());
//		return uri;
//		}
//	
//	public IPath getPath(EObject eo) {
//	if ((eo==null)||(eo.eResource()==null)) {return null;}	
//	URI uri=eo.eResource().getURI();
//	IPath p=(new Path(URI.decode(uri.path()))).removeFirstSegments(1);
//	return p;
//	}
//		
//	public String getProjectName(EObject eo) {
//		URI uri=eo.eResource().getURI();
//		IPath p=(new Path(uri.path())).removeFirstSegments(1);
//		return p.segment(0);
//	}
//	
//	public IProject getProject(EObject eo) {
//		 return wsr.getProject(getProjectName(eo));
//	}
//	
//	public URI getFolder(EObject eo) {
//		URI uri=eo.eResource().getURI();
//		IPath p=(new Path(uri.path())).removeFirstSegments(1);
//		return uri.trimSegments(1);
//	}
	
	public IPath string2Path(String s) {
		return root.append(s);
	}
//	public URI path2URI(IPath p) {
//		return string2URI(p.toString());
//	}
//	public IPath URI2Path(URI uri) {
//		return new Path(uri.path()).removeFirstSegments(1);
//	}
//	public URI string2URI(String s){
//		URI uri=null;
//		if (s==null) {return null;}
////		return uri=URI.createURI(profilePath);
//		uri=URI.createURI(s);
//		String scheme=uri.scheme();
//		if (scheme==null) {uri=URI.createPlatformResourceURI(s);}
//		else if (scheme.length()==1) {uri=URI.createFileURI(s);}
//		return uri;
//	}
//
//	public URI changeExtension(URI uri,String ext) {
//		return uri.trimFileExtension().appendFileExtension(ext);
//	}
//	public URI changeFile(URI uri,String newName_Ext) {
//		return uri.trimSegments(1).appendSegment(newName_Ext);
//	}
//	public URI changeFileName(URI uri,String newName) {
//		String ext=uri.fileExtension();
//		return uri.trimSegments(1).appendSegment(newName).appendFileExtension(ext);
//	}
	public IFile changeFileName(IFile f, String newName) {
		String ext=f.getFileExtension();
		return wsr.getFile(f.getFullPath().removeLastSegments(1).append(newName).addFileExtension(ext));
	}
	public IFile changeFile(IFile f, String newName_Ext) {
		return wsr.getFile(f.getFullPath().removeLastSegments(1).append(newName_Ext));
	}
	public IFile changeExtension(IFile f, String ext) {
		return wsr.getFile(f.getFullPath().removeFileExtension().addFileExtension(ext));
	}
	public void edit(IFile f,String editorId) {
		try {
			getActivePage().openEditor(new FileEditorInput(f),editorId);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(getLogger());
		}
	}
	
	public IEditorPart edit(IFile f) {
		if (f==null) {log("Error - null file passed for editing - ignored");return null;}
		String edId="org.eclipse.ui.DefaultTextEditor"; //our default
		IEditorDescriptor ied=PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(f.getName());
		if (ied!=null) {edId=ied.getId();}
//		log("Default Editor found is: "+edId);
		IWorkbenchPage wp=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			return wp.openEditor(new FileEditorInput(f),edId);
		} catch (PartInitException e) {
			e.printStackTrace(getLogger());
		}
		return null;
	}
	public void editExternal(IFile f) {
		if (f==null) {log("Error - null file passed for editing - ignored");return;}
		String edId=wb.getEditorRegistry().SYSTEM_EXTERNAL_EDITOR_ID; //our default
		IWorkbenchPage wp=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			wp.openEditor(new FileEditorInput(f),edId);
		} catch (PartInitException e) {
			e.printStackTrace(getLogger());
		}
	}
	public void editExternal(IFileStore f) {
		if (f==null) {log("Error - null file passed for editing - ignored");return;}
		String edId=wb.getEditorRegistry().SYSTEM_EXTERNAL_EDITOR_ID; // pass to external system to choose
		p(edId);
		IWorkbenchPage wp=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			wp.openEditor(new FileStoreEditorInput(f),edId);
		} catch (PartInitException e) {
			e.printStackTrace(getLogger());
		}
	}
	public void editExternal(String fileSystemPath) {
		editExternal(getFileStore(fileSystemPath));
	}
	public void dumpEditors() { // outdated??? 
		IFileEditorMapping[] fems = wb.getEditorRegistry().getFileEditorMappings();
		for (int i = 0; i < fems.length; i++) {
			IFileEditorMapping fem = fems[i];
			log(fem.getExtension()+":"+fem.getDefaultEditor());
//			if ("text".equals(fem.getExtension())) {
			dump(fem.getEditors());
			log("---");
//			}
		}
	}
	public IFile saveInputStream2File(InputStream is,IFile f){
		try {
			if (f.exists()) {f.setContents(is,true,true,new NullProgressMonitor());}
			else {getContainer(f);f.create(is,true,new NullProgressMonitor());}
		} catch (CoreException e) {e.printStackTrace(getLogger());}
		return f;
	}

	public void saveDoc(Document doc,IFile f){
		saveInputStream2File(doc2InputStream(doc),f);
	}
	public void saveDoc(org.w3c.dom.Node anyNodeInTheDoc,IFile f){
		saveDoc(anyNodeInTheDoc.getOwnerDocument(),f);
	}
	public void saveDoc(org.w3c.dom.Node anyNodeInTheDoc) {
		Document doc=anyNodeInTheDoc.getOwnerDocument();
		IFile tgtFile=(IFile)doc.getUserData("pbeFile");
		saveDoc(doc,tgtFile);
	}
	public void setFile(org.w3c.dom.Node anyNodeInTheDoc,IFile f){
		anyNodeInTheDoc.getOwnerDocument().setUserData("pbeFile", f, null);
	}
	public IFile getFile(org.w3c.dom.Node anyNodeInTheDoc){
		return (IFile)getDoc(anyNodeInTheDoc).getUserData("pbeFile");
	}
	public boolean hasFile(org.w3c.dom.Node anyNodeInTheDoc){
		if (anyNodeInTheDoc==null) {return false;}
		return getDoc(anyNodeInTheDoc).getUserData("pbeFile")!=null;
	}
	public Document getDoc(org.w3c.dom.Node anyNodeInTheDoc){
		if ( (anyNodeInTheDoc==null) || org.w3c.dom.Node.DOCUMENT_NODE==anyNodeInTheDoc.getNodeType()) {return (Document)anyNodeInTheDoc;}
		return anyNodeInTheDoc.getOwnerDocument();
	}
	
	public ByteArrayInputStream doc2InputStream(org.w3c.dom.Node docNode) {
		StreamResult result= new StreamResult(new ByteArrayOutputStream());
		doc2sr(docNode,result);
		return new ByteArrayInputStream(((ByteArrayOutputStream)result.getOutputStream()).toByteArray());
	}
	
	public StringBuffer doc2StringBuffer(org.w3c.dom.Node docNode) {
		StreamResult result= new StreamResult(new StringWriter());
		doc2sr(docNode,result);
		return ((StringWriter)result.getWriter()).getBuffer();
	}
	public String doc2String(org.w3c.dom.Node docNode) {
		return doc2StringBuffer(docNode).toString();
	}
	
	private StreamResult doc2sr(org.w3c.dom.Node docNode,StreamResult sr) {
		Source ds=new DOMSource(docNode);
		//	Source ds=new TestSource(); // throws TransformerException on class
		Transformer t=null;
		try {
			t = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e2) {
			e2.printStackTrace(getLogger());
		} catch (TransformerFactoryConfigurationError e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace(getLogger());
		}
//		log("**Transformer:"+t);
		t.setOutputProperty(OutputKeys.INDENT,"yes");
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");
//		t.setOutputProperty("{http://www.ibm.com/xmlns/prod/xltxe-j}indent-amount","2");
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//	    transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");

		try {
			t.transform(ds,sr);
		} catch (TransformerException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace(getLogger());
		}
		return sr;
	}

	public List<IFile> getBaseSelectionList() {
		ISelection sel=getBaseSelection();
		if (sel instanceof IStructuredSelection) {return ((IStructuredSelection)sel).toList();}
		List<IFile> l=new ArrayList<IFile>();
		if (sel instanceof TextSelection) {
			IFile f=getEditedFile();
			if (f!=null) {l.add(f);}
		}
		return l;
	}
	
	public ISelection getBaseSelection() {
		ISelection sel=getActivePart().getSite().getSelectionProvider().getSelection();
		return sel;
	}

	public IResource getActiveResource() {
		IWorkbenchPart wp=getActivePart();
//		log("Active part: "+wp);
//		dump(wp.getClass());
//		log(wp.getTitle());
//		log(((WorkbenchPart)wp).getPartName());
		if (wp instanceof IEditorPart) {
		IEditorInput inp = ((IEditorPart)wp).getEditorInput();
		return (IFile)inp.getAdapter(IFile.class);
		} else if (wp instanceof IViewPart) {
			return getSelectedResource();
		}
		return  null;
	}
	public IFile getActiveFile() {
		IResource r=getActiveResource();
		if (isFile(r)) {return (IFile)r;} else {return null;}
	}
	
	public IFile getEditedFile() {
		IWorkbenchPart wp=getActivePart();
		if (!(wp instanceof IEditorPart)) {return null;}
		IEditorInput inp = ((IEditorPart)wp).getEditorInput();
		return (IFile)inp.getAdapter(IFile.class);
	}

	public Object getFirstSelected() {
		ISelection sel=getBaseSelection();
		if (sel instanceof IStructuredSelection) {
			return ((IStructuredSelection)sel).getFirstElement();
		} else return null;
	}
	
	public IResource getSelectedResource() {
		List<IFile> elts=getBaseSelectionList();
		if (elts.isEmpty()) {return null;} else {
			Object o=elts.get(0);
			if (o instanceof IAdaptable) {
				IResource r=(IResource)((IAdaptable)o).getAdapter(IResource.class);
				if (r==null) {
					IContributorResourceAdapter icr = (IContributorResourceAdapter)((IAdaptable)o).getAdapter(IContributorResourceAdapter.class);
					if (icr!=null) {r = icr.getAdaptedResource((IAdaptable)o);
					}
				}
				return r;} 
			else return null;
		}	
	}
	public List<IResource> getSelectedResources() {
		List<IFile> elts=getBaseSelectionList();
		List<IResource> resources=new ArrayList<IResource>();
		for (Iterator<IFile> iter = elts.iterator(); iter.hasNext();) {
			IResource r=(IResource)((IAdaptable)iter.next()).getAdapter(IResource.class);
			resources.add(r);
		}
		return resources;
	}
	public boolean isFile(IResource r) {
		if ( (r!=null) && (r.getType()==IResource.FILE) ) {return true;} else {return false;}
	}
	public boolean isProject(IResource r) {
		if ( (r!=null) && (r.getType()==IResource.PROJECT) ) {return true;} else {return false;}
	}
	public boolean isFolder(IResource r) {
		if ( (r!=null) && (r.getType()==IResource.FOLDER) ) {return true;} else {return false;}
	}
	public boolean isContainer(IResource r) {
		return (isFolder(r)||isProject(r)); // deliberately omit test for wsr - not normally what we want 
	}
	


	public void dump(PreferenceStore prefs) {
		if (log) {prefs.list(getLogger());}
	}
	public void dump(Map<?, ?> map) {
		if (log) {
			Set<?> entries=map.entrySet();
			log("No of Entries:"+entries.size());
			for (Iterator<?> iterator = entries.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				log(entry.getKey()+"="+entry.getValue());
			}
		}
	}
	public void dump(Properties props) {
		if (log) {props.list(getLogger());}
	}
	
	public void dump(InputStream is) {
		if (is==null) {dump("null");return;}
		LineNumberReader isr;
		try {
			isr =  new LineNumberReader(new InputStreamReader(is));
			while (isr.ready()) {
				log(isr.readLine());
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(getLogger());
		}
	}
	public void dump(IFile f) {
		try {
			dump(f.getContents());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(getLogger());
		}
	}
	public void dump(java.lang.Class sc) {
		if (sc==null) {log("WARNING: Requested to dump null class - nothing done");return;}
		log("Interfaces:");
		java.lang.Class[] ints = sc.getInterfaces();
		dump(ints);
		log("");
		log("Superclass hierarchy:");
		dumpParent(sc);
		log("");
		log("Methods:");
		dump(sc.getMethods());
	}
	private void dumpParent(java.lang.Class sc) {
		log(sc);
		if (sc==Object.class) {return;} else {
			dumpParent(sc.getSuperclass());
		}
		
	}
	
	
	public void dump(Object o) {
		p(o);
	}
	public void dumpClass(Object o) {
		dump(o.getClass());
	}


	
	public void dump(Object[] array) {
		for (int i = 0; i < array.length; i++) {
			dump(array[i]);
		}
	}
	public void dump(Collection<?> c) {
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			dump(iter.next());
		}
	}
	public void dump(Enumeration<?> e) {
		for (; e.hasMoreElements();) {
			dump(e.nextElement());
		}
	}
	public void dump(org.w3c.dom.Element root){
		dump(root.getOwnerDocument());
	}
	public void dump(Document d){
		p(doc2String(d));
	}
	/**
	 * @param e
	 */
	public void dump(CoreException e) {
		dump(e.getStatus());
		e.printStackTrace(getLogger());
	}
	public void dump(Exception e) {
		e.printStackTrace(getLogger());
	}
	/**
	 * @param status
	 */
	public void dump(IStatus st) {
		String plugin=st.getPlugin();
		if (st.isOK()) {log("Successful completion status from "+plugin);return;}
//		p(plugin+" status: "+st.getSeverity());
		p(st.getMessage());
		if (st.getException()!=null) {p(st.getException());}
		IStatus[] sts = st.getChildren();
		for (int i = 0; i < sts.length; i++) {
			dump(sts[i]);
		}
	}
	public void dump(IJavaElement je) {
		dump(je.getElementName());
	}

	public Document getNewDocument() {
		Document doc=null;
	try {
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
	} catch (FactoryConfigurationError e) {
		e.printStackTrace();
	}
	
	return doc;
	}
	/**
	 * @param string
	 * @return
	 */
	public Document getNewDocument(String root) {
		Document d=getNewDocument();
		d.appendChild(d.createElement(root));
		return d;
	}
	public org.w3c.dom.Element getNewDocumentRoot(String root) {
		Document d=getNewDocument();
		org.w3c.dom.Element r=d.createElement(root);
		d.appendChild(r);
		return r;
	}
	/**
	 * @param file
	 * @return
	 */
	public Document getResolvedDoc(IFile file) {
		XMLResolver xr = new XMLResolver();
		return xr.execute(file);
	}
	/**
	 * @param parent
	 * @param string
	 * @return
	 */
	public org.w3c.dom.Element addElement(org.w3c.dom.Node parent, String s) {
		Document d=parent.getOwnerDocument();
		if (d==null) {d=(Document)parent;}
		org.w3c.dom.Element e=d.createElement(s);
		parent.appendChild(e);
		return e;
	}
	/**
	 * @param te1
	 * @param string
	 */
	public Text addText(org.w3c.dom.Element e, String text) {
		Text t=e.getOwnerDocument().createTextNode(text);
		e.appendChild(t);
		return t;
	}
	/**
	 * @param ps
	 */

	/**
	 * @return
	 */
	
	
	public void saveEditors() {
		wb.saveAllEditors(false);
	}
	public void saveEditorsWithConfirm() {
		wb.saveAllEditors(true);
	}
	public void switchPerspective(String id) {
		IWorkbenchPage page = getActivePage();
//		utils.log(ap.getPerspective().getId());
//		utils.log(ap.getPerspective().getLabel());
//		IPerspectiveDescriptor[] ps = pr.getPerspectives();
//		for (int i = 0; i < ps.length; i++) {
//			IPerspectiveDescriptor pd = ps[i];
//			utils.log(pd.getId()+": "+pd.getLabel());
//		}
//		IPerspectiveDescriptor pj = pr.findPerspectiveWithId("org.eclipse.jdt.ui.JavaPerspective");
		IPerspectiveDescriptor p = pr.findPerspectiveWithId(id);
		page.setPerspective(p);
	}
	
	
	
	
	/**
	 * @return Returns the globals.
	 */
	public Preferences getGlobals() {
		if (globals==null) {globals=new Preferences();}   //????
//		if (globals==null) {try {globals=PBEPlugin.getDefault().getPluginPreferences();} catch(Exception e) {globals=new Preferences();}  }
		if (globals==null) {try {globals=Activator.getDefault().getPluginPreferences();} catch(Exception e) {globals=new Preferences();}  }
		return globals;
	}
	
	public void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {log(e);}
	}
	/**
	 * @param profileURI
	 * @return
	 */
//	public String getName(URI uri) {
//		return uri.trimFileExtension().lastSegment();
//	}
	public String getName(IPath p) {
		return p.removeFileExtension().lastSegment();
	}
	public String getName(IResource r) {
		return r.getProjectRelativePath().removeFileExtension().lastSegment();
	}
	public String getName(String filePath) {
		return filePath.substring(filePath.replace('\\','/').lastIndexOf('/')+1,filePath.lastIndexOf('.'));
	}
	
	public StopWatch getTimer() {return new StopWatch();}
	
	/**
	 * @param d
	 */

	/**
	 * @param sel
	 * @return
	 */
	public String getFileType(IResource f) {
		if (!isFile(f)) {return null;}
		return f.getFileExtension();
	}
	/**
	 * 
	 * @return
	 */
	public String askForFile() {
		return askForFile("C:\\");
	}
	public String[] askForFiles() {
		return askForFiles("C:\\");
	}
	public String askForFile(String root) {
		FileDialog fd = new FileDialog(getShell(),SWT.SINGLE);
		fd.setFilterPath(root);
		//	DirectoryDialog fd = new DirectoryDialog(wbShell,SWT.MULTI);
		return fd.open();
	}
	public String[] askForFiles(String root) {
		FileDialog fd = new FileDialog(getShell(),SWT.MULTI);
		fd.setFilterPath(root);
		fd.open();
		//	DirectoryDialog fd = new DirectoryDialog(wbShell,SWT.MULTI);
		String path=fd.getFilterPath();
		String[] fns=fd.getFileNames();
		List<String> files=new ArrayList<String>();
		for (int i = 0; i < fns.length; i++) {
			String file = path+"\\"+fns[i];
			files.add(file);
		}
		return (String[])files.toArray(new String[] {});
	}
	
	public String askForDirectory() {
		return askForDirectory(null,"C:/");
	}
	public String askForDirectory(String root) {
		return askForDirectory(null,root);
	}
	
	public String askForDirectory(String message, String root) {
		DirectoryDialog fd = new DirectoryDialog(getShell());
		fd.setFilterPath(root);
		if (message==null) {message="Please select a directory";}
		fd.setMessage(message);
		//	DirectoryDialog fd = new DirectoryDialog(wbShell,SWT.MULTI);
		return fd.open();
	}
	public void nolog(Object o) {}
	
	public String askForInput(String title, String question, String initialValue) {
		InputDialog d = new InputDialog(null,title,question,initialValue,null);
		d.open();
		return d.getValue();
	}
	public boolean askQuestion(String title, String question) {
		return MessageDialog.openQuestion(getShell(), title, question);
	}
	
	public Properties getPatternDescriptor(IProject proj) {
		Properties patDesc=null;
		try {
		if (!proj.hasNature(DPTK_NATURE)) {return null;}
		patDesc=new Properties();
		patDesc.load(proj.getFile(".pattern").getContents());
		} catch (Exception e) {
			e.printStackTrace(getLogger());
		} 
		return patDesc;
	}
	
	public IFile backup(IFile target) {
		log("Backing up: "+target);
		if (!target.exists()) {return null;}
		try {
			IPath oldPath=target.getFullPath();
			IPath newPath = target.getFullPath().addFileExtension(getUid()).addFileExtension("backup");
			target.move(newPath,true,null);
			IFile backup=getFile(newPath);
//			IFile backup=target;
//			target=getFile(oldPath);
			log("Orig: "+target);
			log("Orig: "+backup);
			return backup;
		} catch (CoreException e) {e.printStackTrace(getLogger());return null;}
	}
	public IFile copyWithBackup(IFile source,IFile target) {
		if (!(source.exists())) {return null;}
		try {
		if (target.exists()) {backup(target);}
		source.copy(target.getFullPath(),true,null);
		} catch (CoreException e) {e.printStackTrace(getLogger());return null;}
		return target;
	}
	public IStatus copyReplaceFolder(IFolder source, IFolder target) {
		MultiStatus ms = new MultiStatus("PBEUtils", 0, null, null);
		try {
//		log("Copying Folder "+source);
		if (!target.exists()) {
			source.copy(target.getFullPath(),true,null);
		} else {
			for (IResource r: source.members()) {
				if (isFolder(r)) {
					IStatus status=copyReplaceFolder((IFolder)r,target.getFolder(r.getName()));
					ms.merge(status);
				} else if (isFile(r)) {
//					log("Marker8: "+r+":"+target.getFile(r.getName()));
					copyReplaceFile((IFile)r,target.getFile(r.getName()));
//					log("Marker9");
				}
			}
		}
		} catch (CoreException e) {e.printStackTrace(logger); ms.add(new Status(0, "PBEUtils", null, e));}
		return ms;
	}
	
	public IFile copyReplaceFile(IFile source, IFile target) {
		if (source==null||!source.exists()) {return null;}
		try {
		if (target.exists()) {
			IPath path=target.getFullPath();
			target.delete(true,null);
//			log("MarkerB: "+target+":"+source+":"+path);
//			target.refreshLocal(IResource.DEPTH_ZERO, null);
//			log("exists="+target.exists());
			source.copy(target.getFullPath(), true,null);
//			source.copy(path, true,null);
//			log("MarkerC: "+target);
		} else {source.copy(target.getFullPath(), true,null);}
		} catch (CoreException e) {log("CoreException");e.printStackTrace(logger);return null;}
		return target;
	}

	public IFile copy(String fileSystemPath, IFile target) throws IOException, CoreException {
		File fIn= new File(fileSystemPath);
		if (fIn.isFile()) {
		FileInputStream is;
//		try {
			is = new FileInputStream(fIn);
			target.create(is,true,null);
			is.close();
//		} catch (Exception e) {e.printStackTrace(getLogger());} 
		return target;
		} else {
			log("Error - utils.copy function does not support directories, just files"); return null;
		}
	}
	public File copy(IFile source,String targetFilePath) throws IOException, CoreException {
		File fOut= new File(targetFilePath);
		byte[] buffer=new byte[4096];
//		if (!fOut.isDirectory()) {
		BufferedOutputStream os=new BufferedOutputStream(new FileOutputStream(fOut));	
		InputStream is=source.getContents();
		while (is.available()>0) {
		int nbytes=is.read(buffer);
		os.write(buffer,0,nbytes);
		}
		is.close();
		os.close();
		return fOut;
//		} else {return null;}
	}
	
	public IFile rename(IFile fIn, String newName_Ext ) throws CoreException {
		IFile fOut=changeFile(fIn,newName_Ext);
		fIn.move(fOut.getFullPath(),true,null);
		return fOut;
	}
	
	public IFile copy(String fileSystemPath, IContainer c) throws IOException, CoreException {
		File fIn= new File(fileSystemPath);
		IFile fout= c.getFile(new Path(fIn.getName()));
		if (fIn.isFile()) {
		copy(fileSystemPath,fout);
		}
		return fout;
	}
	
	public void launch(IFile launchConfig) throws CoreException {
		ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfiguration lc = lm.getLaunchConfiguration(launchConfig);
//		utils.log("LC:"+lc.getName());
		lc.launch(ILaunchManager.RUN_MODE,new NullProgressMonitor());
	}
	
	public void enableCapability(String capabilityName) {
		IWorkbenchActivitySupport wba = wb.getActivitySupport();
		Set<String> ids= new HashSet<String>(wba.getActivityManager().getEnabledActivityIds()); 
		if (ids.add(capabilityName)) {wba.setEnabledActivityIds(ids);log("Enabled capability: "+capabilityName);}
//		Set enabledActivityIds = new HashSet(activityManager.getEnabledActivityIds());
//		if (enabledIds.add("org.eclipse.javaDevelopment"))
//			workbenchActivitySupport.setEnabledActivityIds(enabledActivityIds);
	}
	public InputStream getPluginFileInputStream(String pluginId,String relPath) {
		InputStream is=null;
		if (wsr.getProject(pluginId).exists()) {try {
			IFile f=getFile(pluginId+'/'+relPath);
			if (f.exists()) {return f.getContents();}
		} catch (CoreException e) {e.printStackTrace(getLogger());}}
		Bundle b=Platform.getBundle(pluginId);
		URL url=null;
		try {
			url=b.getEntry(relPath);
			if (url!=null) {is=Platform.asLocalURL(url).openStream();} else {log(relPath+" not found in Plugin "+pluginId);}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(getLogger());
		}
		return is;
		
//		try {
//			url = Platform.asLocalURL(b.getEntry(relPath));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace(getLogger());
//		}
//		File f=new FileInputStream(url);
	}
	public void copyPluginFile(String pluginId,String relPath,IFile target) {
		PBEPath path=new PBEPath("/"+pluginId+"/"+relPath);
		IFile f=wsr.getFile(path);
		try {
		if (f.exists()) {f.copy(target.getFullPath(), true, null); return;}
		InputStream is = getPluginFileInputStream(pluginId,relPath);
			target.create(is,true,null);
			if (is==null) {log("***Source file "+f.getFullPath().makeRelative()+" not found for copy - Empty file created at: "+target);}
		} catch (CoreException e) {
			dump(e.getMessage());
		}
	}
	

	/**
	 * @param string
	 */
	/**
	 * @param m
	 * @param string
	 */

	public boolean isHeadless() {
		return !PlatformUI.isWorkbenchRunning();
	}
	
	
	public String getContentAsString(IFile file) {
		StringBuffer sb = getContentAsStringBuffer(file);
		if (sb==null) {return null;} else {return sb.toString();}
	}
	public StringBuffer getContentAsStringBuffer(IFile file) {
		if (file==null||!file.exists()) {return null;}
		try {
			return getContent(new InputStreamReader(file.getContents()));
		} catch (CoreException e) {
			e.printStackTrace(getLogger());
		} 
		return null;
	}
	
	public StringBuffer getContent(InputStreamReader rdr) {
		StringBuffer content=new StringBuffer();
		char[] cbuf = new char[4096];
		try {
			while (rdr.ready()) {
			int nchars=rdr.read(cbuf);
			content.append(cbuf,0,nchars);
			}
		} catch (IOException e) {
			e.printStackTrace(getLogger());
			return null;
		}
		return content;
	}
	public List<org.w3c.dom.Element> getChildElements(org.w3c.dom.Element parent) {
		return getChildElements(parent,null);
	}
	public List<org.w3c.dom.Element> getChildElements(org.w3c.dom.Element parent,String searchName) {
		List<org.w3c.dom.Element> elts=new ArrayList<org.w3c.dom.Element>();
		if (parent==null) {return elts;}
		boolean testName=(searchName!=null)&&(searchName.length()>0);
		boolean ignorePrefix=true; String search=searchName;
		if (testName && searchName.indexOf(':')>=0) {ignorePrefix=false;} 
		NodeList nl = parent.getChildNodes();
		for (int i=0; i< nl.getLength(); i++) {
			org.w3c.dom.Node node = nl.item(i);
			if (node.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE) {
				boolean add=true;
				if (testName) {
					String nodeName=node.getNodeName();
					if (!ignorePrefix) {if (!search.equals(nodeName)) {add=false;}}
					else if (!search.equals(substringAfterOrAll(nodeName,":"))) {add=false;}
				}
				if (add) {elts.add((org.w3c.dom.Element)node);}
			}
		}
		return elts;
	}
	public org.w3c.dom.Element getFirstChild(org.w3c.dom.Element parent)  {
		return getFirstElementAt(parent,"");
	}
	public org.w3c.dom.Element getRootElement(org.w3c.dom.Node anyNode)  {
		return anyNode.getOwnerDocument().getDocumentElement();
	}
	
	public org.w3c.dom.Element getFirstElementAt(org.w3c.dom.Element base, String path){
		// path form  eltName[@att='val']
//		log("gfa called with:"+base.getLocalName()+","+path);
		if (base==null) {return null;}
		if (path==null) {return base;}
		String tail=null;String head=null;
		int ix=path.indexOf('/');
		if (ix<0) {head=path;}
		if (ix==0) {base=base.getOwnerDocument().getDocumentElement(); 
			if (base==null) {return null;}
			int ix2=path.indexOf('/',1);
			if (ix2<0) {head=path.substring(1);}
			else {head=path.substring(1,ix2); tail=path.substring(ix2+1);}
			if (head.length()==0 || base.getLocalName().equals(head)) {return getFirstElementAt(base, tail);} else {return null;}
		}
		if (ix>0) {head=path.substring(0,ix); tail=path.substring(ix+1);}
		
		org.w3c.dom.Element child=null;
		NodeList nl = base.getChildNodes();
		if (nl.getLength()>0) {
//					log("Starting search - childnodes="+nl.getLength());
			String attName=null; String attValue=null;
			int bix=head.indexOf('[');
			if (bix>=0) {String att=head.substring(bix+1);head=head.substring(0,bix);attName=substringBeforeOrAll(att,"=");
			attValue=substringBeforeOrNull(substringAfterOrNull(att,"\""),"\"");}
			boolean testName=(head!=null)&&(head.length()>0);
			boolean ignorePrefix=true; String search=head;
			if (testName && head.indexOf(':')>=0) {ignorePrefix=false;} 

			for (int i=0; i< nl.getLength(); i++) {
				org.w3c.dom.Node node = nl.item(i);
				if (node.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE) {
//					log("Testing elt: "+node.getNodeName());
					boolean match=false;
					if (head.length()==0 || head.equals("*")) {match=true;}
					else {
						String nodeName=node.getNodeName();
						match=true;
						if (!ignorePrefix) {if (!search.equals(nodeName)) {match=false;}}
						else if (!search.equals(substringAfterOrAll(nodeName,":"))) {match=false;}
					}
					if (match && (attName!=null)) {
//						log("Found element - testing for: "+attName+"="+attValue);
						if (attValue!=null && !(((org.w3c.dom.Element)node).getAttribute(attName).equals(attValue))) {match=false;}
						if (attValue==null && !(((org.w3c.dom.Element)node).hasAttribute(attName))) {match=false;}
					}
					if (match) {child=(org.w3c.dom.Element)node;break;}
				}
			}
		}
//					log("Search for "+head+" found "+child);
		if (child==null || tail==null) {return child;} else {return getFirstElementAt(child, tail);} 
	}
	public Document parseManifestAsDoc(IFile file) {
		LineNumberReader lr;
		Document doc=getNewDocument("manifest");
		org.w3c.dom.Element root=doc.getDocumentElement();
		root.setAttribute("project",file.getProject().getName());
		root.setAttribute("path",file.getProjectRelativePath().toString());
		try {
			lr = new LineNumberReader(new InputStreamReader(file.getContents()));
		String fullLine=null; String physLine; List<String> lines=new ArrayList<String>();
		while (null!=(physLine=lr.readLine())) {
//			log(physLine);
			if (physLine.length()==0 ) {continue;}
			if (physLine.startsWith(" ")) {fullLine+=physLine;} 
			else {if (fullLine!=null) {lines.add(fullLine);};fullLine=physLine; }
		}
//		doc = root.getOwnerDocument();
		for (String line : lines) {
			log("Line:"+line);
			int index=line.indexOf(':');
			String hdr; String tail=null;
			if (index<0) {hdr=line.trim();} else {hdr=line.substring(0,index).trim(); tail=line.substring(index+1).trim();}
//			String parts[]=line.split(":");
			org.w3c.dom.Element hdrElt = addElement(root,"header");
			hdrElt.setAttribute("name",hdr);
			if ( (tail!=null) ) {
				log("tail="+tail);
//				String rest=parts[1].trim();
//				log("Rest="+tail);
				if (tail.length()>0) {
					String[] parts=tail.split(",");
					for (int i = 0; i < parts.length; i++) {
						String part=parts[i].trim();
						log("Part:"+part);
						if (part.length()>0) {
							index=part.indexOf(';');
							if (index<0) {hdr=part.trim();tail="";} else {;
							log("Index:"+index);
							hdr=part.substring(0,index).trim(); tail=part.substring(index+1).trim();
							}
							org.w3c.dom.Element hdrVal = addElement(hdrElt, "headerValue");
							hdrVal.setAttribute("value", hdr);
							if (tail.length()>0) {
								String[] parts2=tail.split(":=");
								if (parts2.length==1) { 
								} else {
								org.w3c.dom.Element att= addElement(hdrVal, "attribute");
								att.setAttribute("name", parts2[0].trim());
								att.setAttribute("value", parts2[1].trim());
							}
							}
						}
					}
				}
			}
		}
		} catch (Exception e) {e.printStackTrace(getLogger());}
		return doc;
	}

	/********************* Dummy UML functions ***************/		
//	public EObject getSelectedUMLElement() {
//		return null;
//	}
	
/********************* UML functions ***************/	
//	public IFile getFileFor(EObject eo) {
//		String name;
//		if (eo instanceof View) {name=((View)eo).getDiagram().getName();}
//		else if (eo instanceof NamedElement) {name=((NamedElement)eo).getName();}
//		else {name=eo.eClass().getName();}
//	
//		IPath xmlPath=getPath(eo).removeLastSegments(1).append(name+".xml");
//		return getFile(xmlPath);
//	}
//	public Model getModel(URI modelURI) {
//		Model m=null;
//		try {
//			m= UMLModeler.openModel(modelURI);
//		} catch (IOException e) {
//			m= UMLModeler.createModel(modelURI);
//			String modelName=modelURI.trimFileExtension().lastSegment();
//			this.rename(m,modelName);
//			save(m);
//		}
//		return m;
//	}
//	
//	public Model getModel(String modelPath) {
//		URI uri=this.string2URI(modelPath);
//		return getModel(uri);
//	}
//	
//	public Profile getProfile(URI profileURI) {
//		Profile m=null;
//		try {
//			m= UMLModeler.openProfile(profileURI);
//		} catch (IOException e) {
////			this.log("Creating profile");
//			m= UMLModeler.createProfile(profileURI);
//			if (m!=null) {this.log("No existing profile at "+profileURI+" - new profile created");}
//			save(m);
//		}
//		return m;
//	}
//	
//	public Profile getProfile(String profilePath) {
//		if (profilePath==null) {return null;}
//		URI uri=this.string2URI(profilePath);
//		return getProfile(uri);
//	}
//	
//	public void save(Model m) {
//		try {
//			UMLModeler.saveModel(m);
//			this.log("Model "+m.getName()+" saved to: "+this.getPath(m));
//		} catch (IOException e) {
//			e.printStackTrace(this.getLogger());
//		}
//	}
//	
//	public void save(Model m, URI uri) {
//		try {
//			UMLModeler.saveModelAs(m,uri);
//		} catch (IOException e) {
//			e.printStackTrace(this.getLogger());
//		}
//	}
//	
//	public void save(Model m, String path) {
//		URI uri=this.string2URI(path);
//		save(m,uri);
//	}
//	
//	public void save(Profile m) {
//		try {
//			UMLModeler.saveProfile(m);
//			this.log("Profile "+m.getName()+" saved to: "+this.getPath(m));
//		} catch (IOException e) {
//			e.printStackTrace(this.getLogger());
//		}
//	}
//	
//	public void save(Profile m, URI uri) {
//		try {
//			UMLModeler.saveProfileAs(m,uri);
//		} catch (IOException e) {
//			e.printStackTrace(this.getLogger());
//		}
//	}
//	
//	public void save(Profile m, String path) {
//		URI uri=this.string2URI(path);
//		save(m,uri);
//	}
//	
//	public Model getModel(Object o) {
//		return getPackage(o).getModel();
//	}
//
//	public Package getPackage(Object o) {
//		Package m=null;
//		if (o instanceof View) {m=((Element)((EAnnotation)(((View)o).getDiagram()).eContainer()).getEModelElement()).getNearestPackage();}
//		if (o instanceof Element) {m=((Element)o).getNearestPackage();}
//		return m;
//	}
//	
//	public Model applyProfile(Model m,Profile p) {
//		TxWrapper t=new TxWrapper(new Profile2ModelApplier(m));
//		t.execute(p);
//		return m;
//	}
//	
//	public NamedElement rename(NamedElement e,Object o) {
////		this.log("Renaming "+e.getName()+" to "+o);
//		TxWrapper t=new TxWrapper(new Name2ElementApplier(e));
//		t.execute(o);
//		return e;
//	}
//	public org.eclipse.uml2.uml.Package load(URI uri) {
//        org.eclipse.uml2.uml.Package package_ = null;
//        ResourceSet resourceSet = new ResourceSetImpl(); // must set some defaults not done directly via Resource - load returns null else
//        this.log("Loading: "+uri);
//        try {
//             Resource resource = resourceSet.getResource(uri,true);
//
//             package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(
//                       resource.getContents(), UMLPackage.eINSTANCE.getPackage());
//        } catch (WrappedException we) {
//             this.log(we.getMessage());
//        }
//     this.log("Loaded: "+package_);
//     return package_;
//	}
//
//	public void save(org.eclipse.uml2.uml.Package package_, URI uri) {
//		ResourceSet resourceSet = new ResourceSetImpl();
//        Resource resource = resourceSet.createResource(uri);  
//        resource.getContents().add(package_);
////        this.log("Saving: "+uri);
//           try {
//                resource.save(null);
//                   this.log("Saved: "+uri.devicePath());
////                   this.log(uri.toFileString());
//           } catch (IOException ioe) {
//                this.log(ioe.getMessage());
//           }
//    }
//	public List getSelectedUMLElements() {
//		return UMLModeler.getUMLUIHelper().getSelectedElements();
//	}
//	public EObject getSelectedUMLElement() {
//		List elts=getSelectedUMLElements();
//		if (elts.isEmpty()) {return null;} else {return (EObject)elts.get(0);} 
//	}
//	public Diagram getDiagram() {
//		EObject eo=getSelectedUMLElement();
//		if (eo instanceof View) {return ((View)eo).getDiagram();} else {return null;}
//	}
//	public NamedElement getUMLElementByPath(String qn) {
//	String[] segs=qn.split("::");
//	Collection models=UMLModeler.getOpenedModels();
//	boolean found=false;Model m=null;
//	for (Iterator iter = models.iterator(); iter.hasNext();) {
//		m = (Model) iter.next();
//		if (m.getName().equals(segs[0])) {found=true;break;}
//	}
//	if (!found) {p("Model not opened: "+segs[0]); return null;}
//	Element e=m;
//	for (int i = 1; i < segs.length; i++) {
//		if (e instanceof org.eclipse.uml2.uml.Class) {
//			e=((org.eclipse.uml2.uml.Class)e).getNestedClassifier(segs[i]);
//		} else if (e instanceof Package) {
//			e= ((Package)e).getOwnedMember(segs[i]);
//		} else {e=null;}
//		if (e==null) {break;}
//	}
//	return (NamedElement)e;
//	}
//	public Diagram getDiagram(Namespace ns) {
//		TxWrapper tx=new TxWrapper(new DiagramInNamespaceCreator() );
//		return (Diagram)tx.execute(ns);
//	}
//	public EList getAttributes(Node v) {
//		Node ac=null; String acName="AttributeCompartment";
//		if (!acName.equals(v.getType())) {
//			EList ch=v.getChildren();
//			for (Iterator iter = ch.iterator(); iter.hasNext();) {
//				Node v2 = (Node) iter.next();
//				if (acName.equals(v2.getType())) {ac=v2;break;}
//			}
//		} else {ac=v;}	
//		if (ac==null) {p("No Attributes found: "+ac.getElement());return null;}
//		
//		EList fo=null;EList so=null;
//		UMLListCompartmentStyle ls = (UMLListCompartmentStyle)ac.getStyle(UmlnotationPackage.eINSTANCE.getUMLListCompartmentStyle());
//		if (ls==null) {p("No List compartment found: "+ac.getElement()); return null;}
//		fo = ls.getFilteredObjects(); // entries which are filtered OUT!
////		for (Iterator iter = fo.iterator(); iter.hasNext();) {
////			EObject e = (EObject) iter.next();
////			p("FilterContent: "+e);
////		}
//
//		so = ls.getSortedObjects(); // sorted entries or none if no sorting applied
////		for (Iterator iter = so.iterator(); iter.hasNext();) {
////			EObject e = (EObject) iter.next();
////			p("SortContent: "+e);
////		}
//		if (so.isEmpty()) {so=((Class)v.getElement()).getAttributes();}
//		EList vl=new BasicEList(so);
//		vl.removeAll(fo);
//		return vl;
//	}
//	public NamedElement getParent(Diagram d) {
//		return (NamedElement)d.eContainer().eContainer();
//		
//	}
//	/**
//	 * @param d
//	 */
//	/* Returns the nodes with no target edges (nb - this means no attached comments!) */
//	public List getRoots(Diagram d) {
//		EList nodes = d.getChildren();
//		List roots=new ArrayList();
//		for (Iterator iter = nodes.iterator(); iter.hasNext(); ) {
//			Node node = (Node) iter.next();
//			if (node.getTargetEdges().size()==0) {roots.add(node);}
//		}
//		return roots;
//	}
//	/**
//	 * 
//	 * @param d 
//	 * @return
//	 */
//	public Node getFirstRoot(Diagram d) {
//		List l=getRoots(d);
//		if (l.isEmpty()) {return null;}
//		else return (Node)l.get(0);
//	}
//	public Class getClass(Package p,String name) {
//		NamedElement ne=p.getMember(name);
//		if (ne instanceof Class) {return (Class)ne;} else {return null;}
//	}
//	public Class cgetClass(Package p,String name) {
//		Class c;
//		c=getClass(p,name);
//		if (c!=null) {return c;} else {return addClass(p,name);}
//	}
//	public Class addClass(Package p,String name) {
//		return p.createOwnedClass(name,false); //default to concrete class
//	}
//	public Property getAttr(Class c,String name) {
//		return c.getAttribute(name,null);
//	}
//	public Property cgetAttr(Class c,String name) {
//		Property p=getAttr(c,name);
//		if (p!=null) {return p;} else {return addAttr(c,name);}
//	}
//	public Property addAttr(Class c,String name) {
//		Property pe = c.createOwnedAttribute(name,null);
//		return (Property)pe;
//	}
//	public Package getPackage(Package p,String name) {
//		NamedElement ne=p.getMember(name);
//		if (ne instanceof Package) {return (Package)ne;} else {return null;}
//	}
//	public Package cgetPackage(Package p,String name) {
//		Package c;
//		c=getPackage(p,name);
//		if (c!=null) {return c;} else {return addPackage(p,name);}
//	}
//	public Package addPackage(Package p,String name) {
//		return p.createNestedPackage(name);
//	}
//	
//	
////	public void setType(TypedElement p, String typeName) {
////		p.setType((Type)m.getMember(typeName));
////	}
//	public Diagram createMainDiagram(Namespace element, String diagramName) {
//		final Namespace na=element; final String name=diagramName;
//		TxWrapper t=new TxWrapper(new Executable() {public Object execute(Object o) {
//		Diagram pic = udh.createDiagram(na,UMLDiagramKind.FREEFORM_LITERAL);
//		udh.setMainDiagram(na,pic);
//		pic.setName(name);
//		return pic;
//		} } );
//		return (Diagram)t.execute();
//	}
//	public void openDiagram(Diagram diagram) {
//		udh.openDiagramEditor(diagram);
//	}
//	public void openMainDiagram(Namespace element) {
//		udh.openDiagramEditor(udh.getMainDiagram(element));
//	}
//	public IFile genAppdefFile() {
//		Diagram d=getDiagram();
//		if (d==null) {return null;}
//		IPath xmlPath=getPath(d).removeLastSegments(1).append(d.getName()+".appdef");
//		IFile f=getFile(xmlPath);
//		Document doc=genAppdefDoc(d); 
//		if (doc==null) {return null;}
//		return save(doc,f);
//	}
//	
//	private IFile genAppdefFile(IFile f) {
//		Diagram d=getDiagram();
//		if (d==null) {return null;} 
//		Document doc=genAppdefDoc(d);
//		if (doc==null) {return null;} 
//		return save(doc,f);
//	}
//	
//	
//	public Document genAppdefDoc() {
//		Diagram d=getDiagram();
//		if (d==null) {return null;} else return genAppdefDoc(d);
//	}
//	public Document genAppdefDoc(Diagram d) {
//		String proj=getProject(d).getName();
//		Diagram2DOMTransform t = new BasicDiagram2DOMTransform();
//		Document doc=t.execute(d);
//		org.w3c.dom.Element root=doc.getDocumentElement();
//		if (root.hasAttribute("traceProject")) {
//			if (root.getAttribute("traceProject").length()==0) {root.setAttribute("traceProject",proj);}
//		}
//		if (root.getAttribute("targetProject").length()==0) {root.setAttribute("targetProject",proj);}
//		return doc;
//	}
//	public IFile genAppdef(Diagram d, IFile f) {
//		if (d==null) {
//			EObject eo=getSelectedUMLElement();
//			if (eo instanceof View) {d=((View)eo).getDiagram();} else {return null;}
//		}
//		if (f==null) {
//			IPath xmlPath=getPath(d).removeLastSegments(1).append(d.getName()+".appdef");
//			f=getFile(xmlPath);
//		}
//		Document doc=genAppdefDoc(d);
//		save(doc2InputStream(doc),f);
//		return f;
//	}
//	public IFile genXSD(Diagram d, IFile f) {
//		if (d==null) {
//			EObject eo=getSelectedUMLElement();
//			if (eo instanceof View) {d=((View)eo).getDiagram();} else {return null;}
//		}
//		if (f==null) {
//			IPath xmlPath=getPath(d).removeLastSegments(1).append(d.getName()+".XSD");
//			f=getFile(xmlPath);
//		}
//		Diagram2XSDTransform t = new Diagram2XSDTransform();
//		Document doc=t.execute(d);
//		save(doc2InputStream(doc),f);
//		return f;
//	}
/*************** End UML ***********************/

	
	/*************** Start DPTK ***********************/
//	
//	public PatternApplicationStatus execDPTK(Document d,String patternId,IProject project, Hashtable parms) {
////		String proj="GenProfile";
//		if (project==null) {project=wsr.getProject(".temp");}
//		if (parms==null) {parms=new Hashtable();}
////		String model=(new TestDoc()).getDoc();
////		out.println(model);
//		
//		return com.ibm.dptk.patternWizard.PatternApplicator.singleton().applyPattern(project, d, patternId, parms,null);
//	}
//	
//	public PatternApplicationStatus execDPTK(Document d,String patternId) {
////		Hashtable parms=new Hashtable();
//		return execDPTK(d,patternId,null,null);
//	}
//	public PatternApplicationStatus execDPTK(Object o,String patternId) {
////		Hashtable parms=new Hashtable();
//		IProject temp=null;
////		log("Executing: "+patternId);
////		log("Input="+o);
//		try {
//			temp = getProject(".temp");
//		} catch (CoreException e) {e.printStackTrace(getLogger());}
//		return com.ibm.dptk.patternWizard.PatternApplicator.singleton().applyPattern(temp, o, patternId, null,null);
//	}
//	
//	public void dump(PatternApplicationStatus ps) {
//		dump(ps.getStatus());
//	}
	/*************** End DPTK ***********************/
	
	
	/*************** Added since 6/1/09  ***********************/
	public boolean openAssemblyDiagram(IProject module) {
		IFile sca=module.getFile("sca.module");
		if (sca.exists()) {edit(sca);return true;} else {return false;}
	}
	
	public boolean isFileType(IResource r,String filetype) {
		return ( (isFile(r) && (filetype.equals(r.getFileExtension()))));
	}
	public boolean isFileTypeIn(IResource r,String commaSeparatedFiletypes) {
		return ( (isFile(r) && (commaSeparatedFiletypes.contains(r.getFileExtension()))));
	}
	
	public IStatus runJet(org.w3c.dom.Element root, String patternId) {
		// called by patRunner so must use Platform call!
		log("Running Pattern: "+patternId);
//		return JET2Platform.runTransformOnObject(patternId, root.getOwnerDocument(), new NullProgressMonitor());
		return null;
	}
//	public IStatus runJet(org.w3c.dom.Element root) {
//		patRunner.generate(root);
//		return Status.OK_STATUS;
////		return JET2Platform.runTransformOnObject(patternId, root.getOwnerDocument(), new NullProgressMonitor());
//	}
	
	
	public String getCallingMethod() {
		StackTraceElement[] elements = new Exception().getStackTrace();
		return elements[2].getMethodName(); // allow for caller to be called by calling method
	}
	public IFolder getFolder(IResource r) {
		IFolder f=null;
		try {
			IContainer cn = getContainer(r);
			if (isFolder(r)) { f=(IFolder)r; if (!f.exists()) {f.create(true,true,null);} } 
			else if (isFolder(cn)) {f=(IFolder)cn;}
		} catch (CoreException e) {e.printStackTrace(getLogger());}
		return f;
	}
	public IFolder getFolder(String fullPath) {
		IFolder f = wsr.getFolder(new Path(fullPath));
		if (!f.exists()) {f=getFolder(f);}
		return f;
	}
	public List<Attr> getAttributes(org.w3c.dom.Element docElt) {
		if (docElt==null) {return null;}
		List<Attr> atts=new ArrayList<Attr>();
		NamedNodeMap nl = docElt.getAttributes();
		for (int i=0;i<nl.getLength();i++) {
			atts.add((Attr)nl.item(i));
		}
		return atts;
	}
	public List<Attr> getAttributes(org.w3c.dom.Element docElt, String startsWith) {
		if (docElt==null) {return null;}
		List<Attr> atts=new ArrayList<Attr>();
		NamedNodeMap nl = docElt.getAttributes();
		for (int i=0;i<nl.getLength();i++) {
			if (nl.item(i).getNodeName().startsWith(startsWith)) {atts.add((Attr)nl.item(i));}
		}
		return atts;
	}
	public org.w3c.dom.Element getRootElement(Document doc) {
		return doc.getDocumentElement();
	}
	
	public org.w3c.dom.Node copy(org.w3c.dom.Node srcNode, org.w3c.dom.Element newParent, boolean deep) {
		if ( (srcNode==null) || newParent==null) {return null;}
		Document newDoc = newParent.getOwnerDocument();
		if (newDoc==srcNode.getOwnerDocument()) {
//		log("Copying within doc: "+srcNode.getNodeName());
		org.w3c.dom.Node newNode = srcNode.cloneNode(deep);
		return newParent.appendChild(newNode);
		} else {
//			log("Copying to new doc: "+srcNode.getNodeName());
			org.w3c.dom.Node newNode = newDoc.importNode(srcNode,deep);
			return newParent.appendChild(newNode);
		}
	}
	public org.w3c.dom.Node copy(org.w3c.dom.Node srcNode, org.w3c.dom.Element newParent) {
		return copy (srcNode,newParent,true);
	}
	public int copyContent(org.w3c.dom.Node srcNode, org.w3c.dom.Element newParent,boolean deep) {
		NodeList nl = srcNode.getChildNodes();
		int size=nl.getLength();
//		log("To copy: "+size);
		for (int i=0;i<size;i++) {
			copy(nl.item(i),newParent,deep);
		}
		return size;
	}
	public int copyContent(org.w3c.dom.Node srcNode, org.w3c.dom.Element newParent) {
		return copyContent(srcNode, newParent,true);
	}
	public String getFileExtension(IResource r) {
		if (!isFile(r)) {return null;}
		return r.getFileExtension();
	}
	public void restart() {
		wb.restart();
	}
	public String getPluginPath(String pluginId) {
		Bundle b=Platform.getBundle(pluginId);
//		log("Bundle:"+b);
		if (b==null) {return null;}
		URL url=FileLocator.find(b,new Path("/"),null);
		if (url!=null) {try {return FileLocator.toFileURL(url).toString().substring(6);} catch (IOException e) {e.printStackTrace(getLogger());}}
		return null;
	}
	public String getDate(String pattern) {
		try {
		return new SimpleDateFormat(pattern).format(new Date());
		} catch (Exception e) {return "*InvalidDatePattern:"+pattern+"*";}
	}
	
	public java.lang.Class loadClass(String path) {
		PBEPath path2=new PBEPath(path);
		return path2.getClass();
	}
	
	public void build(IProject project) {
		try {
			project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new NullProgressMonitor() );
		} catch (CoreException e1) {e1.printStackTrace(getLogger());}
	}
	public void cleanBuild(IProject project) {
		try {
			project.build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor() );
			project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor() );
		} catch (CoreException e1) {e1.printStackTrace(getLogger());}
	}
	public Set<IProject> getLibraries(IProject target) {
		IProject[] refs;
		Set<IProject> libs=new HashSet<IProject>();
		try {
			refs = target.getDescription().getReferencedProjects();
			libs.addAll(Arrays.asList(refs));
//			for (IProject ref:refs) {libs.add(ref);}
		} catch (CoreException e) {e.printStackTrace(logger);}
		return libs;
	}
	public Set<IProject> addLibrary(IProject target,IProject library) {
			List<IProject> libs=new ArrayList<IProject>();
			libs.add(library);
			return addLibraries(target,libs);
	}
	public Set<IProject> addLibraries(IProject target,Collection<IProject> libraries) {
		Set<IProject> libs=getLibraries(target);
		libs.addAll(libraries);
		try {
			target.getDescription().setReferencedProjects(libs.toArray(new IProject[] {}));
			IFile cp = target.getFile(".classpath");
			org.w3c.dom.Element cpRoot = getDocRoot(cp);
			Set<String> paths=new HashSet<String>();
			for (org.w3c.dom.Element cpe:getChildElements(cpRoot)) {
				if (cpe.getAttribute("exported").equals("true")) {paths.add(cpe.getAttribute("path"));}
			}
			for (IProject lib:libs) {
				String path="/"+lib.getName();
				if (!paths.contains(path)) {
				org.w3c.dom.Element newcpe = addElement(cpRoot,"classpathentry");
				newcpe.setAttribute("exported","true");
				newcpe.setAttribute("kind","src");
				newcpe.setAttribute("path",path);
				}
			}
			save(cpRoot,cp);
		} catch (CoreException e) {e.printStackTrace(getLogger());}
		return libs;
	}
	
	public String substringAfterOrAll(String inString, String marker) {
			int index = inString.indexOf(marker);
			if (index>=0) {return inString.substring(index+marker.length());} else {return inString;}
	}
	public String substringAfterOrNull(String inString, String marker) {
		int index = inString.indexOf(marker);
		if (index>=0) {return inString.substring(index+marker.length());} else {return null;}
	}
	public String substringBeforeOrNull(String inString, String marker) {
		int index = inString.indexOf(marker);
		if (index>=0) {return inString.substring(0,index);} else {return null;}
	}
	public String substringBeforeOrAll(String inString, String marker) {
		int index = inString.indexOf(marker);
		if (index>=0) {return inString.substring(0,index);} else {return inString;}
	}
	public boolean isOKorINFO(IStatus status) {
		return status.getSeverity()<=statusInfo;
	}
	
		
		
	public static final String NSXMLNS="http://www.w3.org/2000/xmlns/";
	public static final String NSXSI="http://www.w3.org/2001/XMLSchema-instance";
	public static final String NSWIDWSDL="http://www.ibm.com/xmlns/prod/websphere/scdl/wsdl/6.0.0";
	public static final String NSSCDL="http://www.ibm.com/xmlns/prod/websphere/scdl/6.0.0";
	public static final String XMLEDITOR="org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart";
	/*************** End Added since 6/1/09 ***********************/

	public static void run()  {
		final Utils utils = getSingleton();
		utils.log("Dumping editors...");
		utils.dumpEditors();
		if (true) {return;}
		PBEPath path = new PBEPath("com.ibm.pbe.tdpatterns/com.ibm.pbe.tdpatterns.actions.CBR");
		java.lang.Class clas = path.loadClass();
		utils.dump(clas);
		utils.dumpEditors();
//		String path=utils.getPluginPath("com.ibm.pbe.PVTools7");
//		utils.log(path);
		
//		utils.edit(utils.getActiveFile());
//		if (true) {return;}
		
//		try {
//			ms.write("Hello World\n");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace(utils.getLogger());
//		}
		final Display display = utils.getShell().getDisplay();
		final Color inputColor = display.getSystemColor(SWT.COLOR_MAGENTA);
		class ReaderJob extends Job {
			ReaderJob() {super("ConsoleReader");}
			@Override
			protected IStatus run(IProgressMonitor arg0) {
				utils.log("Job running ok: "+new Date());
				final IOConsole c;
				IConsoleManager cmgr=ConsolePlugin.getDefault().getConsoleManager();
				IConsole[] cs = cmgr.getConsoles();
				for (int i = 0; i < cs.length; i++) {
					IConsole c2 = cs[i];
					if ("Test Console".equals(c2.getName())) {cmgr.removeConsoles(new IConsole[] {c2});}
				}
				c=new IOConsole("Test Console",null); 
				IOConsole[] ca=new IOConsole[] {c}; 
				cmgr.addConsoles(ca);
				
				final IOConsoleOutputStream os = c.newOutputStream();
				cmgr.showConsoleView(c);
				c.activate();
				IOConsoleInputStream is = c.getInputStream();
				LineNumberReader lr = new LineNumberReader(new InputStreamReader(is));
			try {
				is.setColor(inputColor);
				String prompt=utils.getUniqueString()+'>';
				int mark=prompt.length();
				IDocument doc = c.getDocument();
				IDocumentPartitioner ptnr = doc.getDocumentPartitioner();
				
				utils.log("Partitioner="+ptnr);
				synchronized (is) {
					String line="";
					while (!"quit".equals(line)) {
						os.write(prompt);
//						utils.log("ready="+lr.ready());
//						lr.skip(6); 
//						is.appendData("_");
//						os.flush();
						line = lr.readLine();
//						utils.log(line);
					}
//					if (is.available()<=0) {is.wait();}
//						String line2 = lr.readLine();
//						utils.log(line);
//						if (line.equals("quit")) {}
				}
				
//				is.appendData("123");
//				utils.log("Avail="+is.available());
//				utils.log(""+is.read());
//				utils.log("Avail="+is.available());
//				is.appendData("456");
//				utils.log("Avail="+is.available());
//				utils.log(""+is.read());
//				utils.log("Avail="+is.available());
				
//				while ((this.getState()!=Job.NONE)&& (is.available()>0)) {
//					utils.log(""+is.read());
//				utils.log("trying next");
//				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace(utils.getLogger());
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace(utils.getLogger());
			} finally {
				try {
				is.close();
				os.close(); } catch (Exception e) {}
			}
			
//			utils.log("Read:"+is.read());
//			if (is.available()>0) {utils.log("Read:"+is.read());} else {utils.log("Nothing");}
//				} 
				utils.log("Finished: "+new Date());
				return Status.OK_STATUS;
			}
		}
		ReaderJob rdr = new ReaderJob();
		rdr.schedule();
		utils.log("Finished launcher");
		if (true) {return;}
		
		
		utils.getActiveResource();
//		URI uri=URI.createURI("platform:/plugin/com.ibm.pbe.WMBPatterns/plugin.xml");
//		URI urri=URI.createURI("platform:/resource/WMBPatterns/plugin.xml");
//		URI uri=URI.createURI("file:/"+utils.wsr.getLocation()+"/WMBPatterns/plugin.xml");
//		utils.dump(uri);
//		utils.dump(uri.device());
//		utils.dump(uri.devicePath());
//		utils.dump(uri.path());
//		utils.dump(utils.getDoc(uri));
		
		
		
		
		String pluginId="org.eclipse.ui.workbench/resourcetypes";
		Bundle b=Platform.getBundle(pluginId);
		IPreferencesService service = Platform.getPreferencesService();
		IEclipsePreferences rootNode = service.getRootNode();
		org.osgi.service.prefs.Preferences node = rootNode.node("instance/org.eclipse.ui.workbench");
//		utils.dump(node.childrenNames());
//		utils.dump(node.keys());
//		utils.log(node.get("editors", "default"));
		String assocs=node.get("resourcetypes", "default");
		utils.log(assocs);
		if (!assocs.contains("appdef")) {
			utils.log("adding assocs ...");
			String newAssocs=assocs.replace("</editors>","<info extension=\"appdef\" name=\"*\">\r\n<editor id=\"org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart\"/>\r\n<defaultEditor id=\"org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart\"/>\r\n</info>\r\n</editors>" );
			utils.log(newAssocs);
			node.put("resourcetypes", newAssocs);
			String a2=node.get("resourcetypes", "default");
//			utils.log(a2);
		}
		
//		String value = service.getString("org.eclipse.ui.workbench", "resourcetypes","Blah", null);
//		String value = service.getString("org.eclipse.ui.workbench", "fred","Blah", null);
//		utils.log(""+value);
//		String[] names = g.propertyNames();
//		utils.dump("Prefs="+names);
		
//		IFile f=utils.getFile("/.temp/dump.appdef");
//		IEditorDescriptor ied=utils.wb.getEditorRegistry().getDefaultEditor(f.getName());
//		utils.log(ied);
//		IEditorPart ed=(IEditorPart) utils.getActivePart();
//		utils.log((IFile)ed.getEditorInput().getAdapter(IFile.class));
//		utils.log(utils.getActivePart().getSite().getId());
//		utils.log(utils.getActivePart().getSite());
//		String ed="org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart";
//		utils.dumpEditors();
		
		if (true) {return;}

//		Preferences g = utils.getGlobals();
//		try {
//			IContainer ct = utils.getContainer(utils.getFile("PVTools/fred/test.txt"));
//			utils.log(ct);
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace(utils.getLogger());
//		}
//		IFile f=utils.copyWithBackup(utils.getFile("/TempModel/xxx.txt"),utils.getFile("/TempModel/yyy.txt"));
//		utils.log("Backed up to: "+utils.backup(f));
//		utils.log(f);
//		String[] names = g.propertyNames();
//		utils.dump("Prefs="+names);
//		URI uri=utils.testURI("pathmap://WASProfiles/SIBConfigProfile.epx");
//		URI uri=utils.testURI("fred/a.txt");
//		utils.log(uri.scheme());
//		utils.log(utils.string2URI(null));
//		utils.log(utils.string2URI("fred/a.txt"));
//		utils.log(utils.string2URI("pathmap://WASProfiles/SIBConfigProfile.epx"));
//		utils.log(utils.string2URI("file:\\\\WASProfiles\\SIBConfigProfile.epx"));
//		utils.log(utils.string2URI("c://WASProfiles/SIBConfigProfile.epx"));
//		utils.log(utils.string2URI("c:/WASProfiles/SIBConfigProfile.epx"));
//		URI uri=utils.string2URI("c://WASProfiles/SIBConfigProfile.epx");
//		utils.log(uri.scheme());
	}
	

}

