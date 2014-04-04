package com.ibm.pbe.patterns;

//Uses adapter to gen profile from diagram
import java.io.ByteArrayInputStream;
import java.io.StringBufferInputStream;
import java.net.URI;
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
import org.eclipse.core.runtime.IAdaptable;
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

import com.ibm.pbe.rsa.interfaces.Executable;


// Notes: Pattern just deletes all existing templates at present - could do with keeping anything whcih does not need overwriting!
public class CreatePatternWID  implements Executable {
	Utils utils=Utils.getSingleton();
	String patName;
	String projName;
	String rootName;
	String rootNameL1;
	String rootNameL;
	List sourcePaths=new LinkedList();
	TextFileDocumentProvider tp = new TextFileDocumentProvider();
	String rootNameVar="<attr node=\"entry\" name=\"name\" />";
	String rootNameVarlc1="<attr node=\"entry\" name=\"name\" format=\"L1\"/>";
	String rootNameVarlc="<attr node=\"entry\" name=\"name\" format=\"L\"/>";
	boolean guessPaths=true;
	List<IResource> resourceList;
	IPath templatePath;
	Element genLogic;
	private TemplatiserWID templatiser;
	
	public void run() {execute();}
	public Object execute(Object o) {execute(); return null;}
	public void execute() {
		List selobs = utils.getBaseSelectionList();
		IResource rsel;IResource source=null;
		resourceList=new ArrayList<IResource>();
		for (Iterator iter = selobs.iterator(); iter.hasNext();) {
			Object selob = iter.next();
			
		if (selob!=null) {
			if (selob instanceof IAdaptable) {
				rsel=(IResource)((IAdaptable)selob).getAdapter(IResource.class);
//				if ((rsel!=null)&&(IResource.PROJECT==rsel.getType())) {source=(IProject)rsel;}
				if (rsel!=null) {resourceList.add(rsel);}
			} 
			if (selob.getClass().getName().equals("com.ibm.wbit.ui.logicalview.model.Module")) {
				try {
					resourceList.add((IProject)selob.getClass().getMethod("getParentProject",null).invoke(selob,null));
				} catch (Exception e) {}
			}
		}
		}
		
		if (resourceList.size()==0) {utils.p("Please select a Module!"); return;}
//		if (true) {utils.p("Succesfully located Project: "+source.getName());return;}
		source=resourceList.get(0);
		utils.log("Selected: "+source);
		rootName=source.getName().split("\\.")[0];
		rootNameL1=lc1(rootName);
		rootNameL=rootName.toLowerCase();
//		if (resourceList.size()>1) {guessPaths=false;}
		
		patName = utils.askForInput("New Pattern from Module", "Enter name of new pattern", rootName);
		if (patName==null || patName.length()==0) {return;}
		
//		new ReverseSCA.run()
		utils.p("Creating Pattern: "+patName);
		Document d=utils.getNewDocument("app");
		Element app=d.getDocumentElement();
		app.setAttribute("patternName",patName);
//		projName="."+patName+"Pattern";
//		app.setAttribute("targetProject",projName); //leave to default
		genLogic=app;
		templatiser=new TemplatiserWID();
		templatiser.setEditPaths(true);
		templatiser.setRootName(rootName);
		templatiser.setPatternName(patName);
//		add(d.getDocumentElement(),p);
		
		
//		IProject[] projects=utils.wsr.getProjects();
//		
//		for (int i = 0; i < projects.length; i++) {
//			IProject p=projects[i];
////			utils.p("Processing project" +p);
//			if (!p.getName().startsWith(rootName)) {continue;}
//			resourceList.add(p);
//		}
//		buildDom(d.getDocumentElement(),resourceList);
		execute(app);
	}
	public void buildDom(Element root, List<IResource> resources) {
		for (Iterator iter = resources.iterator(); iter.hasNext();) {
			IResource r = (IResource) iter.next();
			add(root,r);
		}
	}
	public void copyContent(List<IResource> rList, IPath root) {
		utils.log("Copying to:"+root);
		try {
			utils.genFolder(root); // ensure root folder is generated
			for (Iterator iter = rList.iterator(); iter.hasNext();) {
				IResource r = (IResource) iter.next();
//				IPath tgtRoot=root.append(r.getName());
				IPath copyPath=root.append(r.getFullPath());
				utils.log("Processing: "+r);
				if (!utils.isProject(r)) {
					utils.genFolder(root.append(r.getParent().getFullPath()));
					r.copy(copyPath,true,null);
				} else { // can't copy a project to a non-project
					utils.genFolder(root.append(r.getName()));
					IResource[] resList = ((IProject)r).members(); 
					for (int i2 = 0; i2 < resList.length; i2++) {
						IResource rChild = resList[i2];
						IPath copyPathChild=root.append(rChild.getFullPath());
						rChild.copy(copyPathChild,true,null);
//						utils.p("copying: "+r.getName());
//						IResource existing=utils.wsr.findMember(copyPath);
//						utils.p("Existsing result: "+existing);
//						if (existing!=null) {
//						existing.delete(true,null);  // DO overwrite - must at present because deep copying FOLDERS, not indinvid files
//						utils.p("Deleted: "+copyPath);
////						continue; // DON'T overwrite 
//						}
					}
				}
			}
		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
	}
	/**************************************************************************/
	public void execute(Element root) {
		// todo - find source classpathentry & net it off front of resource path for java
		// suppress class files, & frag
		// how to elim timing problems (dont build auto)
		// 
//		utils.dump(root.getOwnerDocument());
		utils.log("Creating pattern project shell");
		Document d=root.getOwnerDocument();
		PatternApplicationStatus st = utils.execDPTK(d,"com.ibm.pv.patterns.createPattern");
		utils.p(st.getStatus());
//		if (!st.isSuccessful()) {utils.log("Error Applying Pattern - cannot continue");return;};
		d=utils.getDocFromString(st.getAfterModel());
		utils.dump(d);
		root=d.getDocumentElement();
		
		templatiser.setControlLogic(root);
		projName=root.getAttribute("targetProject");
		IPath projPath=new Path("/"+projName);
//		IPath patternPath=projPath.append(root.getAttribute("patternFolder"));
		templatePath=projPath.append(root.getAttribute("templateFolder"));
		templatiser.setTemplatePath(templatePath);
//		utils.log("Templatepath="+templatePath);
//		IResource viewFolder=utils.wsr.findMember(viewPath);
//		if (viewFolder!=null) {try {
//			viewFolder.delete(true,null);
//		} catch (CoreException e) {
//			//			e.printStackTrace(); // won't happen - viewFolder variable is null if folder doesn't exist
//		}}
//		utils.p("Copying files ...");
//		copyContent(resourceList,templatePath);
		utils.log("Populating with templates ...");
		templatise();
//		doFileUpdates(utils.wsr.getFolder(templatePath));
		root.setAttribute("update","true");
		utils.dump(d);
		utils.log("Invoking DPTK");
		st = utils.execDPTK(d,"com.ibm.pv.patterns.createPattern");
		utils.p(st.getStatus());
		utils.log("Pattern creation complete!");
	}
	public void templatise() {
		for (Iterator<IResource> iter = resourceList.iterator(); iter.hasNext();) {
			IResource r = iter.next();
//			templatise(r);
			try {
				r.accept(templatiser);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(utils.getLogger());
			}
		}
	}
	public void templatise(IResource r) {
		boolean prune=false; IResource[] mbrs;
		switch (r.getType()) {
		case IResource.PROJECT:
			IProject p=(IProject)r;
			prune=templatiseProject(p, prune); 
			prune=addGenLogic(p, prune); 
			break;
		case IResource.FILE:
			prune=templatiseFile((IFile)r, prune);
			prune=addGenLogic((IFile)r, prune); 
			break;
		case IResource.FOLDER: 
			prune=templatiseFolder((IFolder)r, prune); 
			prune=addGenLogic((IFolder)r, prune); 
			break;
		default: break;
		}
		if ( (!prune) && utils.isContainer(r)) {
			try {
				mbrs=((IContainer)r).members();
				for (int i = 0; i < mbrs.length; i++) {
					IResource mbr= mbrs[i];
					templatise(mbr);
				}
			} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
		}
	}
	public boolean templatiseProject(IProject project, boolean prune) {
		try {
			utils.genFolder(templatePath.append(project.getName()));
		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
		return prune;
	}
	public boolean addGenLogic(IProject project, boolean prune) {
		Element n = genLogic.getOwnerDocument().createElement("project");
		genLogic.appendChild(n);
		n.setAttribute("name",subVar(project.getName()));
		return prune;
	}
	public boolean templatiseFolder(IFolder folder, boolean prune) {
		try {
			utils.genFolder(templatePath.append(folder.getFullPath()));
		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
		return prune;
	}
	public boolean addGenLogic(IFolder folder, boolean prune) {
		Element n = genLogic.getOwnerDocument().createElement("folder");
		genLogic.appendChild(n);
		n.setAttribute("name",subVar(folder.getProjectRelativePath().toString()));
		n.setAttribute("project",subVar(folder.getProject().getName()));
		return prune;
	}
	public boolean templatiseFile(IFile file, boolean prune) {
		//utils.p("Doing file update on: "+f);
		String ext=file.getFileExtension();
		if (("class".equals(ext))||("frag".equals(ext))||("jar".equals(ext))) {return prune=true;} // ignore these
		IFile workFile=file;
		if ("msgflow".equals(ext)) {
			String fname=file.getName()+".xml";
			Document doc=utils.getNewDocument("reverseWMB");
			Element root = doc.getDocumentElement();
			root.setAttribute("pattern", "com.ibm.pbe.esbflowutils");
			root.setAttribute("operation", "reverseWMB");
			root.setAttribute("targetProject", ".temp");
			root.setAttribute("traceProject", ".temp");
			Element imp=utils.addElement(root,"WMBFlow");
			imp.setAttribute("name", file.getProjectRelativePath().removeFileExtension().lastSegment());
			imp.setAttribute("resource", file.getProjectRelativePath().toString());
			imp.setAttribute("project", file.getProject().getName());
			PatternApplicationStatus status = utils.execDPTK(doc,"com.ibm.pbe.esbflowutils");
			utils.dump(status);
//			try {file.delete(true, null);} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
			workFile=utils.getFile("/.temp/"+fname);
			utils.log("msgflow file is now: "+workFile);
		}
		try {
			IPath toPath=templatePath.append(file.getFullPath()).addFileExtension("pat");
			utils.genFolder(toPath.removeLastSegments(1));	
			workFile.copy(toPath, true,null);
			if (workFile!=file) {workFile.delete(true, null);}
			workFile=utils.getFile(toPath);
			IDocument d=null;
			utils.p(workFile.getCharset());
			//		if (true) return;
			
			FileEditorInput fi = new FileEditorInput(workFile);
			tp.connect(fi);
			d=tp.getDocument(fi);
			if (d.getLength()>0) {try {
				//			utils.p("Doc="+d);
				//			StringBuffer sr = new StringBuffer();
				//			BufferedReader br = new BufferedReader(new InputStreamReader(f.getContents()));
				//			for (;;) {
				//				sr.append(br.readLine());
				//			}
				//			d=new Document(f.getContents());
				FindReplaceDocumentAdapter fr = new FindReplaceDocumentAdapter(d);
				//IRegion find(int startOffset, String findString, boolean forwardSearch, boolean caseSensitive, boolean wholeWord, boolean regExSearch) 
				for (int start = 0;;) {
					//				utils.p("Int:"+start);
					IRegion r=fr.find(start,rootName,true,true,false,false);
					//utils.p("Pos: "+((r!=null)?""+r.getOffset():"-1"));
					if (r==null) {break;} 
					fr.replace(rootNameVar,false);
					start=r.getOffset()+r.getLength();
				}
				for (int start = 0;;) {
					//				utils.p("Int:"+start);
					IRegion r=fr.find(start,rootNameL,true,true,false,false);
					//				utils.p("Pos: "+((r!=null)?""+r.getOffset():"-1"));
					if (r==null) {break;} 
					fr.replace(rootNameVarlc,false);
					start=r.getOffset()+r.getLength();
				}
				for (int start = 0;;) {
					//				utils.p("Int:"+start);
					IRegion r=fr.find(start,rootNameL1,true,true,false,false);
					//				utils.p("Pos: "+((r!=null)?""+r.getOffset():"-1"));
					if (r==null) {break;} 
					fr.replace(rootNameVarlc1,false);
					start=r.getOffset()+r.getLength();
				}
				//			tp.resetDocument(fi);
				
				tp.saveDocument(null,fi,d,true);
				
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(utils.getLogger());
			}
			} // end doclength>0
			tp.disconnect(fi);
			
			
//			IResource temp=utils.wsr.findMember(newPath);
//			if (temp!=null) {temp.delete(true,null);}
//			file.move(newPath,false,true,null);
			
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
		
		return prune;
	}
	public boolean addGenLogic(IFile file, boolean prune) {
		if (prune) {return prune;}
		String ext=file.getFileExtension();
		if (("class".equals(ext))||("frag".equals(ext))) {return prune;} // ignore these
		//utils.p("Continuing!");
		String fileType;
		if ("jar".equals(ext)) {fileType="bin";} else {fileType="file";}
		Element n = genLogic.getOwnerDocument().createElement(fileType);
		genLogic.appendChild(n);
//		n.setAttribute("name",r.getName()); 
		n.setAttribute("source",file.getFullPath().toString());
		String path=file.getProjectRelativePath().toString();
		// next few lines handle dptk oddity of treatinng java source file paths differently from other directories
		if ("java".equals(ext)) {
			getSourcePaths(file.getProject());
			for (Iterator iter = sourcePaths.iterator(); iter.hasNext();) { 
				String sp = (String ) iter.next();
				if (sp.length()==0) {continue;}
				if (path.startsWith(sp)) {path=path.substring(sp.length()+1);break;} //take off '/'
			}
		}
		if ("msgflow".equals(ext)) {n.setAttribute("transform", "com.ibm.pbe.esbflowutils");}
		n.setAttribute("target",subVar(path));
		n.setAttribute("project",subVar(file.getProject().getName()));
		return prune;
	}
	
	
	/**
	 * @param root
	 */
	private void doFileUpdates(IFolder f) {
//		utils.p("Doing file updates for: "+f);
		IResource[] mbrs;
		try {
			mbrs = f.members();
			
			for (int i = 0; i < mbrs.length; i++) {
				IResource r = mbrs[i];
				//utils.p("Processing: "+r);
				
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
	}

	/**
	 * @param file
	 */
	private void doFileUpdate(IFile f) {
		//utils.p("Doing file update on: "+f);
		String ext=f.getFileExtension();
		if (("class".equals(ext))||("frag".equals(ext))||("jar".equals(ext))) {return;} // ignore these
		if ("msgflow".equals(ext)) {
			Document doc=utils.getNewDocument("reverseWMB");
			Element root = doc.getDocumentElement();
			root.setAttribute("pattern", "com.ibm.pbe.esbflowutils");
			root.setAttribute("operation", "reverseWMB");
			if (root.getAttribute("targetProject").length()==0) {root.setAttribute("targetProject", f.getProject().getName());}
			if (root.getAttribute("traceProject").length()==0) {root.setAttribute("traceProject", ".temp");}
			Element imp=utils.addElement(root,"WMBFlow");
			imp.setAttribute("name", f.getProjectRelativePath().removeFileExtension().lastSegment());
			imp.setAttribute("resource", f.getProjectRelativePath().toString());
			imp.setAttribute("project", f.getProject().getName());
			PatternApplicationStatus status = utils.execDPTK(doc,"com.ibm.pbe.esbflowutils");
			utils.dump(status);
			try {f.delete(true, null);} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
			
			f=utils.getFile(f.getFullPath().addFileExtension("xml"));
			utils.log("msgflow file is now: "+f);
		}
		IDocument d=null;
		try {
			//		utils.p(f.getCharset());
			//		if (true) return;
			
			FileEditorInput fi = new FileEditorInput(f);
			tp.connect(fi);
			d=tp.getDocument(fi);
			if (d.getLength()>0) {try {
				//			utils.p("Doc="+d);
				//			StringBuffer sr = new StringBuffer();
				//			BufferedReader br = new BufferedReader(new InputStreamReader(f.getContents()));
				//			for (;;) {
				//				sr.append(br.readLine());
				//			}
				//			d=new Document(f.getContents());
				FindReplaceDocumentAdapter fr = new FindReplaceDocumentAdapter(d);
				//IRegion find(int startOffset, String findString, boolean forwardSearch, boolean caseSensitive, boolean wholeWord, boolean regExSearch) 
				for (int start = 0;;) {
					//				utils.p("Int:"+start);
					IRegion r=fr.find(start,rootName,true,true,false,false);
					//utils.p("Pos: "+((r!=null)?""+r.getOffset():"-1"));
					if (r==null) {break;} 
					fr.replace(rootNameVar,false);
					start=r.getOffset()+r.getLength();
				}
				for (int start = 0;;) {
					//				utils.p("Int:"+start);
					IRegion r=fr.find(start,rootNameL,true,true,false,false);
					//				utils.p("Pos: "+((r!=null)?""+r.getOffset():"-1"));
					if (r==null) {break;} 
					fr.replace(rootNameVarlc,false);
					start=r.getOffset()+r.getLength();
				}
				for (int start = 0;;) {
					//				utils.p("Int:"+start);
					IRegion r=fr.find(start,rootNameL1,true,true,false,false);
					//				utils.p("Pos: "+((r!=null)?""+r.getOffset():"-1"));
					if (r==null) {break;} 
					fr.replace(rootNameVarlc1,false);
					start=r.getOffset()+r.getLength();
				}
				//			tp.resetDocument(fi);
				tp.saveDocument(null,fi,d,true);
				
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(utils.getLogger());
			}
			} // end doclength>0
			tp.disconnect(fi);
			
			IPath newPath=f.getFullPath().addFileExtension("pat");
			IResource temp=utils.wsr.findMember(newPath);
			if (temp!=null) {temp.delete(true,null);}
			f.move(newPath,false,true,null);
			
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
		
	}

	/**
	 * @param p
	 */
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

	/**
	 * @param d
	 * @param p
	 */
	private void add(Element d, IResource r) {
		Element n;IResource[] mbrs;
//		p("Adding: "+r);
			try {
		switch (r.getType()) {
		case IResource.PROJECT:
			n = d.getOwnerDocument().createElement("project");
			d.appendChild(n);
			n.setAttribute("name",subVar(r.getName()));
//			n.setAttribute("suffix",r.getName().substring(patName.length()));
			mbrs = ((IContainer)r).members();
			for (int i = 0; i < mbrs.length; i++) {
				add(d,mbrs[i]);
			}
			break;
		case IResource.FILE:
			String ext=r.getFileExtension();
		//utils.p("Processing file: "+r+", ext="+ext);
			if (("class".equals(ext))||("frag".equals(ext))) {break;} // ignore these
		//utils.p("Continuing!");
			String fileType;
			if ("jar".equals(ext)) {fileType="bin";} else {fileType="file";}
			n = d.getOwnerDocument().createElement(fileType);
			d.appendChild(n);
//			n.setAttribute("name",r.getName()); 
			n.setAttribute("source",r.getFullPath().toString());
			String path=r.getProjectRelativePath().toString();
			// next few lines handle dptk oddity of treatinng java source file paths differently from other directories
			if ("java".equals(ext)) {
				getSourcePaths(r.getProject());
				for (Iterator iter = sourcePaths.iterator(); iter.hasNext();) { 
					String sp = (String ) iter.next();
					if (sp.length()==0) {continue;}
					if (path.startsWith(sp)) {path=path.substring(sp.length()+1);break;} //take off '/'
				}
			}
			n.setAttribute("target",subVar(path));
			n.setAttribute("project",subVar(r.getProject().getName()));
			break;
		case IResource.FOLDER:
			n = d.getOwnerDocument().createElement("folder");
			d.appendChild(n);
//			n.setAttribute("name",r.getName());
			n.setAttribute("name",subVar(r.getProjectRelativePath().toString()));
			n.setAttribute("project",subVar(r.getProject().getName()));
			mbrs = ((IContainer)r).members();
			for (int i = 0; i < mbrs.length; i++) {
				add(d,mbrs[i]);
			}
			break;

		default:
			break;
		}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(utils.getLogger());
			}
		
	}
	
	/**
	 * @param string
	 * @return
	 */
	private String subVar(String s) {
		if (!guessPaths) {return s;} 
		return s.replaceAll(rootName,"%entry(name)%").replaceAll(rootNameL,"%entry(nameL)%").replaceAll(rootNameL1,"%entry(nameL1)%");
	}

	public String lc1(String s) {
		return s.substring(0,1).toLowerCase()+s.substring(1);
	}
	
	public Element getDSLCommand(String s) {
		Document d=utils.getNewDocument("app");
		Element app=d.getDocumentElement();
		app.setAttribute("patternName",patName);
		app.setAttribute("targetProject",projName);
		return app;
	}
	
}
