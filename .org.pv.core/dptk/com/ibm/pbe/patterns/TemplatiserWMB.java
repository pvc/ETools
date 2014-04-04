package com.ibm.pbe.patterns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

import com.ibm.dptk.patternWizard.PatternApplicationStatus;

public class TemplatiserWMB implements IResourceVisitor {
	Utils utils=Utils.getSingleton();
	protected IPath templatePath;
	protected Element controlLogic;
	boolean editPaths=true;
	List sourcePaths=new LinkedList();
	TextFileDocumentProvider tp = new TextFileDocumentProvider();
	String patternName;
	String patternNameL1;
	String patternNameL;
//	private String patternProject;
	String rootName;
	String rootNameL1;
	String rootNameL;
	String rootNameVar="<c:get select=\"$entry/@name\"/>";
	String rootNameVarL1="<c:get select=\"lowercaseFirst($entry/@name)\"/>";
	String rootNameVarL="<c:get select=\"lower-case($entry/@name)\"/>";
	final static String noGenFolders="bin";
/* Only for WID	
	final static String noGenFolders="gen,src,.settings,META-INF,xslt";
 */
	ArrayList<ChangeDef> stdEdits = new ArrayList<ChangeDef>();
	ArrayList<ChangeDef> wcdlEdits = new ArrayList<ChangeDef>();
	
		
	public boolean visit(IResource r) {
//		utils.log("Building pattern artefacts for: "+r);
		boolean visitChildren=true; 
		switch (r.getType()) {
		case IResource.PROJECT:
			IProject p=(IProject)r;
			visitChildren=templatiseProject(p, visitChildren); 
			visitChildren=addControlLogic(p, visitChildren); 
			break;
		case IResource.FILE:
			visitChildren=templatiseFile((IFile)r, visitChildren);
			visitChildren=addControlLogic((IFile)r, visitChildren); 
			break;
		case IResource.FOLDER: 
			visitChildren=templatiseFolder((IFolder)r, visitChildren); 
			visitChildren=addControlLogic((IFolder)r, visitChildren); 
			break;
		default: break;
		}
		return visitChildren;
	}
	public boolean templatiseProject(IProject project, boolean visitChildren) {
		try {
			utils.genFolder(templatePath.append(editPath1(project.getName())));
		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
		return visitChildren;
	}
	public boolean addControlLogic(IProject project, boolean visitChildren) {
		Element n = controlLogic.getOwnerDocument().createElement("project");
		controlLogic.appendChild(n);
		n.setAttribute("name",editPath2(project.getName()));
		return visitChildren;
	}
	public boolean templatiseFolder(IFolder folder, boolean visitChildren) {
		if ("gen,META-INF".contains(folder.getName())) {return false;}
		try {
			utils.genFolder(templatePath.append(editPath1(folder.getFullPath().toString())));
		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
		return visitChildren;
	}
	public boolean addControlLogic(IFolder folder, boolean visitChildren) {
		if (noGenFolders.contains(folder.getName())) {return visitChildren;}
		Element n = controlLogic.getOwnerDocument().createElement("folder");
		controlLogic.appendChild(n);
		n.setAttribute("name",editPath2(folder.getProjectRelativePath().toString()));
		n.setAttribute("project",editPath2(folder.getProject().getName()));
		return visitChildren;
	}
	public boolean templatiseFile(IFile file, boolean visitChildren) {
		String ext=file.getFileExtension();
		if (("class".equals(ext))||("frag".equals(ext))) {return false;} // ignore these
		try {
		if (("jar".equals(ext))||("bar".equals(ext))) {
			IPath toPath=templatePath.append(editPath1(file.getFullPath().toString()));
			file.copy(toPath,true,null);
			utils.p("Copied: "+file);
			return visitChildren;
		}
		IFile workFile=file;
		
		if ("msgflow".equals(ext)) {
//			String fname=file.getName()+".xml";
			Document doc=utils.getNewDocument("reverseWMB");
			Element root = doc.getDocumentElement();
			root.setAttribute("pattern", "com.ibm.pbe.esbflowutils");
			root.setAttribute("operation", "reverseWMB");
			root.setAttribute("targetProject", ".temp");
			root.setAttribute("traceProject", ".temp");
			Element imp=utils.addElement(root,"WMBFlow");
			imp.setAttribute("name", file.getProjectRelativePath().removeFileExtension().lastSegment());
			imp.setAttribute("targetFolder", file.getProjectRelativePath().removeLastSegments(1).toString());
			imp.setAttribute("sourceProject", file.getProject().getName());
			PatternApplicationStatus status = utils.execDPTK(doc,"com.ibm.pbe.esbflowutils");
			utils.dump(status);
//			try {file.delete(true, null);} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
			workFile=utils.getFile("/.temp/"+file.getProjectRelativePath().removeFileExtension().toString()+".xml");
			utils.log("msgflow file is now: "+workFile);
		} else if (ext.equals("wsdl")) {
//			IPath tgt=new Path("/WSDL").append(file.getName());
//			if (!utils.getFile(tgt).exists()) {file.copy(new Path("/WSDL"), true, null);} 
			return(false);
		} else if (file.getProjectRelativePath().segmentCount()==1) {
			if (ext.endsWith("port") || "component".equals(ext)|| ext.startsWith("mfc") || ext.startsWith("module") || "medflow".equals(ext)) {return false;}
			else if (ext.equals("wcdl")) {visitChildren=false;}
			else if (".project,.classpath".contains(file.getName())) {return false;}
		} else if (file.getProjectRelativePath().segment(0).equals("xslt")) {
			if (!"map,xml".contains(ext)) {return false;}
		} else if (file.getProjectRelativePath().segment(0).equals(".settings")) {
//			if (!ext.equals("map")) {return false;}
			return false;
		}
		
			List<ChangeDef> changes = getEdits(workFile);
			IPath toPath=templatePath.append(editPath1(file.getFullPath().toString())).addFileExtension("jet");
			IFile toFile=utils.getFile(toPath);
			if (toFile.exists()) {toFile.delete(true,null);}
			utils.genFolder(toPath.removeLastSegments(1));	
			workFile.copy(toPath, true,null);
			if (workFile!=file) {workFile.delete(true, null);}
			workFile=toFile;
			if (changes.size()==0) {return visitChildren;}
			IDocument d=null;
//			utils.p(workFile.getCharset());
			//		if (true) return;
			
			FileEditorInput fi = new FileEditorInput(workFile);
			tp.connect(fi);
			d=tp.getDocument(fi);
			if (d.getLength()==0) { return visitChildren;}
			try {
				//			utils.p("Doc="+d);
				//			StringBuffer sr = new StringBuffer();
				//			BufferedReader br = new BufferedReader(new InputStreamReader(f.getContents()));
				//			for (;;) {
				//				sr.append(br.readLine());
				//			}
				//			d=new Document(f.getContents());
				FindReplaceDocumentAdapter fr = new FindReplaceDocumentAdapter(d);
				//IRegion find(int startOffset, String findString, boolean forwardSearch, boolean caseSensitive, boolean wholeWord, boolean regExSearch) 
				for (Iterator iter = changes.iterator(); iter.hasNext();) {
					ChangeDef chg = (ChangeDef) iter.next();
//					if (chg.getTarget().equals("<map/>")) {utils.log("Processing mapchange with: "+chg.getReplacement());}
					for (int start = 0;;) {
						IRegion r=fr.find(start,chg.getTarget(),true,chg.isCaseSensitive(),false,chg.isRegex());
						if (r==null) {break;} 
//						if (chg.getTarget().equals("<map/>")) {utils.log("Processing mapchange with: "+chg.getReplacement());utils.log(r);}
						IRegion r2=fr.replace(chg.getReplacement(),false);
//						if (chg.getTarget().equals("<map/>")) {utils.log("rLength="+r.getLength());utils.log(r2);utils.log(r);}
						start=r.getOffset()+r2.getLength();
					}
				}
				
				//			tp.resetDocument(fi);
				
				tp.saveDocument(null,fi,d,true);
				
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(utils.getLogger());
			}
			tp.disconnect(fi);
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
			return(false);
		}
		
		return visitChildren;
	}

	
	public boolean addControlLogic(IFile file, boolean visitChildren) {
		if (!visitChildren) {return visitChildren;}
		String ext=file.getFileExtension();
		if (("class".equals(ext))||("frag".equals(ext))) {return visitChildren;} // ignore these
		String fileType;
		if ("jar".equals(ext)||("bar".equals(ext))) {fileType="bin";} else {fileType="file";}
		utils.p("Adding controller logic for "+file);
		
		Element n = controlLogic.getOwnerDocument().createElement(fileType);
		controlLogic.appendChild(n);
//		n.setAttribute("name",r.getName()); 
		n.setAttribute("source",editPath1(file.getFullPath().toString()));
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
		n.setAttribute("target",editPath2(path));
		n.setAttribute("project",editPath2(file.getProject().getName()));
		return visitChildren;
	}
	private String editPath1(String s) {
		if (!editPaths) {return s;} 
		return s.replaceAll(rootName,patternName).replaceAll(rootNameL,patternNameL).replaceAll(rootNameL1,patternNameL1);
	}
	private String editPath2(String s) {
		if (!editPaths) {return s;} 
		return s.replaceAll(rootName,"\\{\\$entry/@name\\}").replaceAll(rootNameL,"\\{\\$entry/@nameL\\}").replaceAll(rootNameL1,"\\{\\$entry/@nameL1\\}");
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
	public void setRootName(String rootName) {
		this.rootName = rootName;
		rootNameL1=lc1(rootName);
		rootNameL=rootName.toLowerCase();
		stdEdits.clear();
		stdEdits.add(new ChangeDef(rootName, rootNameVar));
		stdEdits.add(new ChangeDef(rootNameL, rootNameVarL));
		stdEdits.add(new ChangeDef(rootName, rootNameVar));
		wcdlEdits.clear();
		wcdlEdits.addAll(stdEdits);
//		wcdlEdits.add(new ChangeDef("WSDLSTARTIn","<c:get select=\"$entry/@wsdlIn"));
//		wcdlEdits.add(new ChangeDef("WSDLSTARTOut","<c:get select=\"$entry/@wsdlOut"));
//		wcdlEdits.add(new ChangeDef("WSDLEND","\"/>"));
		wcdlEdits.add(new ChangeDef("<WSDLS/>","<WSDLS><c:iterate select=\"$entry/wsdl\" var=\"wsdl\"><c:dump select=\"$wsdl\"/></c:iterate></WSDLS>"));
		 
//		wcdlEdits.add(new ChangeDef("wsdl=\"outWsdl\"","wsdl=\"<c:get select=\"$entry/@outWsdl\"/>\""));
//		wcdlEdits.add(new ChangeDef("<map/>","<CHANGED/>"));
//		wcdlEdits.add(new ChangeDef("<map/>","hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"));
//		wcdlEdits.add(new ChangeDef("<map/>","<c:iterate select=\"$entry/map\" var=\"map\"><map srcOp=\"<c:get select=\"$map/@srcOp\"/>\" tgtOp=\"<c:get select=\"$map/@tgtOp\"/>\"/></c:iterate>"));
	}
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
	private List<ChangeDef> getEdits(IFile workFile) {
		utils.log("getedits for "+workFile);
		if ("wcdl".equals(workFile.getFileExtension())) {
			Element root = utils.getDocRoot(workFile);
			Element oldWsdls=utils.getFirstElementAt(root,"WSDLS");
			Element newWsdls = root.getOwnerDocument().createElement("WSDLS");
			root.replaceChild(newWsdls,oldWsdls);
//			List<Element> wsdls = utils.getChildElements(wsdlroot);
//			int count=1;
//			for (Element wsdl:wsdls) {
//				wsdl.removeAttribute("portType");
//				wsdl.removeAttribute("portTypeNS");
//				wsdl.setAttribute("location","WSDLSTART"+wsdl.getAttribute("id")+"WSDLEND");
//			}
			Element portroot=utils.getFirstElementAt(root,"PORTS");
			List<Element> ports = utils.getChildElements(portroot);
			for (Element port:ports) {
				port.removeAttribute("portType");
				port.removeAttribute("portTypeNS");
				port.removeAttribute("port");
				port.removeAttribute("portNS");
				port.removeAttribute("service");
				port.removeAttribute("serviceNS");
				port.removeAttribute("address");
			}
//			exp.setAttribute("wsdl","inWsdl");
//			Element imp=utils.getFirstElementAt(root,"import");
//			imp.setAttribute("wsdl","outWsdl");
//			Element med=utils.getFirstElementAt(root,"mediation");
//			for (Element map:utils.getChildElements(med,"map")) {
//				med.removeChild(map);
//			}
//			utils.addElement(med,"map");
			utils.save(root,workFile);
			return wcdlEdits;
		}
		else {return stdEdits;}
		
	}
	private class ChangeDef {
		String target;
		String replacement;
		boolean regex=false;
		boolean caseSensitive=true;
		public ChangeDef(String target, String replacement) {
			this.target=target;
			this.replacement=replacement;
		}
		public boolean isRegex() {
			return regex;
		}
		public void setRegex(boolean regex) {
			this.regex = regex;
		}
		public String getReplacement() {
			return replacement;
		}
		public void setReplacement(String replacement) {
			this.replacement = replacement;
		}
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
		public boolean isCaseSensitive() {
			return caseSensitive;
		}
		public void setCaseSensitive(boolean caseSensitive) {
			this.caseSensitive = caseSensitive;
		}
	}
}
