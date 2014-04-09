package com.ibm.pbe.patterns;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.pbe.rsa.interfaces.Runner;

/*
 * each artifact has a sourcePath, a genPath and a templatePath 
 * This class knows how to:
 * Copy the contents of 1M resourceRoots to a target patterns project
 * Append the jet extension
 * Apply a set of edits/changedefs
 * Create a model summarising the templates for input to a pattern creator pattern
 * 
 * Q: why treat files as needing to be rwapped in a resourceRoot - ynot roots in own right? 
 */


public class TemplatiserJetBase implements IResourceVisitor {
	Utils utils=Utils.getSingleton();
	protected IPath templatePath;
	protected Element controlLogic;
	boolean editPaths=true;
	boolean multiProjects=false;
	List sourcePaths=new LinkedList();
	Runner editsApplicator;
	String patternName;
	String patternNameL1;
	String patternNameL;
//	private String patternProject;
	String rootName;
	String rootNameL1;
	String rootNameL;
	String rootNameVar="<c:get select=\"$model/@name\"/>";
	String rootNameVarL1="<c:get select=\"lowercaseFirst($model/@name)\"/>";
	String rootNameVarL="<c:get select=\"lower-case($model/@name)\"/>";
	IProject curProject=null;
	Element curProjectElement=null;
	Element curFolderElement=null;
	Element curElt=null;
	String curIndexStr;
	String curProjectSourcePath;
	String curGenPath;
	IPath curProjectTemplatesPath;
	IContainer curFolder;
	String curFolderPath;
	boolean newRoot=false;
	IPath rootPath=null;
//	Stack<IContainer> cStack=new Stack<IContainer>();
	Map<IContainer,Element> containerElts=new HashMap<IContainer,Element>();
	final static String excludedFolders="bin,gen";
	final static String excludedFiletypes="class,frag";
/* Only for WID	
	final static String excludedFolders="gen,src,.settings,META-INF,xslt";
 */
	ArrayList<ChangeDef> stdEdits = new ArrayList<ChangeDef>();
	ArrayList<ChangeDef> wcdlEdits = new ArrayList<ChangeDef>();
	private int rootCount=0;
	
	public TemplatiserJetBase(Runner editsApplicator) {this.editsApplicator=editsApplicator;}
	
