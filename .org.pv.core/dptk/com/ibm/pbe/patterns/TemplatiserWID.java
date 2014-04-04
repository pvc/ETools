package com.ibm.pbe.patterns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
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

public class TemplatiserWID implements IResourceVisitor {
	Utils utils=Utils.getSingleton();
	protected IPath templatePath;
	protected Element controlLogic;
	boolean editPaths=true;
	List sourcePaths=new LinkedList();
	TextFileDocumentProvider tp = new TextFileDocumentProvider();
	String patternName;
	String patternNameL1;
	String patternNameL;
	String rootName;
	String rootNameL1;
	String rootNameL;
	String rootNameVar="<attr node=\"entry\" name=\"name\" />";
	String rootNameVarL1="<attr node=\"entry\" name=\"name\" format=\"L1\"/>";
	String rootNameVarL="<attr node=\"entry\" name=\"name\" format=\"L\"/>";
	ArrayList<ChangeDef> stdEdits = new ArrayList<ChangeDef>();
	ArrayList<ChangeDef> nonstdEdits = new ArrayList<ChangeDef>();
	
		
	public boolean visit(IResource r) {
		utils.log("Building pattern artefacts for: "+r);
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
		try {
			utils.genFolder(templatePath.append(editPath1(folder.getFullPath().toString())));
		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
		return visitChildren;
	}
	public boolean addControlLogic(IFolder folder, boolean visitChildren) {
		Element n = controlLogic.getOwnerDocument().createElement("folder");
		controlLogic.appendChild(n);
		n.setAttribute("name",editPath2(folder.getProjectRelativePath().toString()));
		n.setAttribute("project",editPath2(folder.getProject().getName()));
		return visitChildren;
	}
	public boolean templatiseFile(IFile file, boolean visitChildren) {
		String ext=file.getFileExtension();
		if (("class".equals(ext))||("frag".equals(ext))) {return visitChildren;} // ignore these
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
		} else if (file.getProjectRelativePath().segmentCount()==1) {
			if (ext.endsWith("port") || "component".equals(ext)|| ext.startsWith("mfc") || ext.startsWith("module") || "medflow".equals(ext)) {return false;}
			if (ext.equals("wsdl")) {file.copy(new Path("/WSDL"), true, null); return(false);}
		}
		
			IPath toPath=templatePath.append(editPath1(file.getFullPath().toString())).addFileExtension("pat");
			IFile toFile=utils.getFile(toPath);
			if (toFile.exists()) {toFile.delete(true,null);}
			utils.genFolder(toPath.removeLastSegments(1));	
			workFile.copy(toPath, true,null);
			if (workFile!=file) {workFile.delete(true, null);}
			workFile=toFile;
			List<ChangeDef> changes = getEdits(workFile);
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
					for (int start = 0;;) {
						IRegion r=fr.find(start,chg.getTarget(),true,chg.isCaseSensitive(),false,chg.isRegex());
						if (r==null) {break;} 
						fr.replace(chg.getReplacement(),false);
						start=r.getOffset()+r.getLength();
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
		utils.p("Generating logic for "+file);
		
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
		return s.replaceAll(rootName,"%entry(name)%").replaceAll(rootNameL,"%entry(nameL)%").replaceAll(rootNameL1,"%entry(nameL1)%");
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
		return stdEdits;
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