	public void templatise(IResource r, String tplPath) {
		
		templatise(r);
	}
	public void templatise(IResource r) {
		newRoot=true; 
		rootPath=r.getFullPath();
		if (!utils.isProject(r)) {
			Element parentElt=containerElts.get(r.getParent());
			if (parentElt!=null) {newRoot=false;}
		}
		
		try {
			r.accept(this);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
	}
		
	public boolean visit(IResource r) {
		utils.log("Building pattern artefacts for: "+r);
		if (newRoot) {
//			IContainer parent=r.getParent();
//			if (isMultiProjects()) {curIndexStr=""+rootCount;} else {curIndexStr= rootCount==0? "":""+rootCount;}
			curIndexStr=""+rootCount;
			}
		boolean visitChildren=true; Element parentElt=null; 
		if (newRoot) {
			parentElt=controlLogic;
//			curElt=utils.addElement(controlLogic,"container");
//			curElt.setAttribute("index",curIndexStr);
//			if (utils.isContainer(r)) {containerElts.put((IContainer)r,curElt);curElt.setAttribute("source",r.getFullPath().toString());}
//			else {containerElts.put(r.getParent(),curElt);curElt.setAttribute("source",r.getParent().getFullPath().toString());} 
		} 
		else {
			parentElt=containerElts.get(r.getParent());
		}
		curElt=null;
		switch (r.getType()) {
		case IResource.PROJECT:
			curProject=(IProject)r;
			if (!accept(curProject)) {visitChildren=false;break;}
			utils.log("Project Accepted");
			curElt=utils.addElement(parentElt,"project");
			containerElts.put(curProject,curElt);
//			if (isMultiProjects()) {curIndexStr=""+rootCount;}
//			else {curIndexStr= rootCount==1? "":""+rootCount;}
//			curProjectSourcePath=curProject.getName();
			if (newRoot) {curElt.setAttribute("path",r.getFullPath().toString());curElt.setAttribute("index",curIndexStr);} 
			curGenPath="{$model/@projectName"+curIndexStr+"}";curElt.setAttribute("genPath",curGenPath);
//			visitChildren=templatiseProject(curProject, visitChildren); 
//			visitChildren=addControlLogic(curProject, visitChildren); 
//			curFolderElement=curProjectElement;
//			curFolder=curProject;
			break;
		case IResource.FOLDER:
			if (!accept((IFolder)r)) {visitChildren=false;break;}
			utils.log("Folder Accepted");
			curElt=utils.addElement(parentElt,"folder");
			containerElts.put((IFolder)r,curElt);
			if (newRoot) {curGenPath="{$model/@container"+curIndexStr+"}";curElt.setAttribute("genPath",curGenPath);}
			if (newRoot) {curElt.setAttribute("path",r.getFullPath().toString());curElt.setAttribute("index",curIndexStr);} 
//			visitChildren=templatiseFolder((IFolder)r, visitChildren); 
//			if (visitChildren) {visitChildren=addControlLogic((IFolder)r, visitChildren);} 
			break;
		case IResource.FILE:
			if (!accept((IFile)r)) {visitChildren=false;break;}
			utils.log("File Accepted");
			utils.log("Parent: "+parentElt);
			if (newRoot) {
			curElt=utils.addElement(parentElt,"folder");
			containerElts.put(r.getParent(),curElt);
			curElt.setAttribute("path",r.getParent().getFullPath().toString());
			curElt.setAttribute("index",curIndexStr);
			curElt.setAttribute("name",r.getParent().getName());
			curGenPath="{$model/@container"+curIndexStr+"}";
			curElt.setAttribute("genPath",curGenPath);
			parentElt=curElt;
			}
			curElt=utils.addElement(parentElt,"file");
//			curElt.setAttribute("path",r.getName());
//			curFolderPath=r.getProjectRelativePath().toString();
			visitChildren=templatiseFile((IFile)r, visitChildren);
			visitChildren=addControlLogic((IFile)r, visitChildren); 
			break;
		default: break;
		}
		if (curElt!=null) {
			curElt.setAttribute("name",r.getName());
//			parentElt.setAttribute("empty","false");
		}
		if (newRoot && visitChildren) {rootCount++;newRoot=false;}
		utils.log("Visit complete");
		return visitChildren;
	}
	public boolean accept(IProject r) {
		return true;
	}
	public boolean accept(IFolder f) {
		if (excludedFolders.contains(f.getName())) {return false;} else {return true;}
	}
	public boolean accept(IFile f) {
		if (excludedFiletypes.contains(f.getFileExtension())) {return false;} else	{return true;}
	}

	public boolean templatiseProject(IProject project, boolean visitChildren) {
		try {
//			utils.genFolder(templatePath.append(editPath1(project.getName())));
			utils.genFolder(curProjectTemplatesPath);
		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
		return visitChildren;
	}
	public boolean addControlLogic(IProject project, boolean visitChildren) {
		curProjectElement = utils.addElement(controlLogic,"project");
		containerElts.put(project,curProjectElement);
//		n.setAttribute("name",editPath2(project.getName()));
//		n.setAttribute("name",curGenPath);
		curProjectElement.setAttribute("source",project.getFullPath().toString());
		curProjectElement.setAttribute("index",curIndexStr); 
		curElt=curProjectElement;
		return visitChildren;
	}
	public boolean templatiseFolder(IFolder folder, boolean visitChildren) {
		if (excludedFolders.contains(folder.getName())) {return false;}
//		try {
////			utils.genFolder(templatePath.append(editPath1(folder.getFullPath().toString())));
//			utils.genFolder(curProjectTemplatesPath.append(editPath1(folder.getProjectRelativePath().toString())));
//		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
		return visitChildren;
	}
//	public boolean addControlLogic(IFolder folder, boolean visitChildren) {
//		IContainer parent=folder.getParent(); Element parentElt=curFolderElement;
//		if (parent!=curFolder) {getParentElt(folder);}
//		Element n = utils.addElement(parentElt,"folder"); parentElt.setAttribute("empty","false");
//		n.setAttribute("name",editPath2(folder.getProjectRelativePath().toString()));
//		n.setAttribute("source",folder.getFullPath().toString());
////		n.setAttribute("project",curGenPath);
//		curElt=n;
//		return visitChildren;
//	}
//	private Element getElt(IContainer c) {
////		IContainer parent=c.getParent();
//		if (c==curFolder) {return curFolderElement;}
//		Element e=containerElts.get(c);
//		if (e==null) {
//			Element pElt=getElt(c.getParent());
//			e=utils.addElement(pElt,"folder");
//		}
//		return null;
//		
//	}
	public boolean templatiseFile(IFile file, boolean visitChildren) {
		String ext=file.getFileExtension();
		if (("class".equals(ext))||("frag".equals(ext))) {return false;} // ignore these
		try {
			IPath toPath=templatePath.append(file.getFullPath().toString()).addFileExtension("jet");
			IFile toFile=utils.getFile(toPath);
			if (toFile.exists()) {toFile.delete(true,null);}
			else {utils.genFolder(toPath.removeLastSegments(1));}	
			file.copy(toPath, true,null);
			utils.p("Copied: "+file);
		if (("jar".equals(ext))||("bar".equals(ext))) {
//			IPath toPath=templatePath.append(editPath1(file.getFullPath().toString()));
//			IPath toPath=curProjectTemplatesPath.append(editPath1(file.getProjectRelativePath().toString()));
//			file.copy(toPath,true,null);
			return visitChildren;
		}
//		IFile workFile=file;
		
//		if ("msgflow".equals(ext)) {
////			String fname=file.getName()+".xml";
//			Document doc=utils.getNewDocument("reverseWMB");
//			Element root = doc.getDocumentElement();
//			root.setAttribute("pattern", "com.ibm.pbe.esbflowutils");
//			root.setAttribute("operation", "reverseWMB");
//			root.setAttribute("targetProject", ".temp");
//			root.setAttribute("traceProject", ".temp");
//			Element imp=utils.addElement(root,"WMBFlow");
//			imp.setAttribute("name", file.getProjectRelativePath().removeFileExtension().lastSegment());
//			imp.setAttribute("targetFolder", file.getProjectRelativePath().removeLastSegments(1).toString());
//			imp.setAttribute("sourceProject", file.getProject().getName());
//			PatternApplicationStatus status = utils.execDPTK(doc,"com.ibm.pbe.esbflowutils");
//			utils.dump(status);
////			try {file.delete(true, null);} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
//			workFile=utils.getFile("/.temp/"+file.getProjectRelativePath().removeFileExtension().toString()+".xml");
//			utils.log("msgflow file is now: "+workFile);
//		} else 
//			if (ext.equals("wsdl")) {
//			IPath tgt=new Path("/WSDL").append(file.getName());
//			if (!utils.getFile(tgt).exists()) {file.copy(new Path("/WSDL"), true, null);} 
//			return(false);
//		} else if (file.getProjectRelativePath().segmentCount()==1) {
//			if (ext.endsWith("port") || "component".equals(ext)|| ext.startsWith("mfc") || ext.startsWith("module") || "medflow".equals(ext)) {return false;}
//			else if (ext.equals("wcdl")) {visitChildren=false;}
//			else if (".project,.classpath".contains(file.getName())) {return false;}
//		} else if (file.getProjectRelativePath().segment(0).equals("xslt")) {
//			if (!"map,xml".contains(ext)) {return false;}
//		} else if (file.getProjectRelativePath().segment(0).equals(".settings")) {
//			if (!ext.equals("map")) {return false;}
//			return false;
//		}
		
//			IPath toPath=templatePath.append(editPath1(file.getFullPath().toString())).addFileExtension("jet");
			
//			if (workFile!=file) {workFile.delete(true, null);}
//			workFile=toFile;
			editsApplicator.run(toFile); // apply edits
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
			return(false);
		}
		
		return visitChildren;
	}

	
	public boolean addControlLogic(IFile file, boolean visitChildren) {
//		if (!visitChildren) {return visitChildren;}
//		if (("class".equals(ext))||("frag".equals(ext))) {return visitChildren;} // ignore these
		String action;
		String ext=file.getFileExtension();
		if ("jar".equals(ext)||("bar".equals(ext))) {action="copy";} else {action="gen";}
		curElt.setAttribute("action", action);
		utils.p("Adding controller logic for "+file);
		
//		Element n = utils.addElement(curFolderElement,fileType);
//		n.setAttribute("name",r.getName()); 
//		n.setAttribute("source",editPath1(file.getFullPath().toString()));
//		n.setAttribute("source",file.getFullPath().toString());
		Element n=curElt;
		n.setAttribute("startType", "file");
		String path=file.getProjectRelativePath().toString();
		// next few lines handle dptk oddity of treatinng java source file paths differently from other directories
		if ("java".equals(ext)) {
			n.setAttribute("startType", "javaClass");
			getSourcePaths(file.getProject());
			for (Iterator iter = sourcePaths.iterator(); iter.hasNext();) { 
				String sp = (String ) iter.next();
				if (sp.length()==0) {continue;}
				if (path.startsWith(sp)) {path=path.substring(sp.length()+1);break;} //take off '/'
			}
		}
		if ("msgflow".equals(ext)) {n.setAttribute("transform", "com.ibm.pbe.esbflowutils");}
//		n.setAttribute("path",editPath2(path));
//		n.setAttribute("project",editPath2(file.getProject().getName()));
//		n.setAttribute("project",curGenPath);
		return visitChildren;
	}
	private String editPath1(String s) {
		if (!editPaths) {return s;} 
		return s.replaceAll(rootName,patternName).replaceAll(rootNameL,patternNameL).replaceAll(rootNameL1,patternNameL1);
	}
	private String editPath2(String s) {
		if (!editPaths) {return s;} 
		return s.replaceAll(rootName,"\\{\\$model/@name\\}").replaceAll(rootNameL,"\\{\\$model/@nameL\\}").replaceAll(rootNameL1,"\\{\\$model/@nameL1\\}");
	}
	public String lc1(String s) {
		return s.substring(0,1).toLowerCase()+s.substring(1);
	}
	
	private void getSourcePaths(IProject p) {
		utils.log("sourcepaths function entered");
		sourcePaths.clear();
		IJavaProject jp = JavaCore.create(p);
		if (jp==null) {return;}
		utils.p("Getting sourcepaths for: "+jp.getElementName());
		IClasspathEntry[] cpes;
		try {
			jp.open(null); // not necessary?
			cpes = jp.getRawClasspath();
		
			for (int i = 0; i < cpes.length; i++) {
				IClasspathEntry cpe = cpes[i];
//				utils.p(""+cpe.getEntryKind()+":"+cpe.getPath());
				if (cpe.getEntryKind()!=IClasspathEntry.CPE_SOURCE) {continue;}
				sourcePaths.add(cpe.getPath().removeFirstSegments(1).toString());
			}
		} catch (JavaModelException e) {
//			e.printStackTrace(utils.getLogger());  // exception thrown for non-Java project or EAR file - ignore
		}
		
	}
	public Element getControlLogic() {
		return controlLogic;
	}
	public void setControlLogic(Element controlLogic) {
		this.controlLogic = controlLogic;
	}
	public boolean isEditPaths() {
		return editPaths;
	}
	public void setEditPaths(boolean editPath2s) {
		this.editPaths = editPath2s;
	}
	public String getRootName() {
		return rootName;
	}
//	public void setRootName(String rootName) {
//		this.rootName = rootName;
//		rootNameL1=lc1(rootName);
//		rootNameL=rootName.toLowerCase();
//		stdEdits.clear();
//		stdEdits.add(new ChangeDef(rootName, rootNameVar));
//		stdEdits.add(new ChangeDef(rootNameL, rootNameVarL));
//		stdEdits.add(new ChangeDef(rootName, rootNameVar));
//		wcdlEdits.clear();
//		wcdlEdits.addAll(stdEdits);
////		wcdlEdits.add(new ChangeDef("WSDLSTARTIn","<c:get select=\"$entry/@wsdlIn"));
////		wcdlEdits.add(new ChangeDef("WSDLSTARTOut","<c:get select=\"$entry/@wsdlOut"));
////		wcdlEdits.add(new ChangeDef("WSDLEND","\"/>"));
//		wcdlEdits.add(new ChangeDef("<WSDLS/>","<WSDLS><c:iterate select=\"$model/wsdl\" var=\"wsdl\"><c:dump select=\"$wsdl\"/></c:iterate></WSDLS>"));
//		 
////		wcdlEdits.add(new ChangeDef("wsdl=\"outWsdl\"","wsdl=\"<c:get select=\"$entry/@outWsdl\"/>\""));
////		wcdlEdits.add(new ChangeDef("<map/>","<CHANGED/>"));
////		wcdlEdits.add(new ChangeDef("<map/>","hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"));
////		wcdlEdits.add(new ChangeDef("<map/>","<c:iterate select=\"$entry/map\" var=\"map\"><map srcOp=\"<c:get select=\"$map/@srcOp\"/>\" tgtOp=\"<c:get select=\"$map/@tgtOp\"/>\"/></c:iterate>"));
//	}
	public String getPatternName() {
		return patternName;
	}
	public void setPatternName(String patternName) {
		this.patternName = patternName;
		patternNameL1=lc1(patternName);
		patternNameL=patternName.toLowerCase();
	}
	public IPath getTemplatePath() {
		return templatePath;
	}
	public void setTemplatePath(IPath templatePath) {
		this.templatePath = templatePath;
	}
	

	public boolean isMultiProjects() {
		return multiProjects;
	}
	public void setMultiProjects(boolean multiProjects) {
		this.multiProjects = multiProjects;
	}

	
}
